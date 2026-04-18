package com.finalproject.safepickup.Service;

import com.finalproject.safepickup.Api.ApiException;
import com.finalproject.safepickup.DTOin.NfcCardDTO;
import com.finalproject.safepickup.DTOout.NfcCardResponseDTO;
import com.finalproject.safepickup.Model.ExitLog;
import com.finalproject.safepickup.Model.NfcCard;
import com.finalproject.safepickup.Model.Student;
import com.finalproject.safepickup.Repository.ExitLogRepository;
import com.finalproject.safepickup.Repository.NfcCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NfcCardService {
    private final NfcCardRepository nfcCardRepository;
    private final ExitLogRepository exitLogRepository;

    /* endpoint will be linked: available NFC cards
     * this for show available NFC cards
     */
    public List<NfcCardResponseDTO> findAll() {
        List<NfcCard> nfcCards = nfcCardRepository.findAll();
        return nfcCards.
                stream()
                        .map(NfcCardResponseDTO::new) // map(nfcCards) -> new DTO's constructor
                        .collect(Collectors.toList()); // return them to list
    }

    // 2- Add new NFC card
    public void addNfcCard(NfcCardDTO dto) {

        // 1- Create NFC card
        NfcCard nfcCard = new NfcCard();
        nfcCard.setUid(dto.getUid());
        nfcCard.setStatus(dto.getStatus().toUpperCase());

        // 2- Save
        nfcCardRepository.save(nfcCard);
    }

    // 3- Update NFC card
    public void updateNfcCard(Integer nfcCardId, NfcCardDTO dto) {

        // 1- Find existing NFC card
        NfcCard oldNfcCard = nfcCardRepository.findNfcCardById(nfcCardId);
        if (oldNfcCard == null) {
            throw new ApiException("NfcCard not found");
        }

        // 2- Update NFC card fields
        oldNfcCard.setUid(dto.getUid());
        oldNfcCard.setStatus(dto.getStatus());

        // 3- Save
        nfcCardRepository.save(oldNfcCard);
    }

    // 4- Delete NFC card
    public void deleteNfcCard(Integer nfcCardId) {
        NfcCard nfcCard = nfcCardRepository.findNfcCardById(nfcCardId);
        if(nfcCard == null) {
            throw new ApiException("NfcCard not found");
        }
        nfcCardRepository.delete(nfcCard);
    }

    /* 5- endpoint will be linked at UI
     * service used when student scan his nfc card to reader
     * */
    public String processNfcScan(String uid) {

        // 1- Find NFC card by UID
        NfcCard nfcCard = nfcCardRepository.findNfcCardByUid(uid);
        if (nfcCard == null) {
            throw new ApiException("Invalid NFC card");
        }

        // 2- Check if NFC card is linked to a student
        Student student = nfcCard.getStudent();
        if (student == null) {
            throw new ApiException("NFC card is not assigned to any student");
        }

        // 3- Find active approved exit request for this student
        ExitLog activeRequest = exitLogRepository.findActiveRequestForStudent(
                student.getId(),
                LocalDateTime.now()
        );

        if (activeRequest == null) {
            // No valid request found - create rejected log
            ExitLog rejectedLog = new ExitLog();
            rejectedLog.setStudent(student);
            rejectedLog.setParent(student.getParent());
            rejectedLog.setNfcCard(nfcCard);
            rejectedLog.setScanTime(LocalDateTime.now());
            rejectedLog.setIsAccepted(false);

            exitLogRepository.save(rejectedLog);

            throw new ApiException("Exit denied. No valid exit request found or request expired");
        }

        // 4- Valid request found - update scan time and link NFC card
        int id =  activeRequest.getId();
        System.out.println("id of active log: "+id);
        activeRequest.setScanTime(LocalDateTime.now());
//        activeRequest.setNfcCard(nfcCard);

        exitLogRepository.save(activeRequest);

        // 5- Return success message with student name
        System.out.print("student scanned successfully");
        return student.getName();
    }

}
