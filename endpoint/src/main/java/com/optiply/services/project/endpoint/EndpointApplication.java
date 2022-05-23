package com.optiply.services.project.endpoint;

import io.micronaut.runtime.Micronaut;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Webshop Service",
                version = "0.0",
                description = "Estagio",
                license = @License(name = "Apache 2.0")
        )
)
public class EndpointApplication {

    public static void main(String[] args) {
        Micronaut.run(EndpointApplication.class, args);
    }
}
