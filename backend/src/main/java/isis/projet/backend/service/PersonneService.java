package isis.projet.backend.service;

import isis.projet.backend.dao.PersonneRepository;
import isis.projet.backend.entity.Personne;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PersonneService {

    @Autowired
    private PersonneRepository personneRepository;

    public List<Personne> getAllPersonnes() {
        log.info("Récupération de toutes les personnes depuis la base de données...");
        return personneRepository.findAll();
    }
}
