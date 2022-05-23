package com.optiply.services.project.endpoint.service;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.Webshop;
import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.WebshopConfg;
import com.optiply.services.project.endpoint.dto.WebshopConfgDto;
import com.optiply.services.project.endpoint.dto.WebshopDto;
import com.optiply.services.project.endpoint.mapper.WebshopConfgMapper;
import com.optiply.services.project.endpoint.repositories.JooqWebshopConfgRepository;
import com.optiply.services.project.endpoint.repositories.JooqWebshopRepository;
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

public class WebshopConfgServiceTest {
    @Mock
    JooqWebshopConfgRepository webshopConfgRepository;

    @Mock
    JooqWebshopRepository webshopRepository;

    @Mock
    WebshopConfgMapper webshopConfgMapper;

    @InjectMocks
    WebshopConfgService webshopConfgService;

    WebshopConfg webshopConfg = new WebshopConfg();

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        webshopConfg.setFkWebshop(1);
        webshopConfg.setMultiSupplier(true);
        webshopConfg.setRunJobs(false);
        webshopConfg.setCurrency("EUR");
    }

    @Test
    void addWebshopsConfgTest(){
        Mono<WebshopConfg> w = Mono.just(webshopConfg);

        Mockito.when(webshopConfgMapper.updateWebshopConfgFromDto(any(),any())).thenReturn(webshopConfg);
        Mockito.when(webshopConfgRepository.save(any(),any(Integer.class))).thenReturn(w);
        Mockito.when(webshopRepository.findIdbyHandle(any())).thenReturn(Mono.just(1));

        Mono<WebshopConfg> webshopConfgAdd = webshopConfgService.addWebshopConfg(new WebshopConfgDto(),"");

        StepVerifier.create(webshopConfgAdd)
                .consumeNextWith(wshopConfg -> {
                    assertNotNull(wshopConfg);
                    assertEquals(true,wshopConfg.getMultiSupplier());
                }).verifyComplete();

        Mockito.verify(webshopConfgRepository).save(any(),any(Integer.class));
        Mockito.verify(webshopConfgMapper).updateWebshopConfgFromDto(any(),any());
    }

    @Test
    void updateWebshopsCnfgTest(){
        Mono<WebshopConfg> w = Mono.just(webshopConfg);

        Mockito.when(webshopConfgMapper.updateWebshopConfgFromDto(any(),any())).thenReturn(webshopConfg);
        Mockito.when(webshopConfgRepository.update(any())).thenReturn(w);
        Mockito.when(webshopRepository.findIdbyHandle(any())).thenReturn(Mono.just(1));
        Mockito.when(webshopConfgRepository.findById(any(Integer.class))).thenReturn(w);

        Mono<WebshopConfg> webshopUpdate = webshopConfgService.updateWebshopConfg("",new WebshopConfgDto());

        StepVerifier.create(webshopUpdate)
                .consumeNextWith(wshopConfg -> {
                    assertNotNull(wshopConfg);
                    assertEquals(true, wshopConfg.getMultiSupplier());
                }).verifyComplete();

        Mockito.verify(webshopConfgMapper).updateWebshopConfgFromDto(any(),any());
        Mockito.verify(webshopConfgRepository).update(any());
        Mockito.verify(webshopRepository).findIdbyHandle(any());
        Mockito.verify(webshopConfgRepository).findById(any(Integer.class));

    }

    @Test
    void getWebshopCnfgByHandleTest(){
        Mono<WebshopConfg> w = Mono.just(webshopConfg);

        Mockito.when(webshopRepository.findIdbyHandle(any())).thenReturn(Mono.just(1));
        Mockito.when(webshopConfgRepository.findById(any(Integer.class))).thenReturn(w);

        Mono<WebshopConfg> webshopByHandle = webshopConfgService.getWebshopConfgByHandle("");

        StepVerifier.create(webshopByHandle)
                .consumeNextWith(wshopConfg -> {
                    assertNotNull(wshopConfg);
                    assertEquals(true,wshopConfg.getMultiSupplier());
                }).verifyComplete();

        Mockito.verify(webshopRepository).findIdbyHandle(any());
        Mockito.verify(webshopConfgRepository).findById(any(Integer.class));
    }
}
