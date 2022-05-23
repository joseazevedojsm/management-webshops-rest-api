package com.optiply.services.project.endpoint.service;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.WebshopConfg;
import com.optiply.services.project.endpoint.dto.WebshopConfgDto;
import com.optiply.services.project.endpoint.mapper.WebshopConfgMapper;
import com.optiply.services.project.endpoint.repositories.JooqWebshopConfgRepository;
import com.optiply.services.project.endpoint.repositories.JooqWebshopRepository;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

public class WebshopConfgService {

    @Inject
    JooqWebshopConfgRepository webshopConfgRepository;
    @Inject
    JooqWebshopRepository webshopRepository;
    @Inject
    WebshopConfgMapper webshopConfgMapper;

    public Mono<WebshopConfg> getWebshopConfgByHandle(String handle){
        Mono<Integer> id = webshopRepository.findIdbyHandle(handle);
        return Mono.from(id)
                .flatMap(result -> webshopConfgRepository.findById(result));
    }

    public Mono<WebshopConfg> addWebshopConfg(WebshopConfgDto dto, String handle){
        WebshopConfg webshopConfg = webshopConfgMapper.updateWebshopConfgFromDto(dto, new WebshopConfg());

        webshopConfg.setCurrency(webshopConfg.getCurrency() == null ? "EUR" : webshopConfg.getCurrency());
        webshopConfg.setRunJobs(webshopConfg.getRunJobs() == null ? false : webshopConfg.getRunJobs());
        webshopConfg.setMultiSupplier(webshopConfg.getMultiSupplier() == null ? true : webshopConfg.getMultiSupplier());

        Mono<Integer> id = webshopRepository.findIdbyHandle(handle);
        return  Mono.from(id)
                .flatMap(result -> webshopConfgRepository.save(webshopConfg,result));
    }

    public Mono<WebshopConfg> updateWebshopConfg(String handle, WebshopConfgDto dto){
        Mono<Integer> id = webshopRepository.findIdbyHandle(handle);
        return Mono.from(id)
                .flatMap(result -> webshopConfgRepository.findById(result))
                .map(webshopConfg -> {
                    webshopConfg = webshopConfgMapper.updateWebshopConfgFromDto(dto, webshopConfg);
                    System.out.println(webshopConfg+"****************");
                    return webshopConfg;
                })
                .flatMap(result -> webshopConfgRepository.update(result));
    }

}
