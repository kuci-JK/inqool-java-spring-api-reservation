package com.example.inqooltennisreservationapi.configuration;

import com.example.inqooltennisreservationapi.model.entity.CourtEntity;
import com.example.inqooltennisreservationapi.model.entity.CourtSurfaceEntity;
import com.example.inqooltennisreservationapi.repository.CourtRepository;
import com.example.inqooltennisreservationapi.repository.CourtSurfaceRepository;
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
    public CommandLineRunner dataInitRunner(CourtRepository courtRepo, CourtSurfaceRepository surfaceRepo) {

        return args -> {
            var dataInit = Arrays.asList(args).contains("data-init");
            log.info("Data init: {}", dataInit ? "enabled" : "disabled");
            if (dataInit) {
                log.info("Starting Data init");

                var surface1 = surfaceRepo
                        .createCourtSurface(new CourtSurfaceEntity(0, "Clay", 10))
                        .orElseThrow(DataInitializationFailed::new);
                var surface2 = surfaceRepo
                        .createCourtSurface(new CourtSurfaceEntity(0, "Grass", 20))
                        .orElseThrow(DataInitializationFailed::new);

                courtRepo.createCourt(new CourtEntity(0, "Court 1", surface1)).orElseThrow(DataInitializationFailed::new);
                courtRepo.createCourt(new CourtEntity(0, "Court 2", surface1)).orElseThrow(DataInitializationFailed::new);
                courtRepo.createCourt(new CourtEntity(0, "Court 3", surface2)).orElseThrow(DataInitializationFailed::new);
                courtRepo.createCourt(new CourtEntity(0, "Court 4", surface2)).orElseThrow(DataInitializationFailed::new);

                log.info("Finished Data init");
            }

        };
    }


}
