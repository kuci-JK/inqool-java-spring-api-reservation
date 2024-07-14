package com.example.inqooltennisreservationapi;

import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class TestUtil {

    public static MockHttpServletRequestBuilder applyHeadersAndContent(MockHttpServletRequestBuilder builder, Object body) throws JsonProcessingException {
        builder.contentType(MediaType.APPLICATION_JSON);
        if (body != null) {
            builder.content(new ObjectMapper().writeValueAsString(body));
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

}
