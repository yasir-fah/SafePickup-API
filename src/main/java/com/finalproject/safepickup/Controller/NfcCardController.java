package com.finalproject.safepickup.Controller;

import com.finalproject.safepickup.Api.ApiResponse;
import com.finalproject.safepickup.DTOin.NfcCardDTO;
import com.finalproject.safepickup.Service.NfcCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/nfc")
@RequiredArgsConstructor
public class NfcCardController {

    private final NfcCardService nfcCardService;

    @GetMapping("/get/cards")
    public ResponseEntity<?> getAllNfcCards() {
        return ResponseEntity.status(200).body(nfcCardService.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNfcCard(@Valid @RequestBody NfcCardDTO nfcCardDTO) {
        nfcCardService.addNfcCard(nfcCardDTO);
        return ResponseEntity.status(200).body(new ApiResponse("NFC Card added successfully"));
    }

    @PutMapping("/update/card/{nfcCardId}")
    public ResponseEntity<?> updateNfcCard(@PathVariable Integer nfcCardId,
                                           @RequestBody @Valid NfcCardDTO dto) {
        nfcCardService.updateNfcCard(nfcCardId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("NFC Card updated successfully"));
    }

    @DeleteMapping("/delete/card/{nfcCardId}")
    public ResponseEntity<?> deleteNfcCard(@PathVariable Integer nfcCardId) {
        nfcCardService.deleteNfcCard(nfcCardId);
        return ResponseEntity.status(200).body(new ApiResponse("NFC Card deleted successfully"));
    }
}
