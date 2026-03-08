package com.finalproject.safepickup.DTOout;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CongestionResultDto {
    private double avgJamFactor;
    private String status; // low, mid, high
}