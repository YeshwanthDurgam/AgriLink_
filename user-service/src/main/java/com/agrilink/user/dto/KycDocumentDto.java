package com.agrilink.user.dto;

import com.agrilink.user.entity.KycDocument;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for KYC document information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KycDocumentDto {

    private UUID id;
    private UUID userId;

    @NotBlank(message = "Document type is required")
    private String documentType;

    private String documentNumber;
    private String documentUrl;
    private KycDocument.KycStatus status;
    private LocalDateTime verifiedAt;
    private String rejectionReason;
    private LocalDateTime createdAt;
}
