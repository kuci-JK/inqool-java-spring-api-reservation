package com.example.inqooltennisreservationapi.service.impl;

import com.example.inqooltennisreservationapi.model.api.CourtDTOs;
import com.example.inqooltennisreservationapi.model.mappers.CourtMapper;
import com.example.inqooltennisreservationapi.repository.CourtRepository;
import com.example.inqooltennisreservationapi.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;
    private final CourtMapper courtMapper;

    @Autowired
    public CourtServiceImpl(CourtRepository courtRepository, CourtMapper courtMapper) {
        this.courtRepository = courtRepository;
        this.courtMapper = courtMapper;
    }


    @Override
    public CourtDTOs.CourtResponseDTO getCourt(long id) {
        var entity = courtRepository.getCourtById(id);
        if (entity.isEmpty()) {
            throw new RuntimeException("Entity not found");
        }
        return courtMapper.entityToResponseDto(entity.get());
    }

    @Override
    public CourtDTOs.CourtResponseDTO createCourt(CourtDTOs.CourtModifyParams courtModifyParams) {
        var newEntity = courtMapper.dtoToEntity(courtModifyParams);
        if (newEntity == null) {
            throw new RuntimeException("Entity not found"); // TODO throw in mapper instead?
        }
        var res = courtRepository.createCourt(newEntity);
        if (res.isEmpty()) {
            throw new RuntimeException("Court creation failed"); // TODO more meaningful error
        }
        return courtMapper.entityToResponseDto(res.get());
    }

    @Override
    public CourtDTOs.CourtResponseDTO editCourt(long id, CourtDTOs.CourtModifyParams courtModifyParams) {
        var entityToSave = courtMapper.dtoToEntity(courtModifyParams);
        entityToSave.setId(id);

        var result = courtRepository.updateCourt(id, entityToSave);
        if (result.isEmpty()) {
            throw new RuntimeException("Court surface update failed"); // TODO
        }
        return courtMapper.entityToResponseDto(result.get());
    }

    @Override
    public CourtDTOs.CourtResponseDTO deleteCourt(long id) {
        var result = courtRepository.deleteCourt(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Court deletion failed"); // TODO
        }
        return courtMapper.entityToResponseDto(result.get());
    }

    @Override
    public List<CourtDTOs.CourtResponseDTO> getAllCourts() {
        return courtRepository.listCourts().stream().map(courtMapper::entityToResponseDto).toList();
    }
}
