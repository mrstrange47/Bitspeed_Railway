package com.example.bitespeed.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentifyRequest {
    private String email;
    private String phoneNumber;
}
