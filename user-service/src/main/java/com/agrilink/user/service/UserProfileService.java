package com.agrilink.user.service;

import com.agrilink.user.dto.UpdateProfileRequest;
import com.agrilink.user.dto.UserProfileDto;
import com.agrilink.user.entity.UserProfile;
import com.agrilink.user.repository.UserProfileRepository;
import com.agrilink.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for user profile operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    /**
     * Get user profile by user ID, creating if not exists.
     */
    @Transactional
    public UserProfileDto getOrCreateProfile(UUID userId) {
        log.info("Getting or creating profile for user: {}", userId);
        
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserProfile newProfile = UserProfile.builder()
                            .userId(userId)
                            .build();
                    return userProfileRepository.save(newProfile);
                });

        return mapToDto(profile);
    }

    /**
     * Get user profile by user ID.
     */
    @Transactional(readOnly = true)
    public UserProfileDto getProfile(UUID userId) {
        log.info("Getting profile for user: {}", userId);
        
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userId", userId));

        return mapToDto(profile);
    }

    /**
     * Update user profile.
     */
    @Transactional
    public UserProfileDto updateProfile(UUID userId, UpdateProfileRequest request) {
        log.info("Updating profile for user: {}", userId);
        
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> UserProfile.builder().userId(userId).build());

        if (request.getFirstName() != null) {
            profile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            profile.setLastName(request.getLastName());
        }
        if (request.getDateOfBirth() != null) {
            profile.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getAddress() != null) {
            profile.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            profile.setCity(request.getCity());
        }
        if (request.getState() != null) {
            profile.setState(request.getState());
        }
        if (request.getCountry() != null) {
            profile.setCountry(request.getCountry());
        }
        if (request.getPostalCode() != null) {
            profile.setPostalCode(request.getPostalCode());
        }
        if (request.getProfilePictureUrl() != null) {
            profile.setProfilePictureUrl(request.getProfilePictureUrl());
        }
        if (request.getBio() != null) {
            profile.setBio(request.getBio());
        }

        UserProfile savedProfile = userProfileRepository.save(profile);
        log.info("Profile updated for user: {}", userId);

        return mapToDto(savedProfile);
    }

    private UserProfileDto mapToDto(UserProfile profile) {
        return UserProfileDto.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .fullName(profile.getFullName())
                .dateOfBirth(profile.getDateOfBirth())
                .address(profile.getAddress())
                .city(profile.getCity())
                .state(profile.getState())
                .country(profile.getCountry())
                .postalCode(profile.getPostalCode())
                .profilePictureUrl(profile.getProfilePictureUrl())
                .bio(profile.getBio())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
