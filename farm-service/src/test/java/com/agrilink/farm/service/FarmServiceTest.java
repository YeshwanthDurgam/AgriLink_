package com.agrilink.farm.service;

import com.agrilink.farm.dto.CreateFarmRequest;
import com.agrilink.farm.dto.FarmDto;
import com.agrilink.farm.entity.Farm;
import com.agrilink.farm.repository.FarmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FarmService.
 */
@ExtendWith(MockitoExtension.class)
class FarmServiceTest {

    @Mock
    private FarmRepository farmRepository;

    @InjectMocks
    private FarmService farmService;

    private UUID farmerId;
    private UUID farmId;
    private Farm farm;
    private CreateFarmRequest createRequest;

    @BeforeEach
    void setUp() {
        farmerId = UUID.randomUUID();
        farmId = UUID.randomUUID();

        farm = Farm.builder()
                .id(farmId)
                .farmerId(farmerId)
                .name("Test Farm")
                .description("A test farm")
                .location("Test Location")
                .totalArea(new BigDecimal("100.00"))
                .areaUnit("HECTARE")
                .active(true)
                .build();

        createRequest = CreateFarmRequest.builder()
                .name("Test Farm")
                .description("A test farm")
                .location("Test Location")
                .totalArea(new BigDecimal("100.00"))
                .areaUnit("HECTARE")
                .build();
    }

    @Test
    @DisplayName("Should create farm successfully")
    void shouldCreateFarmSuccessfully() {
        // Given
        when(farmRepository.save(any(Farm.class))).thenReturn(farm);

        // When
        FarmDto result = farmService.createFarm(farmerId, createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Farm");
        assertThat(result.getFarmerId()).isEqualTo(farmerId);
        verify(farmRepository).save(any(Farm.class));
    }

    @Test
    @DisplayName("Should get farms by farmer")
    void shouldGetFarmsByFarmer() {
        // Given
        when(farmRepository.findByFarmerIdAndActiveTrue(farmerId)).thenReturn(List.of(farm));

        // When
        List<FarmDto> result = farmService.getFarmsByFarmer(farmerId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Farm");
    }

    @Test
    @DisplayName("Should get farm by ID")
    void shouldGetFarmById() {
        // Given
        when(farmRepository.findById(farmId)).thenReturn(Optional.of(farm));

        // When
        FarmDto result = farmService.getFarm(farmId, farmerId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(farmId);
    }

    @Test
    @DisplayName("Should update farm successfully")
    void shouldUpdateFarmSuccessfully() {
        // Given
        CreateFarmRequest updateRequest = CreateFarmRequest.builder()
                .name("Updated Farm")
                .build();

        Farm updatedFarm = Farm.builder()
                .id(farmId)
                .farmerId(farmerId)
                .name("Updated Farm")
                .active(true)
                .build();

        when(farmRepository.findByIdAndFarmerId(farmId, farmerId)).thenReturn(Optional.of(farm));
        when(farmRepository.save(any(Farm.class))).thenReturn(updatedFarm);

        // When
        FarmDto result = farmService.updateFarm(farmId, farmerId, updateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Updated Farm");
    }

    @Test
    @DisplayName("Should soft delete farm")
    void shouldSoftDeleteFarm() {
        // Given
        when(farmRepository.findByIdAndFarmerId(farmId, farmerId)).thenReturn(Optional.of(farm));
        when(farmRepository.save(any(Farm.class))).thenReturn(farm);

        // When
        farmService.deleteFarm(farmId, farmerId);

        // Then
        verify(farmRepository).save(argThat(f -> !f.isActive()));
    }
}
