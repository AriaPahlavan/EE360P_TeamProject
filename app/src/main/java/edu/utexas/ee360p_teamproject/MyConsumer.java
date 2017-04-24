package edu.utexas.ee360p_teamproject;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A modified version of the Consumer interface in Java 8
 * that can be run in SDKs lower than 24
 */

@FunctionalInterface
public interface MyConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t);
}