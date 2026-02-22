package com.polaris.model.dto;

import com.polaris.model.entity.DogStatus;
import com.polaris.model.entity.Gender;
import com.polaris.model.entity.LeavingReason;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Serdeable
public record DogRequest(
        @NotBlank String name,
         String breed,
         String supplier,
         String badgeId,
         Gender gender,
         LocalDate birthDate,
         LocalDate dateAcquired,
         DogStatus currentStatus,
         LocalDate leavingDate,
         LeavingReason leavingReason,
         String kennellingCharacteristic) {
}
