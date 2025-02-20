package isis.projet.backend.controller;

import isis.projet.backend.service.ParticipationProjet;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestBody;

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


    @PostMapping
    public ResponseEntity<?> enregistrerParticipation(@RequestBody ParticipationDTO request) {
        try {
            var participation = participationService.enregistrerParticipation(
                    request.getMatricule(),
                    request.getCodeProjet(),
                    request.getRole(),
                    request.getPourcentage()
            );
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
