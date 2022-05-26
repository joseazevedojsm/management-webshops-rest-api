package com.optiply.services.project.endpoint.controller;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.Webshop;
import com.optiply.services.project.endpoint.dto.WebshopDto;
import com.optiply.services.project.endpoint.service.WebshopService;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@MicronautTest
class WebshopControllerTest {

    @Inject
    WebshopService webshopService;

    @Inject
    @Client("/webshops")
    HttpClient client;

    Webshop webshop = new Webshop();
    WebshopDto webshopDto = new WebshopDto();


    @BeforeEach
    void setUp() {
        // MockitoAnnotations.initMocks(this);

        webshop.setId(1);
        webshop.setHandle("test");
        webshop.setEmails(new String[]{"test@gmail.com"});
        webshop.setUrl("https://test.com");
        webshop.setServiceLevelCatA(30);
        webshop.setServiceLevelCatB(30);
        webshop.setServiceLevelCatC(40);
        webshop.setInterestRate(20);

        webshopDto.setHandle("test");
        webshopDto.setEmails(new String[]{"test@gmail.com"});
        webshopDto.setUrl("https://test.com");
        webshopDto.setServiceLevelCatA(30);
        webshopDto.setServiceLevelCatB(30);
        webshopDto.setServiceLevelCatC(40);
        webshopDto.setInterestRate(20);
    }

    @Test
    void webshopsEndpointReturnsListOfWebshops() {

        List<Webshop> list = new ArrayList<>();
        list.add(webshop);

        Mockito.when(webshopService.getAllWebshop()).thenReturn(Mono.just(list));

        var usersResponse = client.toBlocking().exchange(HttpRequest.GET("/all"), Argument.of(List.class, Webshop.class));

        assertEquals(HttpStatus.OK, usersResponse.getStatus());
        assertEquals(1, usersResponse.getBody().get().size());
        assertEquals("test", ((Webshop) usersResponse.getBody().get().get(0)).getHandle());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, usersResponse.getContentType().get());
        Mockito.verify(webshopService).getAllWebshop();
    }

    @Test
    void webshopsEndpointReturnsListOfWebshopsById() {
        Mono<Webshop> webshopFlux = Mono.just(webshop);

        Mockito.when(webshopService.getWebshopById(any(Integer.class))).thenReturn(webshopFlux);

        var usersResponse = client.toBlocking().exchange(HttpRequest.GET("/1"), Webshop.class);

        assertEquals(HttpStatus.OK, usersResponse.getStatus());
        assertNotNull(usersResponse.getBody().get());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, usersResponse.getContentType().get());
        assertEquals(1, usersResponse.getBody().get().getId());
        Mockito.verify(webshopService).getWebshopById(any(Integer.class));
    }


    @Test
    void webshopsEndpointCreateWebshop() {
        Mono<Webshop> webshopFlux = Mono.just(webshop);

        Mockito.when(webshopService.addWebshop(any())).thenReturn(webshopFlux);

        var usersResponse = client.toBlocking().exchange(HttpRequest.POST("/", webshopDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON), Webshop.class);

        assertEquals(HttpStatus.CREATED, usersResponse.getStatus());
        assertEquals("test", usersResponse.getBody().get().getHandle());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, usersResponse.getContentType().get());
        Mockito.verify(webshopService).addWebshop(any());

    }


    @Test
    void webshopsEndpointUpdateWebshop() {

        Mono<Webshop> webshopFlux = Mono.just(webshop);

        Mockito.when(webshopService.updateWebshop(any(Integer.class), any())).thenReturn(webshopFlux);


        var usersResponse = client.toBlocking().exchange(HttpRequest.PUT("/1", webshopDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON), Webshop.class);

        assertEquals(HttpStatus.OK, usersResponse.getStatus());
        assertNotNull(usersResponse.getBody().get());
        assertEquals("test", usersResponse.getBody().get().getHandle());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, usersResponse.getContentType().get());
        Mockito.verify(webshopService).updateWebshop(any(Integer.class), any());

    }

    @Test
    void webshopsEndpointDeleteWebshops() {
        Mono<Boolean> webshopFlux = Mono.just(true);

        Mockito.when(webshopService.deleteWebshop(any(Integer.class))).thenReturn(webshopFlux);

        var usersResponse = client.toBlocking().exchange(HttpRequest.DELETE("/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON), Boolean.class);

        assertEquals(HttpStatus.NO_CONTENT, usersResponse.getStatus());

        Mockito.verify(webshopService).deleteWebshop(any(Integer.class));
    }

    @Test
    void webshopsEndpointGetAllSortOrAndFilterTest(){

        Mockito.when(webshopService.getAllWebshopsSortAndFilter(any(),any())).thenReturn(Flux.just(webshop));

        var usersResponse = client.toBlocking().exchange(HttpRequest.GET("/search?sortBy=interest_rate&filter=handle%25test")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON), Argument.of(List.class, Webshop.class));

        assertEquals(HttpStatus.OK, usersResponse.getStatus());
        assertEquals(1, usersResponse.getBody().get().size());
        assertEquals("test", ((Webshop) usersResponse.getBody().get().get(0)).getHandle());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, usersResponse.getContentType().get());

        Mockito.verify(webshopService).getAllWebshopsSortAndFilter(any(),any());
    }

    @MockBean(WebshopService.class)
    public WebshopService mockUserRepository() {
        return Mockito.mock(WebshopService.class);
    }
}

// Old implementation
//    @Mock
//    WebshopService webshopService;

//    @InjectMocks
//    WebshopController  webshopController;

//    @Test
//    void getAllTest(){
//        List<Webshop> list = new ArrayList<>();
//        list.add(webshop);
//        Mockito.when(webshopService.getAllWebshop()).thenReturn(Mono.just(list));
//
//        Mono<MutableHttpResponse<List<Webshop>>> webshopGet = webshopController.getAll();
//
//        StepVerifier.create(webshopGet)
//                .consumeNextWith(mutableHttpResponse -> {
//                    List<Webshop> webshopList  = (List<Webshop>) mutableHttpResponse.body();
//                    assertNotNull(webshopList);
//                    assertEquals(1,webshopList.size());
//                    assertEquals("test",webshopList.get(0).getHandle());
//                }).verifyComplete();
//
//        Mockito.verify(webshopService).getAllWebshop();
//
//    }
//
//    @Test
//    void getByIdTest(){
//        Mockito.when(webshopService.getWebshopById(any(Integer.class))).thenReturn(Mono.just(webshop));
//
//        Mono<MutableHttpResponse<Webshop>> webshopById = webshopController.getById(1);
//
//        StepVerifier.create(webshopById)
//                .consumeNextWith(mutableHttpResponse -> {
//                    Webshop webshop  = (Webshop) mutableHttpResponse.body();
//                    assertNotNull(webshop);
//                    assertEquals("test",webshop.getHandle());
//                }).verifyComplete();
//
//        Mockito.verify(webshopService).getWebshopById(any(Integer.class));
//    }
//
//    @Test
//    void insertTest(){
//
//        Mockito.when(webshopService.addWebshop(any())).thenReturn(Mono.just(webshop));
//
//        Mono<MutableHttpResponse<Webshop>> webshopSave = webshopController.insert(new WebshopDto());
//
//        StepVerifier.create(webshopSave)
//                .consumeNextWith(mutableHttpResponse -> {
//                    Webshop webshop  = (Webshop) mutableHttpResponse.body();
//                    assertNotNull(webshop);
//                    assertEquals("test",webshop.getHandle());
//                }).verifyComplete();
//
//        Mockito.verify(webshopService).addWebshop(any());
//    }
//
//    @Test
//    void insertMultipleTest(){
//
//        Mockito.when(webshopService.addWebshops(any())).thenReturn(Flux.just(webshop));
//
//        Mono<MutableHttpResponse<List<Webshop>>> webshopSaveMany = webshopController.insertMultiple(new ArrayList<WebshopDto>());
//
//        StepVerifier.create(webshopSaveMany)
//                .consumeNextWith(mutableHttpResponse -> {
//                    List<Webshop> webshopList  = (List<Webshop>) mutableHttpResponse.body();
//                    assertNotNull(webshopList);
//                    assertEquals(1,webshopList.size());
//                    assertEquals("test",webshopList.get(0).getHandle());
//                }).verifyComplete();
//
//        Mockito.verify(webshopService).addWebshops(any());
//    }
//
//    @Test
//    void updateTest(){
//
//        Mockito.when(webshopService.updateWebshop(any(Integer.class),any())).thenReturn(Mono.just(webshop));
//
//        Mono<MutableHttpResponse<Webshop>> webshopUpdate = webshopController.update(1,new WebshopDto());
//
//        StepVerifier.create(webshopUpdate)
//                .consumeNextWith(mutableHttpResponse -> {
//                    Webshop webshop  = (Webshop) mutableHttpResponse.body();
//                    assertNotNull(webshop);
//                    assertEquals("test",webshop.getHandle());
//                }).verifyComplete();
//
//        Mockito.verify(webshopService).updateWebshop(any(Integer.class),any());
//    }
//
//    @Test
//    void deleteTest(){
//
//        Mockito.when(webshopService.deleteWebshop(any(Integer.class))).thenReturn(Mono.just(true));
//
//        Mono<MutableHttpResponse<Boolean>> webshopUpdate = webshopController.delete(1);
//
//        StepVerifier.create(webshopUpdate)
//                .consumeNextWith(mutableHttpResponse -> {
//                    Boolean response  = (Boolean) mutableHttpResponse.body();
//                    assertNotNull(response);
//                    assertEquals(true,response);
//                }).verifyComplete();
//
//        Mockito.verify(webshopService).deleteWebshop(any(Integer.class));
//    }
//
//    @Test
//    void getAllSortOrAndFilterTest(){
//        Mockito.when(webshopService.getAllWebshopsSortAndFilter(any(),any())).thenReturn(Flux.just(webshop));
//
//        Mono<MutableHttpResponse<List<Webshop>>> webshopUpdate = webshopController.getAllSortOrAndFilter("", new ArrayList<>());
//
//        StepVerifier.create(webshopUpdate)
//                .consumeNextWith(mutableHttpResponse -> {
//                    List<Webshop> webshopList  = (List<Webshop>) mutableHttpResponse.body();
//                    assertNotNull(webshopList);
//                    assertEquals("test",webshopList.get(0).getHandle());
//                }).verifyComplete();
//
//        Mockito.verify(webshopService).getAllWebshopsSortAndFilter(any(),any());
//    }
//





























