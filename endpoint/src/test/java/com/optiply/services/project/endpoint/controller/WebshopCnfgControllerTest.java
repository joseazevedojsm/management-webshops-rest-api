package com.optiply.services.project.endpoint.controller;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.WebshopConfg;
import com.optiply.services.project.endpoint.dto.WebshopConfgDto;
import com.optiply.services.project.endpoint.service.WebshopConfgService;
import io.micronaut.http.MutableHttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

public class WebshopCnfgControllerTest {

    @Mock
    WebshopConfgService webshopConfgService;

    @InjectMocks
    WebshopConfgController  webshopConfgController;

    WebshopConfg webshopConfg = new WebshopConfg();

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);

        webshopConfg.setFkWebshop(1);
        webshopConfg.setMultiSupplier(true);
        webshopConfg.setRunJobs(false);
        webshopConfg.setCurrency("EUR");
    }

    @Test
    void getConfgByHandle(){

        Mockito.when(webshopConfgService.getWebshopConfgByHandle(any())).thenReturn(Mono.just(webshopConfg));

        Mono<MutableHttpResponse<WebshopConfg>> webshopConfgGet = webshopConfgController.getByHandle("");

        StepVerifier.create(webshopConfgGet)
                .consumeNextWith(mutableHttpResponse -> {
                    WebshopConfg webshopConfg  = mutableHttpResponse.body();
                    assertNotNull(webshopConfg);
                    assertEquals(true,webshopConfg.getMultiSupplier());
                }).verifyComplete();

        Mockito.verify(webshopConfgService).getWebshopConfgByHandle(any());
    }

    @Test
    void insertTest(){

        Mockito.when(webshopConfgService.addWebshopConfg(any(),any())).thenReturn(Mono.just(webshopConfg));

        Mono<MutableHttpResponse<WebshopConfg>> webshopConfgInsert = webshopConfgController.insert("",new WebshopConfgDto());

        StepVerifier.create(webshopConfgInsert)
                .consumeNextWith(mutableHttpResponse -> {
                    WebshopConfg webshopConfg  = mutableHttpResponse.body();
                    assertNotNull(webshopConfg);
                    assertEquals(true,webshopConfg.getMultiSupplier());
                }).verifyComplete();
    }
    @Test
    void updateTest(){

        Mockito.when(webshopConfgService.updateWebshopConfg(any(),any())).thenReturn(Mono.just(webshopConfg));

        Mono<MutableHttpResponse<WebshopConfg>> webshopConfgUpdate = webshopConfgController.update("",new WebshopConfgDto());

        StepVerifier.create(webshopConfgUpdate)
                .consumeNextWith(mutableHttpResponse -> {
                    WebshopConfg webshopConfg  = mutableHttpResponse.body();
                    assertNotNull(webshopConfg);
                    assertEquals(true,webshopConfg.getMultiSupplier());
                }).verifyComplete();
    }

}
