package com.optiply.services.project.endpoint.dto;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.Webshop;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(name = "WebshopDTO", description = "Webshop description")
public class WebshopDto {

    @Schema(description="Webshop URL",pattern = "https://",required = true)
    @Getter @Setter private String url;
    @Schema(description="Webshop HANDLE",required = true)
    @Getter @Setter private String handle;
    @Schema(description="Webshop HANDLE",nullable = true,pattern = "xxxxx@xxx.xx")
    @Getter @Setter private String[] emails;
    @Schema(description="Webshop SERVICE LEVEL CATEGORY A",required = true,maximum = "99",minimum = "1")
    @Getter @Setter private Integer serviceLevelCatA;
    @Schema(description="Webshop SERVICE LEVEL CATEGORY B",required = true,maximum = "99",minimum = "1")
    @Getter @Setter private Integer serviceLevelCatB;
    @Schema(description="Webshop SERVICE LEVEL CATEGORY C",required = true,maximum = "99",minimum = "1")
    @Getter @Setter private Integer serviceLevelCatC;
    @Schema(description="Webshop INTEREST RATE",required = true,defaultValue = "20",nullable = true)
    @Getter @Setter private Integer interestRate;

}
