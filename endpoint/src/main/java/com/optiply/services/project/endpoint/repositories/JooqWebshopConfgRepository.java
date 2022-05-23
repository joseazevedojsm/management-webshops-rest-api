package com.optiply.services.project.endpoint.repositories;

import com.optiply.infrastructure.data.jooq.repositories.tables.pojos.WebshopConfg;
import io.micronaut.data.r2dbc.operations.R2dbcOperations;
import io.micronaut.transaction.TransactionDefinition;
import io.micronaut.transaction.support.DefaultTransactionDefinition;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

import static com.optiply.infrastructure.data.jooq.repositories.tables.WebshopConfg.WEBSHOP_CONFG;
import static org.jooq.impl.DSL.row;

@Singleton
public class JooqWebshopConfgRepository implements WebshopConfigRepository{

    @Inject
    @Named("dsl")
    DSLContext dslContext;

    @Inject
    R2dbcOperations operations;


    TransactionDefinition txDefinition = new DefaultTransactionDefinition(TransactionDefinition.Propagation.MANDATORY);


    @Transactional
    public Mono<WebshopConfg> save(WebshopConfg webshopConfg, int id) {
        return Mono.
                from(operations.withTransaction(txDefinition, status -> Mono
                        .from(DSL
                                .using(status.getConnection(), SQLDialect.POSTGRES,dslContext.settings())
                                .insertInto(WEBSHOP_CONFG)
                                .columns(WEBSHOP_CONFG.RUN_JOBS,WEBSHOP_CONFG.MULTI_SUPPLIER,WEBSHOP_CONFG.CURRENCY, WEBSHOP_CONFG.FK_WEBSHOP)
                                .values(webshopConfg.getRunJobs(),webshopConfg.getMultiSupplier(),webshopConfg.getCurrency(),id)
                                .returningResult(WEBSHOP_CONFG.asterisk()))
                        .map(result -> result.into(WebshopConfg.class))
                ));
    }

    @Transactional
    public Mono<WebshopConfg> update(WebshopConfg webshopConfg) {
        return Mono
                .from( operations.withTransaction(txDefinition, status -> Mono
                        .from(DSL
                                .using(status.getConnection(), SQLDialect.POSTGRES, dslContext.settings())
                                .update(WEBSHOP_CONFG)
                                .set(row(WEBSHOP_CONFG.RUN_JOBS,WEBSHOP_CONFG.MULTI_SUPPLIER,WEBSHOP_CONFG.CURRENCY),
                                        row(webshopConfg.getRunJobs(),webshopConfg.getMultiSupplier(),webshopConfg.getCurrency()))
                                .where(WEBSHOP_CONFG.FK_WEBSHOP.eq(webshopConfg.getFkWebshop()))
                                .returningResult(WEBSHOP_CONFG.asterisk()))

                        .map(result -> result.into(WebshopConfg.class))));
    }


    public Mono<WebshopConfg> findById(int id) {
        return Mono
                .from( operations.withTransaction(TransactionDefinition.READ_ONLY, status -> DSL
                        .using(status.getConnection(), SQLDialect.POSTGRES,dslContext.settings())
                        .select(WEBSHOP_CONFG.asterisk())
                        .from(WEBSHOP_CONFG)
                        .where(WEBSHOP_CONFG.FK_WEBSHOP.equal(id))))
                .map(result -> result.into(WebshopConfg.class))
                .doOnNext(System.out::println);
    }
}
