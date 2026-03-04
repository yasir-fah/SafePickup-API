package com.finalproject.safepickup.Repository;

import com.finalproject.safepickup.Model.NfcCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NfcCardRepository extends JpaRepository<NfcCard, Integer> {

    NfcCard findNfcCardById(Integer id);

}
