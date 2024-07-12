package com.example.inqooltennisreservationapi.service.impl;

import com.example.inqooltennisreservationapi.exceptions.DatabaseException;
import com.example.inqooltennisreservationapi.exceptions.EntityNotFoundException;
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
            throw new EntityNotFoundException(String.format("Court id: %s not found", id));
        }
        return courtMapper.entityToResponseDto(entity.get());
    }

    @Override
    public CourtDTOs.CourtResponseDTO createCourt(CourtDTOs.CourtModifyParams courtModifyParams) {
        var newEntity = courtMapper.dtoToEntity(courtModifyParams);

        var res = courtRepository.createCourt(newEntity);
        if (res.isEmpty()) {
            throw new DatabaseException("Court creation failed");
        }
        return courtMapper.entityToResponseDto(res.get());
    }

    @Override
    public CourtDTOs.CourtResponseDTO editCourt(long id, CourtDTOs.CourtModifyParams courtModifyParams) {
        // TODO disable surface edit when reservation in the future... ?
        var entityToSave = courtMapper.dtoToEntity(courtModifyParams);
        entityToSave.setId(id);

        if (!courtExists(id)) {
            throw new EntityNotFoundException(String.format("Court id: %s not found", id));
        }

        var result = courtRepository.updateCourt(id, entityToSave);
        if (result.isEmpty()) {
            throw new DatabaseException(String.format("Court (courtId: %s) update failed", id));
        }
        return courtMapper.entityToResponseDto(result.get());
    }

    @Override
    public CourtDTOs.CourtResponseDTO deleteCourt(long id) {
        // TODO need to soft delete reservations... or disable if used...
        if (!courtExists(id)) {
            throw new EntityNotFoundException(String.format("Court id: %s not found", id));
        }

        var result = courtRepository.deleteCourt(id);
        if (result.isEmpty()) {
            throw new DatabaseException(String.format("Failed to delete court (id: %s)", id));
        }
        return courtMapper.entityToResponseDto(result.get());
    }

    @Override
    public List<CourtDTOs.CourtResponseDTO> getAllCourts() {
        return courtRepository.listCourts().stream().map(courtMapper::entityToResponseDto).toList();
    }

    private boolean courtExists(long id) {
        return courtRepository.getCourtById(id).isPresent();
    }
}
