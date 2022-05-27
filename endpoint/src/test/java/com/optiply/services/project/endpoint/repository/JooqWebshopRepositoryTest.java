package com.optiply.services.project.endpoint.repository;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.Webshop;
import com.optiply.services.project.endpoint.repositories.JooqWebshopRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@MicronautTest
public class JooqWebshopRepositoryTest {

    @Inject
    JooqWebshopRepository webshopRepository;

    Webshop webshop = new Webshop();

    @BeforeAll
    void setUp(){

        webshop.setId(1);
        webshop.setHandle("test");
        webshop.setEmails(new String[]{"test@gmail.com"});
        webshop.setUrl("https://test.com");
        webshop.setServiceLevelCatA(30);
        webshop.setServiceLevelCatB(30);
        webshop.setServiceLevelCatC(40);
        webshop.setInterestRate(20);

        webshopRepository.save(webshop);

    }

    @Test
    void webshopRepositoryGetAllTest(){

    }
}
