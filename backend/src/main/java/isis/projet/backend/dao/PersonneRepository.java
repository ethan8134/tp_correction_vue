package isis.projet.backend.dao;

import isis.projet.backend.entity.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Integer> {

  /**
   * Récupère les informations de participation d'une personne spécifique.
   * Retourne le nombre de projets en cours et le pourcentage total de participation.
   *
   * @param matricule Identifiant de la personne
   * @return Informations de participation de la personne
   */
  @Query("""
        SELECT p.personne AS contributeur, COUNT(DISTINCT p.projet) AS nombre, COALESCE(SUM(p.pourcentage), 0) AS pourcentage
        FROM Participation p
        WHERE p.personne.matricule = :matricule
        AND p.projet.fin IS NULL
        GROUP BY p.personne
        """)
  Optional<ParticipationInfo> findParticipationInfoByMatricule(Integer matricule);

  /**
   * Récupère les informations de participation de toutes les personnes.
   * Inclut le nombre de projets en cours et le pourcentage total de participation.
   *
   * @return Liste des informations de participation de toutes les personnes
   */
  @Query("""
        SELECT pers AS contributeur, COUNT(DISTINCT p.projet) AS nombre, 
               COALESCE(SUM(p.pourcentage), 0) AS pourcentage
        FROM Personne pers
        LEFT JOIN Participation p ON pers = p.personne AND p.projet.fin IS NULL
        GROUP BY pers
        ORDER BY pers.nom
        """)
  List<ParticipationInfo> findAllParticipationInfo();

}
