package com.optiply.services.project.endpoint.repositories;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.Webshop;
import com.optiply.infrastructure.data.support.sql.QueryResult;
import io.micronaut.data.r2dbc.operations.R2dbcOperations;
import io.micronaut.transaction.TransactionDefinition;
import io.micronaut.transaction.support.DefaultTransactionDefinition;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.jooq.*;
import org.jooq.impl.DSL;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.transaction.Transactional;

import java.util.List;

import static com.optiply.infrastructure.data.jooq.repositories.tables.Webshop.WEBSHOP;
import static org.jooq.impl.DSL.*;

/**
 * Example repository to showcase jOOQ
 * Delete this when no longer necessary
 */
@Singleton
public class JooqWebshopRepository implements WebshopRepository {

    private final DSLContext dslContext;
    private final R2dbcOperations operations;
    TransactionDefinition txDefinition = new DefaultTransactionDefinition(TransactionDefinition.Propagation.MANDATORY);


    @Inject
    public JooqWebshopRepository(@Named("dsl") DSLContext dslContext, R2dbcOperations operations) {
        this.dslContext = dslContext;
        this.operations = operations;
    }



    @Override
    public Flux<Webshop> findAll() {
        return Flux
                .from(operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
                        .using(status.getConnection(), SQLDialect.POSTGRES, dslContext.settings())
                        .selectFrom(WEBSHOP)
                        .where(WEBSHOP.DELETED_AT.isNull())))
                .map(result -> result.into(Webshop.class));
                //.doOnNext(System.out::println);
    }

    public Mono<Webshop> findById(int id){
        return Mono
                .from( operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
                        .using(status.getConnection(), SQLDialect.POSTGRES,dslContext.settings())
                        .selectFrom(WEBSHOP)
                        .where(WEBSHOP.ID.equal(id))
                        .and(WEBSHOP.DELETED_AT.isNull())))
                .map(result -> result.into(Webshop.class))
                .doOnNext(System.out::println);
    }

    @Transactional
    public Mono<Webshop> save(Webshop webshop){
        return Mono
                .from( operations.withTransaction(txDefinition, status -> Mono
                        .from(using(status.getConnection(), SQLDialect.POSTGRES,dslContext.settings())
                                .insertInto(WEBSHOP)
                                .columns(WEBSHOP.URL,WEBSHOP.HANDLE,WEBSHOP.SERVICE_LEVEL_CAT_A, WEBSHOP.SERVICE_LEVEL_CAT_B,WEBSHOP.SERVICE_LEVEL_CAT_C,WEBSHOP.EMAILS,WEBSHOP.INTEREST_RATE)
                                .values(webshop.getUrl(),webshop.getHandle(),webshop.getServiceLevelCatA(),webshop.getServiceLevelCatB(),webshop.getServiceLevelCatC(),webshop.getEmails(),webshop.getInterestRate())
                                .returning())

                .map(result -> result.into(Webshop.class))));
    }

    @Transactional
    public Mono<Webshop> update(Webshop webshop){
        return Mono
                .from( operations.withTransaction(txDefinition, status -> Mono
                        .from(DSL
                                .using(status.getConnection(), SQLDialect.POSTGRES, dslContext.settings())
                                .update(WEBSHOP)
                                .set(row(WEBSHOP.URL,WEBSHOP.HANDLE,WEBSHOP.SERVICE_LEVEL_CAT_A, WEBSHOP.SERVICE_LEVEL_CAT_B,WEBSHOP.SERVICE_LEVEL_CAT_C,WEBSHOP.EMAILS,WEBSHOP.INTEREST_RATE),
                                     row(webshop.getUrl(),webshop.getHandle(),webshop.getServiceLevelCatA(),webshop.getServiceLevelCatB(),webshop.getServiceLevelCatC(),webshop.getEmails(),webshop.getInterestRate()))
                                .where(WEBSHOP.ID.eq(webshop.getId())).and(WEBSHOP.DELETED_AT.isNull())
                                .returning())

                .map(result -> result.into(Webshop.class))));
    }

    @Transactional
    public  Mono<Boolean> delete(int id){
        return Mono.from( operations.withTransaction(txDefinition, status -> Mono
                .from(DSL
                        .using(status.getConnection(),SQLDialect.POSTGRES,dslContext.settings())
                        .update(WEBSHOP)
                        .set(WEBSHOP.DELETED_AT,currentOffsetDateTime())
                        .where(WEBSHOP.ID.eq(id))
                )
                .map(result -> result == QueryResult.SUCCESS.ordinal())));
    }

    public Flux<Webshop> findAndSort(String coluna) {
        return Flux
                .from(operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
                        .using(status.getConnection(), SQLDialect.POSTGRES, dslContext.settings())
                        .selectFrom(WEBSHOP)
                        .where(WEBSHOP.DELETED_AT.isNull())
                        .orderBy(field(coluna).asc())))
                .map(result -> result.into(Webshop.class));
    }


    public Flux<Webshop> findAndFilter(List<Condition> conditionList) {

        return Flux
                .from(operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
                        .using(status.getConnection(), SQLDialect.POSTGRES, dslContext.settings())
                        .selectFrom(WEBSHOP)
                        .where(conditionList).and(WEBSHOP.DELETED_AT.isNull())))
                .map(result -> result.into(Webshop.class))
                .doOnNext(System.out::println);
    }

    public Flux<Webshop> findbyFilterAndSort(String colunaSearch,List<Condition> conditionList) { //POR FAZER
        return Flux
                .from(operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
                        .using(status.getConnection(), SQLDialect.POSTGRES, dslContext.settings())
                        .selectFrom(WEBSHOP)
                        .where(conditionList).and(WEBSHOP.DELETED_AT.isNull())
                        .orderBy(field(colunaSearch).asc())))
                .map(result -> result.into(Webshop.class));
    }

    public Mono<Integer> findIdbyHandle(String handle){
        return  Mono
                 .from( operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
                         .using(status.getConnection(), SQLDialect.POSTGRES,dslContext.settings())
                         .select(WEBSHOP.ID)
                         .from(WEBSHOP)
                         .where(WEBSHOP.HANDLE.eq(handle))))
                .map(result -> result.into(Integer.class));
    }
}


//    public Flux<Webshop> findByFilterDots(String coluna, String valor){
//        return Flux
//                .from( operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
//                        .using(status.getConnection(), SQLDialect.POSTGRES,dslContext.settings())
//                        .select(WEBSHOP.asterisk())
//                        .from(WEBSHOP)
//                        .where(field(coluna).eq(valor).and(WEBSHOP.DELETED_AT.isNull()))))
//                .map(result -> result.into(Webshop.class));
//    }
//
//    public Flux<Webshop> findByFilterPercentage(String coluna, String valor){
//        return Flux
//                .from( operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
//                        .using(status.getConnection(), SQLDialect.POSTGRES,dslContext.settings())
//                        .select(WEBSHOP.asterisk())
//                        .from(WEBSHOP)
//                        .where(WEBSHOP.DELETED_AT.isNull().and(field(coluna).likeIgnoreCase(valor)))))
//                .map(result -> result.into(Webshop.class));
//                //.doOnNext(System.out::println);
//    }
//
//
//    public Flux<Webshop> findByFilterSmaller(String coluna, int valor){
//        return Flux
//                .from( operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
//                        .using(status.getConnection(), SQLDialect.POSTGRES,dslContext.settings())
//                        .select(WEBSHOP.asterisk())
//                        .from(WEBSHOP)
//                        .where(WEBSHOP.DELETED_AT.isNull().and(""+coluna+" < ?",valor)))) //maneira diferente de fazer
//                .map(result -> result.into(Webshop.class))
//                .doOnNext(System.out::println);
//    }
//
//    public Flux<Webshop> findByFilterGreater(String coluna, int valor){
//        return Flux
//                .from( operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
//                        .using(status.getConnection(), SQLDialect.POSTGRES,dslContext.settings())
//                        .select(WEBSHOP.asterisk())
//                        .from(WEBSHOP)
//                        .where(WEBSHOP.DELETED_AT.isNull().and(""+coluna+" > ?",valor)))) // maneira diferente de fazer
//                .map(result -> result.into(Webshop.class))
//                .doOnNext(System.out::println);
//    }


//    public Flux<Webshop> findAndFilterEmails(String valor,String condicao) {
//        return Flux
//                .from(operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
//                        .using(status.getConnection(), SQLDialect.POSTGRES, dslContext.settings())
//                        .selectFrom(WEBSHOP)
//                        .where(WEBSHOP.DELETED_AT.isNull().and("? "+condicao+" any(emails)",valor))))
//                .map(result -> result.into(Webshop.class));
//    }

//    public Flux<Webshop> findbyFilterAndSort(String colunaSearch, String coluna, String valor, String condicao) { //POR FAZER
//        return Flux
//                .from(operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
//                        .using(status.getConnection(), SQLDialect.POSTGRES, dslContext.settings())
//                        .selectFrom(WEBSHOP)
//                        .where(WEBSHOP.DELETED_AT.isNull().and(""+coluna+" "+condicao+" ?",valor))
//                        .orderBy(field(colunaSearch).asc())))
//                .map(result -> result.into(Webshop.class));
//    }

//    public Flux<Webshop> findbyFilterAndSort(String colunaSearch, String coluna, int valor, String condicao) {
//        return Flux
//                .from(operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
//                        .using(status.getConnection(), SQLDialect.POSTGRES, dslContext.settings())
//                        .selectFrom(WEBSHOP)
//                        .where(WEBSHOP.DELETED_AT.isNull().and(""+coluna+" "+condicao+" ?",valor))
//                        .orderBy(field(colunaSearch).asc())))
//                .map(result -> result.into(Webshop.class));
//    }

//    public Flux<Webshop> findbyFilterAndSortEmails(String colunaSearch,String condicao, String valor) {
//        return Flux
//                .from(operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
//                        .using(status.getConnection(), SQLDialect.POSTGRES, dslContext.settings())
//                        .selectFrom(WEBSHOP)
//                        .where(WEBSHOP.DELETED_AT.isNull().and("? "+condicao+" any(emails)",valor))
//                        .orderBy(field(colunaSearch).asc())))
//                .map(result -> result.into(Webshop.class));
//    }


//    public Flux<Webshop> findAndFilter(String coluna,String valor, String condicao) {
//        return Flux
//                .from(operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
//                        .using(status.getConnection(), SQLDialect.POSTGRES, dslContext.settings())
//                        .selectFrom(WEBSHOP)
//                        .where(WEBSHOP.DELETED_AT.isNull().and(""+coluna+" "+condicao+" ?",valor))))
//                .map(result -> result.into(Webshop.class));
//    }

//    public Flux<Webshop> findAndFilter(String coluna,int valor, String condicao) {
//
//        return Flux
//                .from(operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
//                        .using(status.getConnection(), SQLDialect.POSTGRES, dslContext.settings())
//                        .selectFrom(WEBSHOP)
//                        .where(WEBSHOP.DELETED_AT.isNull().and(""+coluna+" "+condicao+" ?",valor))))
//                .map(result -> result.into(Webshop.class));
//    }