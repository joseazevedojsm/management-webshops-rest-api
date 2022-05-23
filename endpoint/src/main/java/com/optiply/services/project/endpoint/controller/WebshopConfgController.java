package com.optiply.services.project.endpoint.controller;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.WebshopConfg;
import com.optiply.services.project.endpoint.dto.WebshopConfgDto;
import com.optiply.services.project.endpoint.service.WebshopConfgService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

@Controller("/webshops/config")
public class WebshopConfgController {
    @Inject
    WebshopConfgService webshopConfgService;

    @Operation(summary = "Returns the configuration of a webshop by handle")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Tag(name = "webshop configuration")
    @Get("/{handle}")
    public Mono<MutableHttpResponse<WebshopConfg>> getByHandle(@Parameter(required = true,description ="Webshop Configuration Handle") String handle){
        Mono<WebshopConfg> config = webshopConfgService.getWebshopConfgByHandle(handle);

        return Mono.from(config)
                .map(response -> HttpResponse.ok(response))
                .switchIfEmpty(Mono.just(HttpResponse.notFound()))
                .onErrorReturn(HttpResponse.serverError());
    }

    @Operation(summary = "Creates a configuration for a webshop")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Tag(name = "webshop configuration")
    @Post(value = "/{handle}",consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Mono<MutableHttpResponse<WebshopConfg>> insert(@PathVariable @Parameter(required = true,description ="Webshop Configuration Handle") String handle,@Body @Parameter(required = true,description ="Webshop Configuration Body") WebshopConfgDto webshopConfgDto){
        Mono<WebshopConfg> confg = webshopConfgService.addWebshopConfg(webshopConfgDto,handle);

        return Mono.from(confg)
                .map(response -> HttpResponse.created(response))
                .switchIfEmpty(Mono.just(HttpResponse.serverError()));
    }

    @Operation(summary = "Updates an existing configuration of a webshop")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Tag(name = "webshop configuration")
    @Put(value = "/{handle}",consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Mono<MutableHttpResponse<WebshopConfg>> update(@PathVariable @Parameter(required = true,description ="Webshop Configuration Handle") String handle, @Body @Parameter(required = true,description ="Webshop Configuration Body") WebshopConfgDto webshop1){
        Mono<WebshopConfg> webshop = webshopConfgService.updateWebshopConfg(handle,webshop1);

        return Mono.from(webshop)
                .map(response -> HttpResponse.ok(response))
                .switchIfEmpty(Mono.just(HttpResponse.notFound()))
                .onErrorReturn(HttpResponse.serverError());
    }
}
