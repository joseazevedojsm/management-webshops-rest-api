package com.optiply.services.project.endpoint.repositories;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.Webshop;
import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.WebshopConfg;
import reactor.core.publisher.Mono;

public interface WebshopConfigRepository {
    Mono<WebshopConfg> save(WebshopConfg webshopConfg,int id);

    Mono<WebshopConfg> update(WebshopConfg webshopConfg);

    Mono<WebshopConfg> findById(int id);
}
