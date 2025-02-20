package isis.projet.backend.controller;

import isis.projet.backend.entity.Projet;
import isis.projet.backend.service.ProjetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
@CrossOrigin(origins = "http://localhost:5173") // Permet au front d'accéder au backend
@Slf4j
public class ProjetController {

    @Autowired
    private ProjetService projetService;

    @GetMapping
    public ResponseEntity<List<Projet>> getAllProjets() {
        log.info("Requête reçue pour récupérer tous les projets...");
        List<Projet> projets = projetService.getAllProjets();

        if (projets.isEmpty()) {
            log.warn("Aucun projet trouvé !");
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(projets);
    }
}
