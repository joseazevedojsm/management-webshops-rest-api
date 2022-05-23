package com.optiply.services.project.endpoint.repositories;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.Webshop;
import org.jooq.Condition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface WebshopRepository {

    Flux<Webshop> findAll();

    Mono<Webshop> findById(int id);

    Mono<Webshop> save(Webshop webshop);

    Mono<Webshop> update(Webshop webshop);

    Mono<Boolean> delete(int id);

    Mono<Integer> findIdbyHandle(String handle);

    Flux<Webshop> findAndFilter(List<Condition> conditionList);

    Flux<Webshop> findAndSort(String coluna);

    Flux<Webshop> findbyFilterAndSort(String colunaSearch, List<Condition> conditionList);
}

//    Flux<Webshop> findAndFilter(String coluna,String valor, String condicao);
//    Flux<Webshop> findAndFilter(String coluna,int valor, String condicao);
//    lux<Webshop> findAndFilter( String condicao);
//    Flux<Webshop> findAndFilterEmails(String valor,String condicao) ;
//    Flux<Webshop> findbyFilterAndSort(String colunaSearch, String coluna, String valor, String condicao);
//    Flux<Webshop> findbyFilterAndSort(String colunaSearch, String coluna, int valor, String condicao);
//    Flux<Webshop> findbyFilterAndSortEmails(String colunaSearch,String condicao, String valor);