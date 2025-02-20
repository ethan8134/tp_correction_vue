package isis.projet.backend.dao;

import isis.projet.backend.entity.Personne;

/**
 * Interface de projection pour récupérer les informations de participation.
 */
public interface ParticipationInfo {
    /**
     * Retourne la personne concernée par la participation.
     */
    Personne getContributeur();

    /**
     * Retourne le nombre de projets en cours.
     */
    int getNombre();

    /**
     * Retourne le pourcentage d'occupation de la personne.
     */
    Float getPourcentage();
}
