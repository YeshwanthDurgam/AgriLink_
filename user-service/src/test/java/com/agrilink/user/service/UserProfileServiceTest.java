package com.agrilink.user.service;

import com.agrilink.user.dto.UpdateProfileRequest;
import com.agrilink.user.dto.UserProfileDto;
import com.agrilink.user.entity.UserProfile;
import com.agrilink.user.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserProfileService.
 */
@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    private UUID userId;
    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userProfile = UserProfile.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .country("USA")
                .build();
    }

    @Test
    @DisplayName("Should get existing profile")
    void shouldGetExistingProfile() {
        // Given
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));

        // When
        UserProfileDto result = userProfileService.getProfile(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        verify(userProfileRepository).findByUserId(userId);
    }

    @Test
    @DisplayName("Should create profile if not exists")
    void shouldCreateProfileIfNotExists() {
        // Given
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        // When
        UserProfileDto result = userProfileService.getOrCreateProfile(userId);

        // Then
        assertThat(result).isNotNull();
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    @DisplayName("Should update profile successfully")
    void shouldUpdateProfileSuccessfully() {
        // Given
        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .city("Los Angeles")
                .build();

        UserProfile updatedProfile = UserProfile.builder()
                .id(userProfile.getId())
                .userId(userId)
                .firstName("Jane")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .city("Los Angeles")
                .build();

        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(updatedProfile);

        // When
        UserProfileDto result = userProfileService.updateProfile(userId, request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Smith");
        assertThat(result.getCity()).isEqualTo("Los Angeles");
        verify(userProfileRepository).save(any(UserProfile.class));
    }
}
