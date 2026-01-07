package com.agrilink.user.repository;

import com.agrilink.user.entity.KycDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for KycDocument entity operations.
 */
@Repository
public interface KycDocumentRepository extends JpaRepository<KycDocument, UUID> {
    
    List<KycDocument> findByUserProfileUserId(UUID userId);
    
    List<KycDocument> findByStatus(KycDocument.KycStatus status);
}
