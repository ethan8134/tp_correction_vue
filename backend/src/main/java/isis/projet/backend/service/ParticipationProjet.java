package isis.projet.backend.service;

import isis.projet.backend.dao.ParticipationInfo;
import isis.projet.backend.dao.ParticipationRepository;
import isis.projet.backend.dao.PersonneRepository;
import isis.projet.backend.dao.ProjetRepository;
import isis.projet.backend.entity.Participation;
import isis.projet.backend.entity.Personne;
import isis.projet.backend.entity.Projet;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

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

    @Transactional
    public Participation enregistrerParticipation(Integer matricule, Integer codeProjet, String role, float pourcentage) {
        // Vérification si le projet existe
        Projet affectation = projetDao.findById(codeProjet)
                .orElseThrow(() -> new NoSuchElementException("Projet non trouvé avec l'ID : " + codeProjet));

        // Vérification si le projet est terminé
        if (affectation.getFin() != null) {
            throw new IllegalStateException("Le projet est terminé et ne peut plus accepter de participants.");
        }

        // Vérification si la personne existe
        Personne contributeur = personneDao.findById(matricule)
                .orElseThrow(() -> new NoSuchElementException("Personne non trouvée avec le matricule : " + matricule));

        // Vérification du nombre de participations et du temps total alloué
        Optional<ParticipationInfo> participationInfo = personneDao.findParticipationInfoByMatricule(matricule);

        if (participationInfo.isPresent()) {
            ParticipationInfo info = participationInfo.get();

            if (info.getNombre() >= 3) {
                throw new IllegalStateException("La personne ne peut pas participer à plus de 3 projets en même temps.");
            }

            if (info.getPourcentage() + pourcentage > 1.0f) {
                throw new IllegalStateException("La personne ne peut pas consacrer plus de 100% de son temps à des projets.");
            }
        }

        // Création et enregistrement de la participation
        Participation participation = new Participation(role, pourcentage, affectation, contributeur);

        try {
            participationDao.save(participation);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Cette personne participe déjà à ce projet.");
        }

        return participation;
    }
}
