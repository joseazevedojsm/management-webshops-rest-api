package com.optiply.core.reactor;

import org.reactivestreams.Publisher;
import reactor.core.CoreSubscriber;
import reactor.core.Fuseable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Operators;

import java.time.Duration;

/**
 * Represents a boolean publisher with a value of false.
 */
public class MonoTrue extends Mono<Boolean> implements Fuseable.ScalarCallable<Boolean>, Fuseable {
    static final Publisher<Boolean> INSTANCE = new MonoTrue();

    /**
     * Returns a properly parametrized instance of this true Publisher.
     *
     * @param <T> the output type
     * @return a properly parametrized instance of this true Publisher
     */
    static <T> Mono<T> instance() {
        return (Mono<T>) INSTANCE;
    }

    @Override
    public void subscribe(CoreSubscriber<? super Boolean> actual) {
        actual.onSubscribe(Operators.scalarSubscription(actual, true));
    }

    @Override
    public Boolean call() {
        return true;
    }

    @Override
    public Boolean block(Duration m) {
        return true;
    }

    @Override
    public Boolean block() {
        return true;
    }

}
