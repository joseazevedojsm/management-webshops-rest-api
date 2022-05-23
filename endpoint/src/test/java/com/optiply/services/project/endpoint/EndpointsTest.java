package com.optiply.services.project.endpoint;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.WebshopConfg;
import com.optiply.services.project.endpoint.controller.WebshopConfgController;
import com.optiply.services.project.endpoint.controller.WebshopController;
import com.optiply.services.project.endpoint.service.WebshopService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import java.net.http.HttpClient;

@MicronautTest
public class EndpointsTest {
    @Inject
    @Client("/webshops")
    HttpClient client;

    @Inject
    WebshopController webshopController;

    @Inject
    WebshopConfgController webshopConfgController;

//    @Test
//    void getAll(){
//       // Mockito.when(webshopController.getAll()).thenReturn(Mono.just(HttpResponse.ok()));
//    }

    @MockBean(WebshopController.class)
    public WebshopController mockWebshopController() {
        return Mockito.mock(WebshopController.class);
    }

    @MockBean(WebshopConfgController.class)
    public WebshopConfgController mockWebshopConfgController() {
        return Mockito.mock(WebshopConfgController.class);
    }
}
