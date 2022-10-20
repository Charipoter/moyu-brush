package com.moyu.brush.server.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@Component
@Slf4j
public class AsyncUtilSupport {

    @Autowired
    private AsyncUtilSupprtFunction asyncUtilSupprtFunction;

    public <IN, OUT> List<OUT> runFunctionsTransactional(Function<IN, OUT> function, Collection<IN> collection, ExecutorService executorService) {
        final CountDownLatch countDownLatch = new CountDownLatch(collection.size());
        final AtomicBoolean rollback = new AtomicBoolean(false);

        List<CompletableFuture<OUT>> futures = collection.stream().map(item -> CompletableFuture.supplyAsync(
                () -> asyncUtilSupprtFunction.runWrappedFunctionForTransaction(function, item, countDownLatch, rollback),
                executorService)).toList();

        List<OUT> outs = AsyncUtil.runFutures(futures);
        if (rollback.get()) {
            log.info("回滚主事务");
            throw new RuntimeException("因个别任务失败而回滚");
        }
        return outs;
    }

}
