package com.optiply.services.project.endpoint.service;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.Webshop;
import com.optiply.services.project.endpoint.dto.WebshopDto;
import com.optiply.services.project.endpoint.mapper.WebshopMapper;
import com.optiply.services.project.endpoint.repositories.JooqWebshopRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jooq.*;
import org.jooq.impl.DSL;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class WebshopService {

    @Inject
    JooqWebshopRepository webshopRepository;

    @Inject
    WebshopMapper webshopMapper;

    public Mono<Webshop> addWebshop(WebshopDto dto) {
        Webshop webshop = new Webshop();
        webshop = webshopMapper.updateWebshopFromDto(dto, webshop); // webshop = because of test, i could do void updateWebshopFromDto

        if (!checkParam(webshop))
            return Mono.error(new RuntimeException("Please check the fields, incorrect format "));

        Mono<Webshop> temp = webshopRepository.save(webshop);
        return temp;
    }

    public Flux<Webshop> addWebshops(List<WebshopDto> webshops) {
        Flux<WebshopDto> fwb = Flux.fromIterable(webshops);
        return Flux.from(fwb)
                .flatMap(r -> addWebshop(r));
    }

    public Mono<Webshop> updateWebshop(int id, WebshopDto dto) {
        return Mono.from(webshopRepository.findById(id))
                .map(webshop -> {
                    webshop = webshopMapper.updateWebshopFromDto(dto, webshop);
                    System.out.println(webshop + "********************");
                    return webshop;
                })
                .flatMap(result -> {
                    if (!checkParam(result))
                        return Mono.error(new RuntimeException("Please check the fields, incorrect format "));
                    return webshopRepository.update(result);
                });
    }

    public Mono<Boolean> deleteWebshop(int id) {
        Mono<Boolean> temp = webshopRepository.delete(id);
        return temp;
    }

    public Mono<Webshop> getWebshopById(int id) {
        if (id < 1)
            return Mono.error(new RuntimeException("Please check the fields, incorrect format "));
        Mono<Webshop> temp = webshopRepository.findById(id);
        return temp;
    }

    public Mono<List<Webshop>> getAllWebshop() {
        Mono<List<Webshop>> temp = webshopRepository.findAll().collectSortedList(java.util.Comparator.comparing(Webshop::getId));// If empty returns [];
        return temp;
    }

    public Flux<Webshop> getAllWebshopsSortAndFilter(String colunaSort, List<String> filters) {
        if (filters == null && colunaSort != null) {
            return webshopRepository.findAndSort(colunaSort);
        } else if (colunaSort == null && filters != null) {
            return webshopRepository.findAndFilter(addConditions(filters));
        } else if (filters != null && colunaSort != null) {
            return webshopRepository.findbyFilterAndSort(colunaSort,addConditions(filters));
        }

        return Flux.empty();
    }

    public int sum(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 99 || arr[i] < 1)
                return -1;
            sum += arr[i];
        }
        return sum;
    }

    public boolean checkParam(Webshop webshop) {
        String url = webshop.getUrl();
        String[] emails = webshop.getEmails();
        webshop.setInterestRate(webshop.getInterestRate() == null ? 20 : webshop.getInterestRate());

        if (!url.startsWith("https://") || webshop.getHandle().equals(""))
            return false;

        if (emails != null && !Arrays.stream(emails).allMatch(email -> email.matches("^(?=.*?[0-9a-zA-Z])[0-9a-zA-Z]*[@][0-9a-zA-Z .]*$")))
            return false;

        int[] categoria = new int[]{webshop.getServiceLevelCatA(), webshop.getServiceLevelCatB(), webshop.getServiceLevelCatC()};

        int sum = sum(categoria);
        if (sum < 100 || sum > 100) {
            return false;
        }
        return true;
    }

    public boolean checkColumn(String coluna, int valor) {
        if (valor < 0)
            return false;
        if (!(coluna.equals("service_level_cat_a") || coluna.equals("service_level_cat_b") || coluna.equals("service_level_cat_c") || coluna.equals("interest_rate") || coluna.equals("id")))
            return false;

        return true;
    }

    public String[] checkFilterParams(String filter) { // SO PASSA SE TIVER AS CONDICOES
        String[] valuesArr = filter.split("((?=:|>|<|:|%)|(?<=:|>|<|:|%))");

        if (valuesArr.length == 3 && valuesArr[1].matches("<|>|:|%")) {
            if (valuesArr[1].equals(":"))
                valuesArr[1] = "=";

            if (valuesArr[0].matches("interest_rate|service_level_cat_a|service_level_cat_b|service_level_cat_c|id")) {
                if (valuesArr[2].matches("^[0-9]*$") && !valuesArr[1].equals("%")) {
                    return valuesArr;
                }
            } else if (valuesArr[0].matches("handle|url|emails")) {
                if (valuesArr[1].equals("%"))
                    valuesArr[1] = "ilike";
                return valuesArr;
            }
        }
        return null;
    }

    public List<Condition> addConditions(List<String> filters) {
        List<Condition> list = new ArrayList<>();
        for (String filter : filters) {

            String[] query = checkFilterParams(filter);
            if(query!=null) {

                String filterCondition = query[1];
                Field<Object> field = DSL.field(query[0]);

                if (field != null && !field.equals(DSL.field("emails"))) {
                    String fieldValue = query[2];

                    if(fieldValue.matches("^[0-9]*$")) {
                        int fieldValueInt = Integer.valueOf(fieldValue);

                        if (Comparator.EQUALS.toSQL().equals(filterCondition)) {
                            list.add(field.eq(fieldValueInt));
                        } else if (Comparator.GREATER.toSQL().equals(filterCondition)) {
                            list.add(field.gt(fieldValueInt));
                        } else if (Comparator.LESS.toSQL().equals(filterCondition)) {
                            list.add(field.lt(fieldValueInt));
                        }
                    } else {
                        if (Comparator.EQUALS.toSQL().equals(filterCondition)) {
                            list.add(field.eq(fieldValue));
                        } else if (Comparator.LIKE_IGNORE_CASE.toSQL().equals(filterCondition)) {
                            list.add(field.likeIgnoreCase(fieldValue));
                        } else if (Comparator.GREATER.toSQL().equals(filterCondition)) {
                            list.add(field.gt(fieldValue));
                        } else if (Comparator.LESS.toSQL().equals(filterCondition)) {
                            list.add(field.lt(fieldValue));
                        }
                    }

                } else {
                    String fieldValue = query[2];
                    if (Comparator.EQUALS.toSQL().equals(filterCondition)) {
                        list.add(DSL.condition(fieldValue + " = any(emails)"));
                    } else if (Comparator.LIKE_IGNORE_CASE.toSQL().equals(filterCondition)) {
                        list.add(DSL.condition(fieldValue + " ilike any(emails)"));
                    }
                }
            }
        }
        return list;
    }
}


//    public WebshopService(JooqWebshopRepository webshopRepository) { SAME AS @INJECT
//        this.webshopRepository = webshopRepository;
//    }


//    public Flux<Webshop> getAllWebshopsSortAndFilter(String colunaSort, String filter) {
//        if (filter == null && colunaSort != null) {
//            return webshopRepository.findAndSort(colunaSort);
//        } else if (colunaSort == null && filter != null) {
//            String[] query = checkFilterParams(filter);//tirava
//            if (query != null) {
//                if (query[2].matches("^[0-9]*$")) // caso para  verificar valores numericos
//                    return webshopRepository.findAndFilter(query[0], Integer.parseInt(query[2]), query[1]);
//                else if (query[0].equals("emails")) // caso para verificar emails
//                    return webshopRepository.findAndFilterEmails(query[2], query[1]);
//
//                return webshopRepository.findAndFilter(query[0], query[2], query[1]);
//            }
//        } else if (filter != null && colunaSort != null) {
//            String[] query = checkFilterParams(filter);
//            if (query != null) {
//
//                if (query[2].matches("^[0-9]*$"))
//                    return webshopRepository.findbyFilterAndSort(colunaSort, query[0], Integer.parseInt(query[2]), query[1]);
//                else if (query[0].equals("emails"))
//                    return webshopRepository.findbyFilterAndSortEmails(colunaSort, query[1], query[2]);
//
//                return webshopRepository.findbyFilterAndSort(colunaSort, query[0], query[2], query[1]);
//            }
//        }
//
//        return Flux.empty();
//    }
