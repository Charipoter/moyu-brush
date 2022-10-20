package com.moyu.brush.server.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class AsyncUtil {

    public static <IN, OUT> CompletableFuture<List<OUT>> runFunctionsAsync(Function<IN, OUT> function, Collection<IN> collection, ExecutorService executorService) {
        List<CompletableFuture<OUT>> futures = collection.stream().map(item -> CompletableFuture.supplyAsync(
                () -> function.apply(item), executorService)).toList();

        return runFuturesAsync(futures);
    }

    public static <IN, OUT> List<OUT> runFunctions(Function<IN, OUT> function, Collection<IN> collection, ExecutorService executorService) {
        return runFuture(runFunctionsAsync(function, collection, executorService));
    }

    public static <OUT> CompletableFuture<List<OUT>> runSuppliesAsync(Collection<Supplier<OUT>> suppliers, ExecutorService executorService) {
        List<CompletableFuture<OUT>> futures = suppliers.stream().map(supplier -> CompletableFuture.supplyAsync(supplier, executorService)).toList();

        return runFuturesAsync(futures);
    }

    public static <OUT> List<OUT> runSupplies(Collection<Supplier<OUT>> suppliers, ExecutorService executorService) {
        return runFuture(runSuppliesAsync(suppliers, executorService));
    }

    public static <OUT, U, E> List<OUT> runBiFunctions(BiFunction<U, E, OUT> biFunction, Collection<U> collectionX, Collection<E> collectionY, ExecutorService executorService) {
        if (collectionX.size() != collectionY.size()) {
            throw new IllegalArgumentException("集合大小不一致");
        }
        List<Object[]> collection = new ArrayList<>(collectionX.size());
        Iterator<U> iteratorX = collectionX.iterator();
        Iterator<E> iteratorY = collectionY.iterator();

        while (iteratorX.hasNext()) {
            collection.add(new Object[]{iteratorX.next(), iteratorY.next()});
        }
        List<CompletableFuture<OUT>> futures = collection.stream().map(
                l -> CompletableFuture.supplyAsync(() -> biFunction.apply((U) l[0], (E) l[1]))).toList();

        return runFutures(futures);
    }

    public static <OUT> OUT runFuture(CompletableFuture<OUT> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static <OUT> List<OUT> runFutures(Collection<CompletableFuture<OUT>> futures) {

        CompletableFuture<List<OUT>> future = runFuturesAsync(futures);

        return runFuture(future);
    }

    public static <OUT> CompletableFuture<List<OUT>> runFuturesAsync(Collection<CompletableFuture<OUT>> futures) {

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        return allOf.thenApply(v -> futures.stream().map(future -> future.getNow(null)).toList());
    }

    public static void runFuturesAnyway(Collection<CompletableFuture<?>> futures) {
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        allOf.join();
    }

}
