package com.optiply.services.project.endpoint.controller;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.Webshop;
import com.optiply.services.project.endpoint.dto.WebshopDto;
import com.optiply.services.project.endpoint.service.WebshopService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.util.List;

@Controller("/webshops")
public class WebshopController {


    private final WebshopService webshopService;

    public WebshopController(WebshopService webshopService) {
        this.webshopService = webshopService;
    }

    @Operation(summary = "Returns all webshops")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Tag(name = "webshop")
    @Get("/all")
    public Mono<MutableHttpResponse<List<Webshop>>> getAll() {
        Mono<List<Webshop>> webshops = webshopService.getAllWebshop();

        return Mono.from(webshops)
                .map(response -> {
                    if(response.isEmpty())
                        return HttpResponse.notFound().body(response);

                    return HttpResponse.ok(response);
                })
                .onErrorReturn(HttpResponse.serverError());

    }

    @Operation(summary = "Returns a webshop by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Tag(name = "webshop")
    @Get("/{id}")
    public Mono<MutableHttpResponse<Webshop>> getById(@PathVariable @Parameter(required = true,description ="Webshop Id") int id){
        Mono<Webshop> webshop = webshopService.getWebshopById(id);

        return Mono.from(webshop)
                .map(response -> HttpResponse.ok(response))
                .switchIfEmpty(Mono.just(HttpResponse.notFound()))
                .onErrorReturn(HttpResponse.serverError());

    };

    @Operation(summary = "Creates a new webshop")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Tag(name = "webshop")
    @Post(value = "/",consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Mono<MutableHttpResponse<Webshop>> insert(@Body @Parameter(required = true, description = "WebshopDto Body") WebshopDto webshopDto){
        Mono<Webshop> webshop = webshopService.addWebshop(webshopDto);

        return Mono.from(webshop)
                .map(response -> HttpResponse.created(response))
                .switchIfEmpty(Mono.just(HttpResponse.serverError()));
    }

    @Operation(summary = "Creates multiple new webshops")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Tag(name = "webshop")
    @Post(value = "/multi",consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Mono<MutableHttpResponse<List<Webshop>>> insertMultiple(@Body @Parameter(required = true, description = "Array of WebshopDto Body") List<WebshopDto> webshopsDto){
        Flux<Webshop> webshop = webshopService.addWebshops(webshopsDto);

        return Flux.from(webshop)
                .collectList()
                .map(response -> {
                    if(response.isEmpty())
                        return HttpResponse.notFound().body(response);

                    return HttpResponse.created(response);
                })
                .onErrorReturn(HttpResponse.serverError());
    }

    @Operation(summary = "Updates an existing webshops")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Tag(name = "webshop")
    @Put(value = "/{id}",consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Mono<MutableHttpResponse<Webshop>> update(@PathVariable  @Parameter(required = true,description ="Webshop Id") int id, @Body @Parameter(required = true, description = "WebshopDto Body")  WebshopDto webshop1){
        Mono<Webshop> webshop = webshopService.updateWebshop(id,webshop1);

        return Mono.from(webshop)
                .map(response -> HttpResponse.ok(response))
                .switchIfEmpty(Mono.just(HttpResponse.notFound()))
                .onErrorReturn(HttpResponse.serverError());
    }

    @Operation(summary = "Delete a webshop by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Tag(name = "webshop")
    @Delete(value = "/{id}",consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Mono<MutableHttpResponse<Boolean>> delete(@PathVariable @Parameter(required = true,description ="Webshop Id") int id){
        Mono<Boolean> webshop = webshopService.deleteWebshop(id);

        return Mono.from(webshop)
                .map(response -> {
                    if(response==true)
                        return HttpResponse.noContent().body(true);
                    else
                        return HttpResponse.notFound().body(false);
                })
                .onErrorReturn(HttpResponse.serverError());
    }

    @Operation(summary = "Returns all the webshops the matches with the sort or/and filter ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Tag(name = "webshop")
    @Get("/search{?sortBy,filter}")
    public Mono<MutableHttpResponse<List<Webshop>>> getAllSortOrAndFilter(@Nullable @Parameter(description = "Sort by field", example = "interest_rate,id,service_levels")String sortBy
            , @Nullable @Parameter(name = "Filter by fields", example = "handle:test,handle%test,emails:test@test.com,emails%test@test.com,") List<String> filter){
        Flux<Webshop> webshop = webshopService.getAllWebshopsSortAndFilter(sortBy,filter);

        return Flux.from(webshop)
                .collectList()
                .map(response -> {
                    if(response.isEmpty())
                        return HttpResponse.notFound().body(response);

                    return HttpResponse.ok(response);
                })
                .onErrorReturn(HttpResponse.serverError());

    }
}


//
//return Flux.from(webshops)
//        .collectSortedList(Comparator.comparing(Webshop::getId)) //collectList() tambem funciona
//        .map(response -> HttpResponse.ok(response))
//        .switchIfEmpty(Mono.just(HttpResponse.notFound()))
//        .onErrorReturn(HttpResponse.serverError())
//        .log();

//    @Get("/asc")
//    public Mono<MutableHttpResponse<?>> getAllAsc(){
//        Flux<Webshop> webshop = webshopService.getAllWebshopsAsc();
//
//        return Flux.from(webshop)
//                .collectSortedList(Comparator.comparing(Webshop::getId))// If empty returns [] // fazer na query
//                .map(response -> {
//                    if(response.isEmpty())
//                        return HttpResponse.notFound();
//
//                    return HttpResponse.ok(response);
//                })
//                .onErrorReturn(HttpResponse.serverError());
//    }
//
//    @Get("/{coluna}:{valor}")
//    public Mono<MutableHttpResponse<List<Webshop>>> filtroEqual(@PathVariable String coluna, @PathVariable String valor){
//        Flux<Webshop> webshop = webshopService.getAllWebshopsEqual(coluna,valor);
//
//        return Flux.from(webshop)
//                .collectSortedList(Comparator.comparing(Webshop::getId))
//                .map(response -> HttpResponse.ok(response))
//                .switchIfEmpty(Mono.just(HttpResponse.notFound()))
//                .onErrorReturn(HttpResponse.serverError());
//    }
//
//    @Get("/{coluna}%25{valor}")
//    public Mono<MutableHttpResponse<List<Webshop>>> filtroIlike(@PathVariable String coluna, @PathVariable String valor){
//        Flux<Webshop> webshop = webshopService.getAllWebshopsILike(coluna,valor);
//
//        return Flux.from(webshop)
//                .collectSortedList(Comparator.comparing(Webshop::getId))
//                .map(response -> HttpResponse.ok(response))
//                .switchIfEmpty(Mono.just(HttpResponse.notFound()))
//                .onErrorReturn(HttpResponse.serverError());
//    }
//
//    @Get("/{coluna}%3c{valor}")
//    public Mono<MutableHttpResponse<?>> filtroMenor(@PathVariable String coluna, @PathVariable int valor){
//        Flux<Webshop> webshop = webshopService.getAllWebshopsSmaller(coluna,valor);
//
//        return Flux.from(webshop)
//                .collectSortedList(Comparator.comparing(Webshop::getId))// If empty returns []
//                .map(response -> {
//                    if(response.isEmpty())
//                        return HttpResponse.notFound();
//
//                    return HttpResponse.ok(response);
//                })
//                .onErrorReturn(HttpResponse.serverError());
//    }
//
//    @Get("/{coluna}%3e{valor}")
//    public Mono<MutableHttpResponse<?>> filtroMaior(@PathVariable String coluna, @PathVariable int valor){
//        Flux<Webshop> webshop = webshopService.getAllWebshopsGreater(coluna,valor);
//
//        return Flux.from(webshop)
//                .collectSortedList(Comparator.comparing(Webshop::getId))// If empty returns []
//                .map(response -> {
//                    if(response.isEmpty())
//                        return HttpResponse.notFound();
//
//                    return HttpResponse.ok(response);
//                })
//                .onErrorReturn(HttpResponse.serverError());
//    }
