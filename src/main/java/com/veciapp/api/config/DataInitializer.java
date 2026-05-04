package com.veciapp.api.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.veciapp.api.entity.AuthorityCenter;
import com.veciapp.api.repository.AuthorityCenterRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedAuthorityCenters(AuthorityCenterRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }
            repository.saveAll(List.of(
                    new AuthorityCenter(
                            "Comisaria Centro",
                            "Av. Jose Balta 501, Chiclayo",
                            -6.77142,
                            -79.84088,
                            "074-101010"),
                    new AuthorityCenter(
                            "Comisaria Norte",
                            "Av. Grau 1240, Chiclayo",
                            -6.76012,
                            -79.83541,
                            "074-202020"),
                    new AuthorityCenter(
                            "Comisaria Oeste",
                            "Av. Salaverry 876, Chiclayo",
                            -6.77891,
                            -79.85310,
                            "074-303030")));
        };
    }
}

