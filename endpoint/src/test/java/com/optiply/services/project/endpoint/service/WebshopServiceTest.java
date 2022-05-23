package com.optiply.services.project.endpoint.service;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.Webshop;
import com.optiply.services.project.endpoint.dto.WebshopDto;
import com.optiply.services.project.endpoint.mapper.WebshopMapper;
import com.optiply.services.project.endpoint.repositories.JooqWebshopRepository;
import jakarta.xml.bind.SchemaOutputResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class WebshopServiceTest {

    @Mock
    JooqWebshopRepository webshopRepository;

    @Mock
    WebshopMapper webshopMapper;

    @InjectMocks
    WebshopService webshopService;

    Webshop webshop = new Webshop();

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        webshop.setId(1);
        webshop.setHandle("test");
        webshop.setEmails(new String[]{"test@gmail.com"});
        webshop.setUrl("https://test.com");
        webshop.setServiceLevelCatA(30);
        webshop.setServiceLevelCatB(30);
        webshop.setServiceLevelCatC(40);
        webshop.setInterestRate(20);

    }

    @Test
    void addWebshopsTest(){
        Mono<Webshop> w = Mono.just(webshop);

        Mockito.when(webshopMapper.updateWebshopFromDto(any(),any())).thenReturn(webshop);
        Mockito.when(webshopRepository.save(any())).thenReturn(w);

        Mono<Webshop> webshopAdd = webshopService.addWebshop(new WebshopDto());

        StepVerifier.create(webshopAdd)
                .consumeNextWith(wshop -> {
                    assertNotNull(wshop);
                    assertEquals("test",wshop.getHandle());
                }).verifyComplete();

        Mockito.verify(webshopRepository).save(any());
        Mockito.verify(webshopMapper).updateWebshopFromDto(any(),any());

    }

    @Test
    void updateWebshopsTest(){
        Mono<Webshop> w = Mono.just(webshop);

        Mockito.when(webshopMapper.updateWebshopFromDto(any(),any())).thenReturn(webshop);
        Mockito.when(webshopRepository.update(any())).thenReturn(w);
        Mockito.when(webshopRepository.findById(any(Integer.class))).thenReturn(w);

        Mono<Webshop> webshopUpdate = webshopService.updateWebshop(1,new WebshopDto());

        StepVerifier.create(webshopUpdate)
                .consumeNextWith(wshop -> {
                    assertNotNull(wshop);
                    assertEquals("test",wshop.getHandle());
                }).verifyComplete();

        Mockito.verify(webshopRepository).update(any());
        Mockito.verify(webshopRepository).findById(any(Integer.class));
        Mockito.verify(webshopMapper).updateWebshopFromDto(any(),any());
    }

    @Test
    void deleteWebshopsTest(){
        Mono<Boolean> w = Mono.just(true);

        Mockito.when(webshopRepository.delete(any(Integer.class))).thenReturn(w);

        Mono<Boolean> webshopById = webshopService.deleteWebshop(1);

        StepVerifier.create(webshopById)
                .consumeNextWith(wshop -> {
                    assertNotNull(wshop);
                    assertEquals(true,wshop);
                }).verifyComplete();

        Mockito.verify(webshopRepository).delete(any(Integer.class));
    }

    @Test
    void getWebshopByIdTest(){
        Mono<Webshop> w = Mono.just(webshop);
        Mockito.when(webshopRepository.findById(any(Integer.class))).thenReturn(w);

        Mono<Webshop> webshopById = webshopService.getWebshopById(1);

        StepVerifier.create(webshopById)
                .consumeNextWith(wshop -> {
                    assertNotNull(wshop);
                    assertEquals("test",wshop.getHandle());
                }).verifyComplete();

        Mockito.verify(webshopRepository).findById(any(Integer.class));
    }


    @Test
    void getAllWebshopTest(){
        Flux<Webshop> w = Flux.just(webshop);
        Mockito.when(webshopRepository.findAll()).thenReturn(w);

        Mono<List<Webshop>> webshopsAll = webshopService.getAllWebshop();

        StepVerifier.create(webshopsAll)
                .consumeNextWith(list -> {
                    assertNotNull(list);
                    assertEquals("test",list.get(0).getHandle());
                    assertEquals(1,list.size());
                }).verifyComplete();

        Mockito.verify(webshopRepository).findAll();
    }

    @Test
    void getAllWebshopsSortAndFilterEmptySortTest(){
        Flux<Webshop> w = Flux.just(webshop);


        Mockito.when(webshopRepository.findAndFilter(any())).thenReturn(w);


        Flux<Webshop> webshopsAll = webshopService.getAllWebshopsSortAndFilter(null,new ArrayList<>());

        StepVerifier.create(webshopsAll)
                .consumeNextWith(webshop -> {
                    assertNotNull(webshop);
                    assertEquals("test",webshop.getHandle());
                }).verifyComplete();

        Mockito.verify(webshopRepository).findAndFilter(any());
    }

    @Test
    void getAllWebshopsSortAndFilterTest(){
        Flux<Webshop> w = Flux.just(webshop);
        Mockito.when(webshopRepository.findbyFilterAndSort(any(),any())).thenReturn(w);

        Flux<Webshop> webshopsAll = webshopService.getAllWebshopsSortAndFilter("sort",new ArrayList<>());

        StepVerifier.create(webshopsAll)
                .consumeNextWith(webshop -> {
                    assertNotNull(webshop);
                    assertEquals("test",webshop.getHandle());
                }).verifyComplete();

        Mockito.verify(webshopRepository).findbyFilterAndSort(any(),any());

    }

    @Test
    void getAllWebshopsSortAndFilterEmptyFilterTest(){
        Flux<Webshop> w = Flux.just(webshop);
        Mockito.when(webshopRepository.findAndSort(any())).thenReturn(w);
        String sort= "interest_rate";
        Flux<Webshop> webshopsAll = webshopService.getAllWebshopsSortAndFilter(sort,null);

        StepVerifier.create(webshopsAll)
                .consumeNextWith(webshop -> {
                    assertNotNull(webshop);
                    assertEquals("test",webshop.getHandle());
                }).verifyComplete();

        Mockito.verify(webshopRepository).findAndSort(any());

    }



    @Test
    void checkParamTest(){
        Boolean result = webshopService.checkParam(webshop);
        assertEquals(true, result);
    }

    @Test
    void sumTest(){
        int sum = webshopService.sum(new int[]{webshop.getServiceLevelCatA(),webshop.getServiceLevelCatB(),webshop.getServiceLevelCatC()});
        assertEquals(100,sum);
    }

    @Test
    void checkColumnTest(){
        String coluna = "interest_rate";
        int valor = 20;
        Boolean result = webshopService.checkColumn(coluna,valor);

        assertEquals(true,result);
    }

    @Test
    void checkFilterParams(){
        String filter = "handle%test";
        String[] valuesArray = webshopService.checkFilterParams(filter);
        assertArrayEquals(new String[]{"handle","ilike","test"},valuesArray);

        filter = "handle:test";
        valuesArray = webshopService.checkFilterParams(filter);
        assertArrayEquals(new String[]{"handle","=","test"}, valuesArray);

        filter = "interest_rate>0";
        valuesArray = webshopService.checkFilterParams(filter);
        assertArrayEquals(new String[]{"interest_rate",">","0"}, valuesArray);
    }

    @Test
    void addCondtionsTest(){
        List<String> filters = Arrays.asList("handle%test", "handle:test", "interest_rate>0","service_level_cat_a<40","emails%google@gmail.com","emails:google@gmail.com");

        assertEquals("handle ilike 'test'",webshopService.addConditions(filters).get(0).toString());
        assertEquals("handle = 'test'",webshopService.addConditions(filters).get(1).toString());
        assertEquals("interest_rate > 0",webshopService.addConditions(filters).get(2).toString());
        assertEquals("service_level_cat_a < 40",webshopService.addConditions(filters).get(3).toString());
        assertEquals("(google@gmail.com ilike any(emails))",webshopService.addConditions(filters).get(4).toString());
        assertEquals("(google@gmail.com = any(emails))",webshopService.addConditions(filters).get(5).toString());


    }


}
