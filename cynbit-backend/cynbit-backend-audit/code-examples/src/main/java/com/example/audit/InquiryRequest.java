package com.example.audit;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record InquiryRequest(
    @NotBlank @Size(max = 120) String fullName,
    @NotBlank @Email @Size(max = 254) String email,
    @Size(max = 120) String company,
    @Size(max = 80) String industry,
    @NotBlank @Size(max = 2000) String message
) {}
