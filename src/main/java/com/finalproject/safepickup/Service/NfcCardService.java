package com.finalproject.safepickup.Service;

import com.finalproject.safepickup.Api.ApiException;
import com.finalproject.safepickup.DTOin.NfcCardDTO;
import com.finalproject.safepickup.DTOout.NfcCardResponseDTO;
import com.finalproject.safepickup.Model.NfcCard;
import com.finalproject.safepickup.Repository.NfcCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NfcCardService {
    private final NfcCardRepository nfcCardRepository;

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
        nfcCard.setStatus(dto.getStatus());

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

}
