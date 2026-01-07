package com.agrilink.user.controller;

import com.agrilink.common.dto.ApiResponse;
import com.agrilink.user.dto.KycDocumentDto;
import com.agrilink.user.service.KycService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for KYC document operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users/kyc")
@RequiredArgsConstructor
public class KycController {

    private final KycService kycService;

    /**
     * Submit a KYC document.
     * POST /api/v1/users/kyc
     */
    @PostMapping
    public ResponseEntity<ApiResponse<KycDocumentDto>> submitDocument(
            Authentication authentication,
            @Valid @RequestBody KycDocumentDto request) {
        UUID userId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        KycDocumentDto document = kycService.submitDocument(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("KYC document submitted successfully", document));
    }

    /**
     * Get all KYC documents for current user.
     * GET /api/v1/users/kyc
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<KycDocumentDto>>> getDocuments(Authentication authentication) {
        UUID userId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        List<KycDocumentDto> documents = kycService.getDocuments(userId);
        return ResponseEntity.ok(ApiResponse.success(documents));
    }

    /**
     * Get KYC document by ID.
     * GET /api/v1/users/kyc/{documentId}
     */
    @GetMapping("/{documentId}")
    public ResponseEntity<ApiResponse<KycDocumentDto>> getDocument(@PathVariable UUID documentId) {
        KycDocumentDto document = kycService.getDocument(documentId);
        return ResponseEntity.ok(ApiResponse.success(document));
    }
}
