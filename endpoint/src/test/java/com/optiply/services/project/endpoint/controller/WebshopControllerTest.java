package com.optiply.services.project.endpoint.controller;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.Webshop;
import com.optiply.services.project.endpoint.dto.WebshopDto;
import com.optiply.services.project.endpoint.service.WebshopService;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
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

    @Mock
    WebshopService webshopService;

    @InjectMocks
    WebshopController  webshopController;

    Webshop webshop = new Webshop();

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);

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
    void getAllTest(){
        List<Webshop> list = new ArrayList<>();
        list.add(webshop);
        Mockito.when(webshopService.getAllWebshop()).thenReturn(Mono.just(list));

        Mono<MutableHttpResponse<List<Webshop>>> webshopGet = webshopController.getAll();

        StepVerifier.create(webshopGet)
                .consumeNextWith(mutableHttpResponse -> {
                    List<Webshop> webshopList  = (List<Webshop>) mutableHttpResponse.body();
                    assertNotNull(webshopList);
                    assertEquals(1,webshopList.size());
                    assertEquals("test",webshopList.get(0).getHandle());
                }).verifyComplete();

        Mockito.verify(webshopService).getAllWebshop();

    }

    @Test
    void getByIdTest(){
        Mockito.when(webshopService.getWebshopById(any(Integer.class))).thenReturn(Mono.just(webshop));

        Mono<MutableHttpResponse<Webshop>> webshopById = webshopController.getById(1);

        StepVerifier.create(webshopById)
                .consumeNextWith(mutableHttpResponse -> {
                    Webshop webshop  = (Webshop) mutableHttpResponse.body();
                    assertNotNull(webshop);
                    assertEquals("test",webshop.getHandle());
                }).verifyComplete();

        Mockito.verify(webshopService).getWebshopById(any(Integer.class));
    }

    @Test
    void insertTest(){

        Mockito.when(webshopService.addWebshop(any())).thenReturn(Mono.just(webshop));

        Mono<MutableHttpResponse<Webshop>> webshopSave = webshopController.insert(new WebshopDto());

        StepVerifier.create(webshopSave)
                .consumeNextWith(mutableHttpResponse -> {
                    Webshop webshop  = (Webshop) mutableHttpResponse.body();
                    assertNotNull(webshop);
                    assertEquals("test",webshop.getHandle());
                }).verifyComplete();

        Mockito.verify(webshopService).addWebshop(any());
    }

    @Test
    void insertMultipleTest(){

        Mockito.when(webshopService.addWebshops(any())).thenReturn(Flux.just(webshop));

        Mono<MutableHttpResponse<List<Webshop>>> webshopSaveMany = webshopController.insertMultiple(new ArrayList<WebshopDto>());

        StepVerifier.create(webshopSaveMany)
                .consumeNextWith(mutableHttpResponse -> {
                    List<Webshop> webshopList  = (List<Webshop>) mutableHttpResponse.body();
                    assertNotNull(webshopList);
                    assertEquals(1,webshopList.size());
                    assertEquals("test",webshopList.get(0).getHandle());
                }).verifyComplete();

        Mockito.verify(webshopService).addWebshops(any());
    }

    @Test
    void updateTest(){

        Mockito.when(webshopService.updateWebshop(any(Integer.class),any())).thenReturn(Mono.just(webshop));

        Mono<MutableHttpResponse<Webshop>> webshopUpdate = webshopController.update(1,new WebshopDto());

        StepVerifier.create(webshopUpdate)
                .consumeNextWith(mutableHttpResponse -> {
                    Webshop webshop  = (Webshop) mutableHttpResponse.body();
                    assertNotNull(webshop);
                    assertEquals("test",webshop.getHandle());
                }).verifyComplete();

        Mockito.verify(webshopService).updateWebshop(any(Integer.class),any());
    }

    @Test
    void deleteTest(){

        Mockito.when(webshopService.deleteWebshop(any(Integer.class))).thenReturn(Mono.just(true));

        Mono<MutableHttpResponse<Boolean>> webshopUpdate = webshopController.delete(1);

        StepVerifier.create(webshopUpdate)
                .consumeNextWith(mutableHttpResponse -> {
                    Boolean response  = (Boolean) mutableHttpResponse.body();
                    assertNotNull(response);
                    assertEquals(true,response);
                }).verifyComplete();

        Mockito.verify(webshopService).deleteWebshop(any(Integer.class));
    }

    @Test
    void getAllSortOrAndFilterTest(){
        Mockito.when(webshopService.getAllWebshopsSortAndFilter(any(),any())).thenReturn(Flux.just(webshop));

        Mono<MutableHttpResponse<List<Webshop>>> webshopUpdate = webshopController.getAllSortOrAndFilter("", new ArrayList<>());

        StepVerifier.create(webshopUpdate)
                .consumeNextWith(mutableHttpResponse -> {
                    List<Webshop> webshopList  = (List<Webshop>) mutableHttpResponse.body();
                    assertNotNull(webshopList);
                    assertEquals("test",webshopList.get(0).getHandle());
                }).verifyComplete();

        Mockito.verify(webshopService).getAllWebshopsSortAndFilter(any(),any());
    }

































//
//    @Inject
//    @Client("/webshops")
//    HttpClient client;
//
//    @Inject
//    WebshopService service;
//
//    @Test
//    void webshopsEndpointReturnsListOfWebshops(){
//        Webshop webshop = new Webshop();
//
//        List<Webshop> list = new ArrayList<>();
//        list.add(webshop);
//
//        Mono<List<Webshop>> webshopFlux = Mono.just(list);
//
//        Mockito.when(service.getAllWebshop()).thenReturn(webshopFlux);
//
//
//        var usersResponse = client.toBlocking().exchange("/all", JsonNode.class);
//        assertEquals(HttpStatus.OK, usersResponse.getStatus());
//        assertEquals(1, usersResponse.getBody().get().size());
//        assertEquals( MediaType.APPLICATION_JSON_TYPE, usersResponse.getContentType().get() );
//        Mockito.verify(service).getAllWebshop();
//    }
//
//    @Test
//    void webshopsEndpointReturnsListOfWebshopsById(){
//        Webshop webshop = new Webshop();
//        webshop.setId(10);
//
//        Mono<Webshop> webshopFlux = Mono.just(webshop);
//
//        Mockito.when(service.getWebshopById(webshop.getId())).thenReturn(webshopFlux);
//
//        var usersResponse = client.toBlocking().exchange("/10", JsonNode.class);
//        assertEquals(HttpStatus.OK, usersResponse.getStatus());
//        assertEquals(1, usersResponse.getBody().get().size());
//        assertEquals( MediaType.APPLICATION_JSON_TYPE, usersResponse.getContentType().get());
//        assertEquals(10,usersResponse.getBody().get().get("id").getIntValue());
//        Mockito.verify(service).getWebshopById(webshop.getId());
//    }
//
////    @Test
////    void webshopsEndpointCreateWebshop(){
//////
//////        Webshop webshop = new Webshop();
//////        webshop.setId(1);
//////        webshop.setHandle("test");
//////        webshop.setEmails("test@gmail.com".split(" "));
//////        webshop.setUrl("https://test.com");
//////        webshop.setServiceLevelCatA(10);
//////        webshop.setServiceLevelCatB(10);
//////        webshop.setServiceLevelCatC(10);
//////        webshop.setInterestRate(20);
//////
//////        Mono<Webshop> webshopFlux = Mono.just(webshop);
//////
//////        Mockito.when(service.addWebshop(webshop)).thenReturn(webshopFlux);
//////
//////        var usersResponse = client.toBlocking().exchange(POST("/", webshop)
//////                                    .contentType(MediaType.APPLICATION_JSON)
//////                                    .accept(MediaType.APPLICATION_JSON), JsonNode.class );
//////
//////        assertEquals(HttpStatus.CREATED, usersResponse.getStatus());
//////        assertEquals(8, usersResponse.getBody().get().size());
////////        assertEquals( MediaType.APPLICATION_JSON_TYPE, usersResponse.getContentType().get());
////////        assertEquals(10,usersResponse.getBody().get().get("id").getIntValue());
//////        Mockito.verify(service).addWebshop(webshop);
////
////    }
//
////    @Test
////    void webshopsEndpointUpdateWebshop(){
////
////        int id = 1;
////        Webshop webshop = new Webshop();
////        webshop.setId(1);
////        webshop.setHandle("test");
////        webshop.setEmails("test@gmail.com".split(" "));
////        webshop.setUrl("https://test.com");
////        webshop.setServiceLevelCatA(10);
////        webshop.setServiceLevelCatB(10);
////        webshop.setServiceLevelCatC(10);
////        webshop.setInterestRate(20);
////
////        WebshopDto wb = new WebshopDto("test","test@gmail.com","test@gmail.com".split(" "),10,10,10,20);
////
////        Mono<Webshop> webshopFlux = Mono.just(webshop);
////
////        Mockito.when(service.updateWebshop(id,wb)).thenReturn(webshopFlux);
////
////
////        var usersResponse = client.toBlocking().exchange(PUT("/1",wb)
////                .contentType(MediaType.APPLICATION_JSON)
////                .accept(MediaType.APPLICATION_JSON), JsonNode.class );
////
////        assertEquals(HttpStatus.OK, usersResponse.getStatus());
////        assertEquals(webshop.getId(),usersResponse.getBody().get().get("id"));
////        assertEquals(8, usersResponse.getBody().get().size());
////        assertEquals( MediaType.APPLICATION_JSON_TYPE, usersResponse.getContentType().get());
////        Mockito.verify(service).updateWebshop(id,wb);
////
////    }
//
//    @Test
//    void webshopsEndpointDeleteWebshops(){
//        Webshop webshop = new Webshop();
//        webshop.setId(10);
//
//        Mono<Boolean> webshopFlux = Mono.just(true);
//
//        Mockito.when(service.deleteWebshop(webshop.getId())).thenReturn(webshopFlux);
//
//        var usersResponse = client.toBlocking().exchange(DELETE("/10",webshop.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON), JsonNode.class );
//        assertEquals(HttpStatus.NO_CONTENT, usersResponse.getStatus());
//        Mockito.verify(service).deleteWebshop(webshop.getId());
//    }
//
//    @MockBean(WebshopService.class)
//    public WebshopService mockUserRepository() {
//        return Mockito.mock(WebshopService.class);
//    }
}
