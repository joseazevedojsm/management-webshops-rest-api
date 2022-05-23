package com.optiply.services.project.endpoint.mapper;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.WebshopConfg;
import com.optiply.services.project.endpoint.dto.WebshopConfgDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "jsr330"
)
public interface WebshopConfgMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    WebshopConfg updateWebshopConfgFromDto(WebshopConfgDto dto, @MappingTarget WebshopConfg webshop);
}
