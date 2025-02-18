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

    @Transactional
    public Participation enregistrerParticipation(Integer matricule, Integer codeProjet, String role, float pourcentage) {
        var affectation = projetDao.findById(codeProjet).orElseThrow();
        if (affectation.getFin() != null) {
            throw new IllegalStateException("Le projet est terminé");
        }
        var contributeur = personneDao.findById(matricule).orElseThrow();
        var infos = personneDao.findParticipationInfoByMatricule(matricule);
        infos.ifPresent(participation -> {
            if (participation.getNombre() >= 3) {
                throw new IllegalStateException("La personne ne peut pas participer à plus de 3 projets en même temps");
            }
            if (participation.getPourcentage() + pourcentage > 1.0f) {
                throw new IllegalStateException("La personne ne peut pas consacrer plus de 100% de son temps à des projets");
            }
        });
        var participation = new Participation(role, pourcentage, affectation, contributeur);
        participationDao.save(participation);
        return participation;
    }
}
