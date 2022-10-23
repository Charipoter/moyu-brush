package com.moyu.brush.server.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@Component
@Slf4j
public class AsyncTransactionSupport {

    @Transactional
    public <IN, OUT> OUT runWrappedFunctionForTransaction(Function<IN, OUT> function,
                                                          IN item,
                                                          CountDownLatch countDownLatch,
                                                          AtomicBoolean rollback) {

        OUT r = null;
        try {
            // 将事务传播进去
            r = function.apply(item);
        } catch (Exception e) {
            rollback.set(true);
            // 抛出异常回滚(可能重复？)
            throw new RuntimeException(e);
        } finally {
            countDownLatch.countDown();
        }
        // 等待所有其他异步任务完成
        try {
            // 可能不太好，任务一多会阻塞大量线程，应当等待指定时间
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (rollback.get()) {
            log.warn("异步事务出现异常进行回滚");
            throw new RuntimeException();
        } else {
            return r;
        }
    }

}
