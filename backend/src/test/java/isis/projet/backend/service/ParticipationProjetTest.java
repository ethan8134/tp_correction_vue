package isis.projet.backend.service;

import isis.projet.backend.dao.PersonneRepository;
import isis.projet.backend.dao.ProjetRepository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ParticipationProjetTest {

    @Autowired
    private ParticipationProjet participationProjet;

    @Autowired
    private PersonneRepository personneDao;

    @Autowired
    private ProjetRepository projetDao;

    @Test
    void unePersonneParticipeAuPlusUneFoisAUnProjet() {
        Integer personneQuiParticipeDeja = 2;
        Integer codeProjet = 1;
        String role = "Developer";
        float pourcentage = 0.3f;

         assertThrows(DataIntegrityViolationException.class,
                () -> participationProjet.enregistrerParticipation(personneQuiParticipeDeja, codeProjet, role, pourcentage),
                "Cette personne participe déjà à ce projet");

    }

    @Test
    void enregistrerParticipation_augmenteParticipation() {
        Integer matricule = 4;
        Integer codeProjet = 4;
        String role = "Developer";
        float pourcentage = 0.3f;

        var infosAvant = personneDao.findParticipationInfoByMatricule(matricule).orElseThrow();

        participationProjet.enregistrerParticipation(matricule, codeProjet, role, pourcentage);

        var infosApres = personneDao.findParticipationInfoByMatricule(matricule).orElseThrow();

        assertEquals(infosAvant.getNombre() + 1, infosApres.getNombre(), "Le nombre de participations a augmenté de 1");
        assertEquals(infosAvant.getPourcentage() + pourcentage, infosApres.getPourcentage(), 0.001F, "Le pourcentage total doit augmenter de " + pourcentage);

    }

    @Test
    void enregistrerParticipation_ShouldThrowException_WhenPersonNotFound() {
        Integer matriculeInconnu = 101;
        Integer codeProjet = 1;
        String role = "Developer";
        float pourcentage = 0.3f;

        assertThrows(NoSuchElementException.class,
                () -> participationProjet.enregistrerParticipation(matriculeInconnu, codeProjet, role, pourcentage));

    }

    @Test
    void enregistrerParticipation_ShouldThrowException_WhenProjetNotFound() {
        Integer matricule = 1;
        Integer codeProjetInconnu = 101;
        String role = "Developer";
        float pourcentage = 0.3f;
        assertThrows(NoSuchElementException.class,
                () -> participationProjet.enregistrerParticipation(matricule, codeProjetInconnu, role, pourcentage));

    }

    @Test
    void enregistrerParticipation_ShouldThrowException_WhenMoreThanThreeProjects() {
        Integer matriculeQuiATroisProjets = 3;
        Integer codeProjet = 4;
        String role = "Tester";

        var infosAvant = personneDao.findParticipationInfoByMatricule(matriculeQuiATroisProjets).orElseThrow();

        float pourcentageDisponible = 1.0F - infosAvant.getPourcentage();


        assertThrows(IllegalStateException.class,
                () -> participationProjet.enregistrerParticipation(matriculeQuiATroisProjets, codeProjet, role, pourcentageDisponible - 0.1F),
                "Cette personne participe déjà à 3 projets");

    }

    @Test
    void enregistrerParticipation_ShouldThrowException_WhenTotalPourcentageExceedsLimit() {
        Integer matriculeQuiADeuxProjets = 4;
        Integer codeProjet = 4;
        String role = "Tester";

        var infosAvant = personneDao.findParticipationInfoByMatricule(matriculeQuiADeuxProjets).orElseThrow();
        float pourcentageDisponible = 1.0F - infosAvant.getPourcentage();

        assertThrows(IllegalStateException.class,
                () -> participationProjet.enregistrerParticipation(matriculeQuiADeuxProjets, codeProjet, role, pourcentageDisponible + 0.1F));

    }
}