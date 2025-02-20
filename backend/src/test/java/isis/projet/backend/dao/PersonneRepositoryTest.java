package isis.projet.backend.dao;

import isis.projet.backend.entity.Personne;
import isis.projet.backend.entity.Participation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j

class PersonneRepositoryTest {

    @Autowired
    private PersonneRepository personneRepository;

    @Test
    void testFindParticipationInfoByMatricule_ExistingMatricule() {
        Integer matricule = 2;
        var personne = personneRepository.findById(matricule).orElseThrow();
        Optional<ParticipationInfo> result = personneRepository.findParticipationInfoByMatricule(matricule);

        assertTrue(result.isPresent());
        var participation = result.get();
        assertEquals(personne,participation.getContributeur());
        assertEquals(2, participation.getNombre(), "La personne 2 participe à 2 projets en cours");
        assertEquals(0.3f, participation.getPourcentage(), "La personne 2 occupe 30% de son temps");
    }

    @Test
    void testFindParticipationInfoByMatricule_NonExistingMatricule() {
        Integer matricule = 9999;

        Optional<ParticipationInfo> result = personneRepository.findParticipationInfoByMatricule(matricule);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllParticipationInfo_WithData() {
        var results = personneRepository.findAllParticipationInfo();

        // Vérifie qu'on a bien des résultats
        assertFalse(results.isEmpty(), "La liste des participations ne doit pas être vide");

        results.forEach(result -> log.info("{} participe à {} projets en cours, occupé à {} %",
                result.getContributeur().getNom(),
                result.getNombre(),
                result.getPourcentage() * 100));

        // Vérifie que la liste est triée
        List<String> nomsAttendus = results.stream()
                .map(result -> result.getContributeur().getNom())
                .sorted() // Tri naturel des noms
                .toList();

        List<String> nomsReels = results.stream()
                .map(result -> result.getContributeur().getNom())
                .toList();

        assertEquals(nomsAttendus, nomsReels, "La liste doit être triée par nom");

        // Vérifie les données pour "Reagan"
        ParticipationInfo second = results.stream()
                .filter(p -> p.getContributeur().getNom().equals("Reagan"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Reagan non trouvé"));

        assertEquals("Reagan", second.getContributeur().getNom());
        assertEquals(2, second.getNombre(), "Reagan participe à 2 projets en cours");
        assertEquals(0.3f, second.getPourcentage(), 0.001f, "Reagan occupe 30% de son temps");
    }


}