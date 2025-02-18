package isis.projet.backend.controller;

import isis.projet.backend.service.ParticipationProjet;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/gestion/participation")
public class ParticipationController {

    private final ParticipationProjet participationService;
    private final ParticipationMapper participationMapper;

    public ParticipationController(ParticipationProjet participationService, ParticipationMapper participationMapper) {
        this.participationService = participationService;
        this.participationMapper = participationMapper;
    }

    /**
     * Enregistre une participation d'une personne à un projet.
     *
     * @param matricule   Identifiant de la personne
     * @param codeProjet  Identifiant du projet
     * @param role        Rôle de la personne dans le projet
     * @param pourcentage Pourcentage de temps consacré au projet
     * @return Réponse HTTP
     */
    @PostMapping
    public ResponseEntity<?> enregistrerParticipation(
            @RequestParam Integer matricule,
            @RequestParam Integer codeProjet,
            @RequestParam String role,
            @RequestParam float pourcentage) {
        try {
            // Enregistrement de la participation
            var participation = participationService.enregistrerParticipation(matricule, codeProjet, role, pourcentage);
            // Transformation en DTO
            var body = participationMapper.toDto(participation);
            return ResponseEntity.ok(body);
        } catch (NoSuchElementException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiErrorDTO(e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(new ApiErrorDTO("Cette personne participe déjà au projet"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiErrorDTO("Une erreur est survenue : " + e.getMessage()));
        }
    }
}
