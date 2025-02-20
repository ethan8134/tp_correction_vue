package isis.projet.backend.service;

import isis.projet.backend.dao.ProjetRepository;
import isis.projet.backend.entity.Projet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    public List<Projet> getAllProjets() {
        log.info("Récupération de tous les projets depuis la base de données...");
        return projetRepository.findAll();
    }
}
