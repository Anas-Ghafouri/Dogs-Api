package com.polaris.model.dto;

import com.polaris.model.entity.DogStatus;
import com.polaris.model.entity.Gender;
import com.polaris.model.entity.LeavingReason;

import java.time.Instant;
import java.time.LocalDate;

public record DogResponse(
        Long id,
        String name,
        String breed,
        String supplier,
        String badgeId,
        Gender gender,
        LocalDate birthDate,
        LocalDate dateAcquired,
        DogStatus currentStatus,
        LocalDate leavingDate,
        LeavingReason leavingReason,
        String kennellingCharacteristic,
        boolean deleted,
        Instant deletedAt,
        Instant createdAt,
        Instant updatedAt) {
}
