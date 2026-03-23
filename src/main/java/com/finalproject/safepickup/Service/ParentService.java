package com.finalproject.safepickup.Service;

import com.finalproject.safepickup.Api.ApiException;
import com.finalproject.safepickup.DTOin.ExitRequestDTO;
import com.finalproject.safepickup.DTOin.ParentDTO;
import com.finalproject.safepickup.DTOout.CongestionResultDto;
import com.finalproject.safepickup.DTOout.ParentResponseDTO;
import com.finalproject.safepickup.Model.*;
import com.finalproject.safepickup.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParentService {

    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final ExitLogRepository exitLogRepository;
    private final NfcCardRepository nfcCardRepository;

    private final CongestionService congestionService;
    private final TwilioVerifyService twilioVerifyService;

    //  maximum allowed distance between parent & school (1.5 km)
    private static final int MAX_ALLOWED_DISTANCE_METERS = 1500;

    // 1- get All parent
    public List<Parent> findAll() {
        List<Parent> parents = parentRepository.findAll();
        return parents;
    }

    // 2- Register new parent
    public void registerParent(ParentDTO dto) {

        // 1- adding user attribute
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setRole("PARENT");

        // 2- adding parent attribute
        Parent parent = new Parent();
        parent.setNationalId(dto.getNationalId());
        parent.setPhone(dto.getPhone());
        // is accepted is false by default  todo: admin should accept Parent accounts

        // 3- link parent & user
        user.setParent(parent);
        parent.setUser(user);

        // 4- save
        userRepository.save(user);
        parentRepository.save(parent);
    }


    // 3- update a parent
    public void updateParent(Integer parentId, ParentDTO dto) {
        // 1- Find existing parent
        Parent oldParent = parentRepository.findParentById(parentId);
        if (oldParent == null) {
            throw new ApiException("Parent not found");
        }

        // 2- Get the associated user
        User user = oldParent.getUser();
        if (user == null) {
            throw new ApiException("Associated user not found");
        }

        // 3- Update user fields
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // todo: hashing at security
        user.setEmail(dto.getEmail());

        // 4- Update parent fields
        oldParent.setNationalId(dto.getNationalId());
        oldParent.setPhone(dto.getPhone());

        // 5- Save both (cascade should handle this, but being explicit)
        parentRepository.save(oldParent);
        userRepository.save(user);
    }


    // 4- delete a parent
    public void deleteParent(Integer parentId) {
        // 1- Find existing parent
        Parent parent = parentRepository.findParentById(parentId);
        if (parent == null) {
            throw new ApiException("Parent not found");
        }

        if (parent.getUser() == null) {
            throw new ApiException("Associated user not found");
        }
        // 2- delete
        User user = parent.getUser();
        userRepository.delete(user);
    }

    /* 5- endpoint will be linked: parent-student-assignment
     * service will return list of available parent
     * */
    public List<ParentResponseDTO> findAllParentsForStudentAssignment() {
        List<Parent> parents = parentRepository.findParentByAccepted();

        return parents.stream()
                .map(ParentResponseDTO::new)
                .collect(Collectors.toList());
    }

    /* 6- endpoint will be linked at UI
     * service will return the result of HERE API(congestion overview) nearby of his students school
     * */
    public CongestionResultDto getTrafficDataForParent(Integer parentId, Integer studentId) {
        Parent parent = parentRepository.findParentById(parentId);
        if (parent == null) {
            throw new ApiException("Parent not found");
        }

        Student student = studentRepository.findStudentById(studentId);
        if (student == null) {
            throw new ApiException("Student not found");
        }

        if (student.getParent() == null) {
            throw new ApiException("Associated parent not found");
        }

        if (parent.getUser() == null) {
            throw new ApiException("Associated student not found");
        }

        if (student.getParent().getId().equals(parent.getId())) {
            // grab student info
            double lat = student.getSchoolLat();
            double lon = student.getSchoolLon();

            // call API
            JsonNode rawResult = congestionService.getTrafficFlow(lat, lon, 40);
            return calculateCongestionAvg(rawResult);
        }
        else {
            throw new ApiException("Student and Parent Are Not Related");
        }
    }

    // helper method for 'getTrafficDataForParent' - calculates average congestion
    public CongestionResultDto calculateCongestionAvg(JsonNode rawResponse) {
        JsonNode results = rawResponse.get("results");

        if (results == null || !results.isArray() || results.isEmpty()) {
            log.info("No results found");
            return CongestionResultDto.builder()
                    .avgJamFactor(0.0)
                    .status("unknown")
                    .build();
        }

        List<Double> avgJamFactors = new ArrayList<>();
        log.info("start loop through results");
        for (JsonNode result : results) {
            JsonNode currentFlow = result.get("currentFlow");
            if (currentFlow != null && currentFlow.has("jamFactor")) {
                avgJamFactors.add(currentFlow.get("jamFactor").asDouble());
            }
        }

        if (avgJamFactors.isEmpty()) {
            log.info("average jam factor is empty");
            return CongestionResultDto.builder()
                    .avgJamFactor(0.0)
                    .status("unknown")
                    .build();
        }

        double avg = avgJamFactors.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        log.info("average jam factor is " + avg);

        String status = "unknown";
        if (avg <= 3.0) {
            status = "low congestion";
        }
        else if (avg <= 5.0) {
            status = "medium congestion";
        }
        else {
            status = "high congestion";
        }
        CongestionResultDto resultDto = new CongestionResultDto();
        resultDto.setAvgJamFactor(avg);
        resultDto.setStatus(status);

        return resultDto;
    }

    /* 7- endpoint will be linked at UI
     * service used when parent ask for student request for his student
     * */
    public ParentResponseDTO parentExitRequest(Integer parentId, Integer studentId, ExitRequestDTO dto) {

        // 1- Find parent
        Parent parent = parentRepository.findParentById(parentId);
        if (parent == null) {
            throw new ApiException("Parent not found");
        }

        // 2- Find student
        Student student = studentRepository.findStudentById(studentId);
        if (student == null) {
            throw new ApiException("Student not found");
        }

        if (student.getParent() == null) {
            throw new ApiException("Student has no associated parent");
        }

        // 3- Verify student belongs to this parent
        if (!student.getParent().getId().equals(parentId)) {
            throw new ApiException("This student does not belong to you");
        }

        // check if student has NFC & link to log
        NfcCard nfc = nfcCardRepository.findNfcCardByStudent_Id(student.getId());
        if (nfc == null) {
            throw new ApiException("NFC Card not found, Student Does Not Has Tag Yet");
        }

        // 4- Check if there's already an active request
        ExitLog activeRequest = exitLogRepository
                .findActiveRequestForStudent(studentId, LocalDateTime.now());

        if (activeRequest != null) {
            throw new ApiException("There is already an active exit request for this student");
        }

        // 5- Calculate distance between parent and school (helper method)
        int distanceInMeters = DistanceCalculator.calculateDistance(
                dto.getParentLat(),
                dto.getParentLon(),
                student.getSchoolLat(),
                student.getSchoolLon()
        );

        // 6- Check if parent is within 1.5km
        boolean isWithinRadius = distanceInMeters <= MAX_ALLOWED_DISTANCE_METERS;


        // 7- Create exit log
        ExitLog exitLog = new ExitLog();
        exitLog.setParent(parent);
        exitLog.setStudent(student);
        exitLog.setRequestTime(LocalDateTime.now());
        exitLog.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        exitLog.setParentLat(dto.getParentLat().toString());
        exitLog.setParentLon(dto.getParentLon().toString());
        exitLog.setNfcCard(nfc);

        // set IsAccepted by check OTP & isWithRadius are true
        exitLog.setIsWithinRadius(isWithinRadius);
        exitLog.setIsOtpVerified(false);
        exitLog.setIsAccepted(false);
        // TODO: add biometric auth


        // 9- Throw error if faraway
        if (!isWithinRadius) {
            throw new ApiException(
                    String.format("Exit request rejected. You are %d meters away from school (maximum allowed: %d meters)",
                            distanceInMeters, MAX_ALLOWED_DISTANCE_METERS)
            );
        }

        // 8- Save
        exitLogRepository.save(exitLog);

        return new ParentResponseDTO(
                parent.getId(),
                parent.getUser().getUsername(),
                parent.getNationalId(),
                parent.getPhone(),
                parent.isAccepted() ? "approved" : "pending"
        );

    }

    // helper method to extract phone number to format of: +966XXXXXXXXX
    protected String extractedPhone(String phone) throws ApiException {
        String format = "+966";
        return phone.replaceFirst("0", "+966");
    }

    // TODO: delete 'parentId' when add security
    public ParentResponseDTO askForOtp(Integer parentId) {

        Parent parent = parentRepository.findParentById(parentId);
        if (parent == null) {
            throw new ApiException("Parent not found");
        }


        List<ExitLog> exitLogs = exitLogRepository.findPendingExitRequestsByParent(parentId, LocalDateTime.now());
        if (exitLogs.isEmpty()) {
            throw new ApiException("Parent has no active exit request for any student");
        }

        ExitLog exitLog = exitLogs.get(0);
        if(exitLog.getLastOtpSentAt() != null) {
            if (LocalDateTime.now().isBefore(exitLog.getLastOtpSentAt().plusMinutes(2))) {
                var remainingTime =
                        ChronoUnit.SECONDS.between(
                                LocalDateTime.now(),
                                exitLog.getLastOtpSentAt().plusMinutes(2)
                        );
                throw new ApiException("you should wait "+ remainingTime +"sec for next OTP");
            }
        }

        // send OTP to parent phone number
        String formatedNumber = extractedPhone(parent.getPhone());
        twilioVerifyService.sendCode(formatedNumber);

        // after successful OTP, add log for lastOtpSendAt
        exitLog.setLastOtpSentAt(LocalDateTime.now());
        exitLogRepository.save(exitLog);

        return new ParentResponseDTO(
                parent.getId(),
                parent.getUser().getUsername(),
                parent.getNationalId(),
                parent.getPhone(),
                parent.isAccepted() ? "approved" : "pending"
        );
    }

    // TODO: delete 'parentId' when add security
    public void verifyExitOTP(Integer parentId, String phoneNumber, String otpCode) {

        // 1- Find parent's pending exit requests (not expired, waiting for OTP)
        List<ExitLog> pendingRequests = exitLogRepository
                .findPendingExitRequestsByParent(parentId, LocalDateTime.now());

        if (pendingRequests.isEmpty()) {
            throw new ApiException("No pending exit request found or request expired");
        }

        // Get the most recent request
        ExitLog exitLog = pendingRequests.get(0);

        // 2- Verify OTP via Twilio
        String extractedPhone = extractedPhone(phoneNumber);
        boolean isValid = twilioVerifyService.verifyCode(extractedPhone, otpCode);
        if (!isValid) {
            throw new ApiException("Invalid OTP");
        }

        // 4- Mark OTP as verified
        exitLog.setIsOtpVerified(true);

        // 5- Update final approval status
        boolean distanceOk = exitLog.isIsWithinRadius();
        boolean otpOk = exitLog.isIsOtpVerified();

        if (distanceOk && otpOk) {
            exitLog.setIsAccepted(true);
        }
        exitLogRepository.save(exitLog);
    }

    // TODO: add service receives Biometric results
}
