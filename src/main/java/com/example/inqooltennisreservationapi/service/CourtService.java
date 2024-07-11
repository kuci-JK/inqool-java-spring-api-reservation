package com.example.inqooltennisreservationapi.service;

import com.example.inqooltennisreservationapi.model.api.CourtDTOs;

import java.util.List;

public interface CourtService {

    CourtDTOs.CourtResponseDTO getCourt(long id);

    CourtDTOs.CourtResponseDTO createCourt(CourtDTOs.CourtModifyParams courtModifyParams);

    CourtDTOs.CourtResponseDTO editCourt(long id, CourtDTOs.CourtModifyParams courtModifyParams);

    CourtDTOs.CourtResponseDTO deleteCourt(long id);

    List<CourtDTOs.CourtResponseDTO> getAllCourts();

}
