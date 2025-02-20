package isis.projet.backend.dao;

import isis.projet.backend.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Integer> {

    /**
     * Calcule la somme des pourcentages de participation d'une personne sur des projets en cours.
     *
     * @param matricule Identifiant de la personne
     * @return La somme des pourcentages de participation sous forme de `Optional<Float>`
     */
    @Query("SELECT COALESCE(SUM(p.pourcentage), 0) FROM Participation p WHERE p.personne.matricule = :matricule AND p.projet.fin IS NULL")
    Optional<Float> findTotalParticipationByMatricule(@Param("matricule") Integer matricule);
}
