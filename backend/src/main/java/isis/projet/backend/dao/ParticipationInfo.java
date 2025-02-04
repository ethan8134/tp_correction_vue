package isis.projet.backend.dao;

import isis.projet.backend.entity.Personne;

/**
 * Une projection utilisée comme résultat de requêtes JPQL
 */
public interface ParticipationInfo {
    /**
     * @return la {@link isis.projet.backend.entity.Personne} concernée
     */
    Personne getContributeur();
    /**
     * @return Le pourcentage d'occupation de la personne dans des projets en cours
     */
    Float getPourcentage();
    /**
     * @return Le nombre de projets en cours dans lesquels la personne participe
     */
    int getNombre();

}