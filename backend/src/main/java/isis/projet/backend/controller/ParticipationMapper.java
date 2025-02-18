package isis.projet.backend.controller;

import isis.projet.backend.entity.Participation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ParticipationMapper {

    private final ModelMapper modelMapper;

    public ParticipationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ParticipationDTO toDto(Participation participation) {
        return modelMapper.map(participation, ParticipationDTO.class);
    }
}
