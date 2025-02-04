package isis.projet.backend.dao;

import isis.projet.backend.entity.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonneRepository extends JpaRepository<Personne, Integer> {
  /**
   * Calcule les infos sur la participation d'une personne dans des projets en cours
   * @param matricule L'identifiant de la personne
   * @return Un Optional avec les infos sur la participation de la personne dans des projets en cours ou vide si la personne n'a pas de participation.
   */
  @Query("""
        SELECT p.personne AS contributeur, COUNT(DISTINCT p.projet) AS nombre, SUM(p.pourcentage) AS pourcentage
        FROM Participation p
        WHERE p.personne.matricule = :matricule
        AND p.projet.fin IS NULL
        GROUP BY p.personne
        """)
  Optional<ParticipationInfo> findParticipationInfoByMatricule(Integer matricule);

  /**
   * Calcule les infos sur la participation de toutes les personnes dans des projets en cours
   * @return une liste des infos sur la participation de toutes les personnes dans des projets en cours
   * Note : on utilise LEFT JOIN pour inclure les personnes qui n'ont pas de participation
   * cf. <a href="https://sql.sh/cours/jointures/left-join">LEFT JOIN</a>
   */
  @Query("""
        SELECT pers AS contributeur, COUNT(DISTINCT p.projet) AS nombre, COALESCE(SUM(p.pourcentage), 0) AS pourcentage
        FROM Personne pers
        LEFT JOIN Participation p ON pers = p.personne AND p.projet.fin IS NULL
        GROUP BY p.personne
        ORDER BY p.personne.nom
        """)
  List<ParticipationInfo> findAllParticipationInfo();
}