package com.example.inqooltennisreservationapi.configuration;

import com.example.inqooltennisreservationapi.model.api.CourtDTOs;
import com.example.inqooltennisreservationapi.model.api.CourtSurfaceDTOs;
import com.example.inqooltennisreservationapi.service.CourtService;
import com.example.inqooltennisreservationapi.service.CourtSurfaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@Configuration
public class ExternalConfig {

    private static final Logger log = LoggerFactory.getLogger(ExternalConfig.class);

    @Bean
    public CommandLineRunner dataInitRunner(CourtService courtService, CourtSurfaceService surfaceService) {

        return args -> {
            var dataInit = Arrays.asList(args).contains("data-init");
            log.info("Data init: {}", dataInit ? "enabled" : "disabled");
            if (dataInit) {
                log.info("Starting Data init");

                try {
                    var surface1 = surfaceService.createSurface(new CourtSurfaceDTOs.CourtSurfaceModifyParams("Clay", 10));
                    var surface2 = surfaceService.createSurface(new CourtSurfaceDTOs.CourtSurfaceModifyParams("Grass", 15));

                    courtService.createCourt(new CourtDTOs.CourtModifyParams("Court 1", surface1.getId()));
                    courtService.createCourt(new CourtDTOs.CourtModifyParams("Court 2", surface1.getId()));
                    courtService.createCourt(new CourtDTOs.CourtModifyParams("Court 3", surface2.getId()));
                    courtService.createCourt(new CourtDTOs.CourtModifyParams("Court 4", surface2.getId()));


                    log.info("Finished Data init");
                } catch (Exception e) {
                    log.error("Failed Data init", e);
                    throw new DataInitializationFailed();
                }

            }

        };
    }


}
