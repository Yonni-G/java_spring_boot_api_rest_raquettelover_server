package com.yonni.raquettelover.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // on ajoute ici nos relations avec User et Court
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    // ici on définit notre relation birectionnelle avec la classe Participation
    // une réservation peut avoir plusieurs participations
    // cascade = CascadeType.ALL pour que les opérations sur Reservation se répercutent sur Participation
    // orphanRemoval = true pour supprimer les participations orphelines
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Participation> participations = new HashSet<>();

    @NotNull(message = "La date de réservation est obligatoire")
    private LocalDate reservationAt;

    @NotNull(message = "L'heure de début est obligatoire")
    @Min(value = 0, message = "L'heure de début doit être comprise entre 0 et 23")
    @Max(value = 23, message = "L'heure de début doit être comprise entre 0 et 23")
    private Integer startHour;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 1, message = "La durée minimale est d'une heure")
    @Max(value = 24, message = "La durée maximale est de 24 heures")
    private Integer duration;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
