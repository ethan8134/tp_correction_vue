package isis.projet.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Personne {

    @Id
    @Setter(lombok.AccessLevel.NONE) // Ne génère pas de setter pour cet attribut
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matricule;

    @Column(nullable = false)
    @NonNull
    private String nom;

    @Column(nullable = false)
    @NonNull
    private String prenom;

    @Column(nullable = false)
    @NonNull
    private String poste;

    @JsonIgnore // Évite la récursion infinie
    @ToString.Exclude
    @OneToMany(mappedBy = "personne", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Participation> affectations = new ArrayList<>();

    @JsonIgnore // Évite la récursion infinie
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Personne superieur;

    @JsonIgnore // Évite la récursion infinie
    @ToString.Exclude
    @OneToMany(mappedBy = "superieur", orphanRemoval = true)
    private List<Personne> subordonnes = new ArrayList<>();
}
