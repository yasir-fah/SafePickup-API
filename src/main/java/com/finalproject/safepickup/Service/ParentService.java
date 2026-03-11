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
    public void parentExitRequest(Integer parentId, Integer studentId, ExitRequestDTO dto) {

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
        if(nfc == null) {
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
        exitLog.setIsAccepted(isWithinRadius);

        // 8- Save
        exitLogRepository.save(exitLog);

        // 9- Throw error if rejected
        if (!isWithinRadius) {
            throw new ApiException(
                    String.format("Exit request rejected. You are %d meters away from school (maximum allowed: %d meters)",
                            distanceInMeters, MAX_ALLOWED_DISTANCE_METERS)
            );
        }
    }
}
