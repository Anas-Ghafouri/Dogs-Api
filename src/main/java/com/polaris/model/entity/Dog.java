package com.polaris.model.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "dogs")
public class Dog {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String name;

        private String breed;
        private String supplier;

        @Column(name = "badge_id")
        private String badgeId;

        @Enumerated(EnumType.STRING)
        private Gender gender;

        @Column(name = "birth_date")
        private LocalDate birthDate;

        @Column(name = "date_acquired")
        private LocalDate dateAcquired;

        @Enumerated(EnumType.STRING)
        @Column(name = "current_status")
        private DogStatus currentStatus;

        @Column(name = "leaving_date")
        private LocalDate leavingDate;

        @Enumerated(EnumType.STRING)
        @Column(name = "leaving_reason")
        private LeavingReason leavingReason;

        @Column(name = "kennelling_characteristic")
        private String kennellingCharacteristic;

        @Column(nullable = false)
        private boolean deleted = false;

        @Column(name = "deleted_at")
        private Instant deletedAt;

        @Column(name = "created_at", nullable = false)
        private Instant createdAt;

        @Column(name = "updated_at", nullable = false)
        private Instant updatedAt;

        @PrePersist
        void prePersist() {
            var now = Instant.now();
            createdAt = now;
            updatedAt = now;
        }

        @PreUpdate
        void preUpdate() {
            updatedAt = Instant.now();
        }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getDateAcquired() {
        return dateAcquired;
    }

    public void setDateAcquired(LocalDate dateAcquired) {
        this.dateAcquired = dateAcquired;
    }

    public DogStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(DogStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public LocalDate getLeavingDate() {
        return leavingDate;
    }

    public void setLeavingDate(LocalDate leavingDate) {
        this.leavingDate = leavingDate;
    }

    public LeavingReason getLeavingReason() {
        return leavingReason;
    }

    public void setLeavingReason(LeavingReason leavingReason) {
        this.leavingReason = leavingReason;
    }

    public String getKennellingCharacteristic() {
        return kennellingCharacteristic;
    }

    public void setKennellingCharacteristic(String kennellingCharacteristic) {
        this.kennellingCharacteristic = kennellingCharacteristic;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
            this.deletedAt = deletedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
