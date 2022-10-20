package com.moyu.brush.server.service.impl;

import com.moyu.brush.server.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class AsyncServiceImpl implements AsyncService {
    @Autowired
    private ExecutorService defaultExecutorService;

    @Override
    public <IN, OUT> CompletableFuture<List<OUT>> runFunctionsAsync(Function<IN, OUT> function, Collection<IN> collection, ExecutorService executorService) {
        List<CompletableFuture<OUT>> futures = collection.stream().map(item -> CompletableFuture.supplyAsync(
                () -> function.apply(item), executorService)).toList();

        return runFuturesAsync(futures);
    }
    @Override
    public <IN, OUT> List<OUT> runFunctions(Function<IN, OUT> function, Collection<IN> collection, ExecutorService executorService) {
        return runFuture(runFunctionsAsync(function, collection, executorService));
    }

    @Override
    public <IN, OUT> CompletableFuture<List<OUT>> runFunctionsAsync(Function<IN, OUT> function, Collection<IN> collection) {
        return runFunctionsAsync(function, collection, defaultExecutorService);
    }

    @Override
    public <IN, OUT> List<OUT> runFunctions(Function<IN, OUT> function, Collection<IN> collection) {
        return runFunctions(function, collection, defaultExecutorService);
    }

    @Override
    public <OUT> CompletableFuture<List<OUT>> runSuppliesAsync(Collection<Supplier<OUT>> suppliers) {
        List<CompletableFuture<OUT>> futures = suppliers.stream().map(supplier -> CompletableFuture.supplyAsync(supplier, defaultExecutorService)).toList();

        return runFuturesAsync(futures);
    }

    @Override
    public <OUT> List<OUT> runSupplies(Collection<Supplier<OUT>> suppliers) {
        return runFuture(runSuppliesAsync(suppliers));
    }

    private <OUT> OUT runFuture(CompletableFuture<OUT> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <OUT> List<OUT> runFutures(Collection<CompletableFuture<OUT>> futures) {

        CompletableFuture<List<OUT>> future = runFuturesAsync(futures);

        return runFuture(future);
    }

    @Override
    public <OUT> CompletableFuture<List<OUT>> runFuturesAsync(Collection<CompletableFuture<OUT>> futures) {

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        return allOf.thenApply(v -> futures.stream().map(future -> future.getNow(null)).toList());
    }

    @Override
    public void runFuturesAnyway(Collection<CompletableFuture<?>> futures) {
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        allOf.join();
    }
}
