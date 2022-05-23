package com.optiply.services.project.endpoint.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(name = "WebshopConfigurationDTO", description = "Webshop configuration description")
public class WebshopConfgDto {
    @Schema(description="Webshop Configuration MULTI SUPPLIER",nullable = true,defaultValue = "false")
    @Getter @Setter private Boolean multiSupplier;
    @Schema(description="Webshop Configuration RUN JOBS",nullable = true, defaultValue = "true")
    @Getter @Setter private Boolean runJobs;
    @Schema(description="Webshop Configuration CURRENCY", format = "ISO-4127", defaultValue = "EUR",nullable = true)
    @Getter @Setter private String  currency;
}
