package com.example.inqooltennisreservationapi;

import com.example.inqooltennisreservationapi.model.GameType;
import com.example.inqooltennisreservationapi.model.api.CourtDTOs;
import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;
import com.example.inqooltennisreservationapi.model.api.ReservationDTOs;
import com.example.inqooltennisreservationapi.model.api.UserDTOs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;

public class TestUtil {

    private static LocalDateTime time = LocalDateTime.now();

    public static MockHttpServletRequestBuilder applyHeadersAndContent(MockHttpServletRequestBuilder builder, Object body) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        builder.contentType(MediaType.APPLICATION_JSON);
        if (body != null) {
            builder.content(mapper.writeValueAsString(body));
        }

        return builder;
    }

    public static MockHttpServletRequestBuilder applyHeaders(MockHttpServletRequestBuilder builder) throws JsonProcessingException {
        return applyHeadersAndContent(builder, null);
    }

    public static CourtSurfaceDTOs.CourtSurfaceResponseDTO getValidSurfaceResponse(long id, String surfaceName, double pricePerMinute) {
        return new CourtSurfaceDTOs.CourtSurfaceResponseDTO(
                id, surfaceName, pricePerMinute, false
        );
    }

    public static CourtSurfaceDTOs.CourtSurfaceModifyParams getValidSurfaceParams(String surfaceName, double pricePerMinute) {
        return new CourtSurfaceDTOs.CourtSurfaceModifyParams(
                surfaceName,
                pricePerMinute
        );
    }

    public static CourtDTOs.CourtResponseDTO getValidCourtResponse(long id, String name, CourtSurfaceDTOs.CourtSurfaceResponseDTO surface) {
        return new CourtDTOs.CourtResponseDTO(
                id, name, surface, false
        );
    }

    public static CourtDTOs.CourtModifyParams getValidCourtParams(String name, long surfaceId) {
        return new CourtDTOs.CourtModifyParams(
                name,
                surfaceId
        );
    }

    public static ReservationDTOs.ReservationResponseDTO getValidReservationResponse(GameType gameType) {
        var court = getValidCourtResponse(1, "Court", getValidSurfaceResponse(1, "Clay", 10));

        return new ReservationDTOs.ReservationResponseDTO(
                1,
                time,
                time.plusDays(1),
                time.plusDays(1).plusHours(1),
                gameType,
                60 * 10 * gameType.getPriceMultiplier(),
                court,
                new UserDTOs.UserResponseDTO(1, "John Doe", "+420777777777", false),
                false
        );
    }

    public static ReservationDTOs.ReservationModifyParams getValidReservationParams(GameType gameType) {
        return new ReservationDTOs.ReservationModifyParams(
                time.plusDays(1),
                time.plusDays(1).plusHours(1),
                gameType,
                1,
                new UserDTOs.UserModifyParams("John Doe", "+420777777777")
        );
    }

}
