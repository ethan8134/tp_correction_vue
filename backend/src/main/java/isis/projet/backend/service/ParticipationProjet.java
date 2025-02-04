package isis.projet.backend.service;

import isis.projet.backend.dao.ParticipationRepository;
import isis.projet.backend.dao.PersonneRepository;
import isis.projet.backend.dao.ProjetRepository;
import isis.projet.backend.entity.Participation;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class ParticipationProjet {
    private final PersonneRepository personneDao;
    private final ProjetRepository projetDao;
    private final ParticipationRepository participationDao;

    public ParticipationProjet(PersonneRepository personneDao, ProjetRepository projetDao, ParticipationRepository participationDao) {
        this.projetDao = projetDao;
        this.personneDao = personneDao;
        this.participationDao = participationDao;
    }

    /**
     * Enregistre la participation d'une personne (connue par son matricule) à un projet (connu par son code)
     * en précisant son rôle et le pourcentage de son temps qu'il consacre à ce projet.
     * @param matricule Identifiant de la personne
     * @param codeProjet Identifiant du projet
     * @param role Rôle de la personne dans le projet
     * @param pourcentage Pourcentage de temps consacré au projet
     * @return La participation enregistrée
     * @throws IllegalStateException si la personne participerait à plus de 3 projets en même temps
     * @throws IllegalStateException si la personne consacrerait plus de 100% de son temps à des projets
     * @throws IllegalStateException si le projet est déjà terminé
     * @throws DataIntegrityViolationException si la personne participe déjà au projet
     * @throws NoSuchElementException si la personne ou le projet n'existe pas
     */
    @Transactional
    public Participation enregistrerParticipation(Integer matricule, Integer codeProjet, String role, float pourcentage) {
        // On cherche le projet auquel la personne participe
        var affectation = projetDao.findById(codeProjet).orElseThrow();
        if (affectation.getFin() != null) {
            throw new IllegalStateException("Le projet est terminé");
        }
        // On cherche la personne qui participe
        var contributeur = personneDao.findById(matricule).orElseThrow();
        // On cherche les infos sur la participation de la personne à des projets en cours
        var infos = personneDao.findParticipationInfoByMatricule(matricule);
        infos.ifPresent( // Si la personne participe déjà à des projets en cours
                participation -> {
                    if (participation.getNombre() >= 3) {
                        throw new IllegalStateException("La personne ne peut pas participer à plus de 3 projets en même temps");
                    }
                    if (participation.getPourcentage() + pourcentage > 1.0f) {
                        throw new IllegalStateException("La personne ne peut pas consacrer plus de 100% de son temps à des projets");
                    }
                }
        );
        // On crée la participation
        var participation = new Participation(role, pourcentage, affectation, contributeur);
        // on l'enregistre dans la base de données
        participationDao.save(participation);
        return participation;
    }


}

