package com.optiply.services.project.endpoint.mapper;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.Webshop;
import com.optiply.services.project.endpoint.dto.WebshopDto;
import org.mapstruct.*;

@Mapper(
        componentModel = "jsr330"
)
public interface WebshopMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Webshop updateWebshopFromDto(WebshopDto dto, @MappingTarget Webshop webshop);
}
