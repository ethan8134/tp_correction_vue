package isis.projet.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"PROJET_ID", "PERSONNE_MATRICULE"}))
public class Participation {
    @Id
    @Setter(lombok.AccessLevel.NONE) // Ne génère pas de setter pour cet attribut
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @NonNull
    private String role;

    @NonNull
    @DecimalMin(value = "0.0", message = "Le pourcentage doit être supérieur ou égal à 0.0")
    @DecimalMax(value = "1.0", message = "Le pourcentage doit être inférieur ou égal à 1.0")
    private Float pourcentage;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @NonNull
    private Projet projet;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @NonNull
    private Personne personne;
}