package com.moyu.brush.server.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 异步执行服务
 */
public interface AsyncService {

    <IN, OUT> CompletableFuture<List<OUT>> runFunctionsAsync(Function<IN, OUT> function, Collection<IN> collection, ExecutorService executorService);

    <IN, OUT> CompletableFuture<List<OUT>> runFunctionsAsync(Function<IN, OUT> function, Collection<IN> collection);

    <IN, OUT> List<OUT> runFunctions(Function<IN, OUT> function, Collection<IN> collection, ExecutorService executorService);

    <IN, OUT> List<OUT> runFunctions(Function<IN, OUT> function, Collection<IN> collection);

    <OUT> List<OUT> runSupplies(Collection<Supplier<OUT>> suppliers);

    <OUT> CompletableFuture<List<OUT>> runSuppliesAsync(Collection<Supplier<OUT>> suppliers);

    <OUT> List<OUT> runFutures(Collection<CompletableFuture<OUT>> futures);

    <OUT> CompletableFuture<List<OUT>> runFuturesAsync(Collection<CompletableFuture<OUT>> futures);

    void runFuturesAnyway(Collection<CompletableFuture<?>> futures);
}
