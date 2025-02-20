package isis.projet.backend.controller;

import isis.projet.backend.entity.Personne;
import isis.projet.backend.service.PersonneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/personnes")
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class PersonneController {

    @Autowired
    private PersonneService personneService;

    @GetMapping
    public ResponseEntity<List<Personne>> getAllPersonnes() {
        log.info("🔹 Requête reçue pour récupérer toutes les personnes...");
        List<Personne> personnes = personneService.getAllPersonnes();

        if (personnes.isEmpty()) {
            log.warn("⚠ Aucune personne trouvée !");
            return ResponseEntity.noContent().build();
        }

        log.info("✅ Renvoi de {} personnes", personnes.size());
        return ResponseEntity.ok(personnes);
    }
}
