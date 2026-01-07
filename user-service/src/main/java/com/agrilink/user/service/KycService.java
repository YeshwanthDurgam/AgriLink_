package com.agrilink.user.service;

import com.agrilink.user.dto.KycDocumentDto;
import com.agrilink.user.entity.KycDocument;
import com.agrilink.user.entity.UserProfile;
import com.agrilink.user.repository.KycDocumentRepository;
import com.agrilink.user.repository.UserProfileRepository;
import com.agrilink.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for KYC document operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KycService {

    private final KycDocumentRepository kycDocumentRepository;
    private final UserProfileRepository userProfileRepository;

    /**
     * Submit a KYC document.
     */
    @Transactional
    public KycDocumentDto submitDocument(UUID userId, KycDocumentDto request) {
        log.info("Submitting KYC document for user: {}", userId);

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserProfile newProfile = UserProfile.builder()
                            .userId(userId)
                            .build();
                    return userProfileRepository.save(newProfile);
                });

        KycDocument document = KycDocument.builder()
                .userProfile(profile)
                .documentType(request.getDocumentType())
                .documentNumber(request.getDocumentNumber())
                .documentUrl(request.getDocumentUrl())
                .status(KycDocument.KycStatus.PENDING)
                .build();

        KycDocument savedDocument = kycDocumentRepository.save(document);
        log.info("KYC document submitted with id: {}", savedDocument.getId());

        return mapToDto(savedDocument);
    }

    /**
     * Get all KYC documents for a user.
     */
    @Transactional(readOnly = true)
    public List<KycDocumentDto> getDocuments(UUID userId) {
        log.info("Getting KYC documents for user: {}", userId);
        
        return kycDocumentRepository.findByUserProfileUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get KYC document by ID.
     */
    @Transactional(readOnly = true)
    public KycDocumentDto getDocument(UUID documentId) {
        KycDocument document = kycDocumentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("KycDocument", "id", documentId));
        return mapToDto(document);
    }

    private KycDocumentDto mapToDto(KycDocument document) {
        return KycDocumentDto.builder()
                .id(document.getId())
                .userId(document.getUserProfile().getUserId())
                .documentType(document.getDocumentType())
                .documentNumber(document.getDocumentNumber())
                .documentUrl(document.getDocumentUrl())
                .status(document.getStatus())
                .verifiedAt(document.getVerifiedAt())
                .rejectionReason(document.getRejectionReason())
                .createdAt(document.getCreatedAt())
                .build();
    }
}
