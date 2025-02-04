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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        results.forEach(result -> log.info("{} participe à {} projets en cours, occupé à {} %",
                                            result.getContributeur(),
                                            result.getNombre(),
                                            result.getPourcentage() * 100));;

        assertEquals(personneRepository.count(), results.size(),
                "On a autant de résultats que de personnes dans la base de données");

        ParticipationInfo first = results.getFirst();
        // Le résultat est trié par nom
        assertEquals("Doe", first.getContributeur().getNom());
        assertEquals(0, first.getNombre(), "Doe n'a aucun projets en cours");
        // Attention au test d'égalité sur les flottants ! cf. https://stackoverflow.com/questions/7554281/junit-assertions-make-the-assertion-between-floats
        assertEquals(0f, first.getPourcentage(), 0.001f, "Doe occupe 0% de son temps");

        ParticipationInfo second = results.get(2);
        assertEquals("Reagan", second.getContributeur().getNom());
        assertEquals(2, second.getNombre(), "Reagan participe à 2 projets en cours");
        assertEquals(0.3f, second.getPourcentage(), 0.001f, "Reagan occupe 30% de son temps");
    }

}