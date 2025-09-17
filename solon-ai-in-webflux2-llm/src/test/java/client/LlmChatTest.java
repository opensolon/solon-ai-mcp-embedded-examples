package client;

import org.junit.jupiter.api.Test;
import org.noear.solon.test.HttpTester;
import org.noear.solon.test.SolonTest;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 运行单测前，先手动运行 HelloApp
 * */
@SolonTest
public class LlmChatTest extends HttpTester {
    @Test
    public void call_hello() throws Exception {
        String rst = path("/chat/call").data("prompt", "hello").post();
        System.out.println(rst);

        assert rst != null && rst.length() > 0;
    }

    @Test
    public void call_getWeather() throws Exception {
        String rst = path("/chat/call").data("prompt", "杭州今天天气怎么样？").post();
        System.out.println(rst);

        assert rst != null && rst.length() > 0;
    }

    @Test
    public void stream_hello() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger counter = new AtomicInteger();

        Flux.from(path("/chat/stream").data("prompt", "hello").
                        execAsSseStream("POST"))
                .doOnNext(sse -> {
                    System.out.println(sse);
                    counter.incrementAndGet();
                }).doOnComplete(() -> {
                    latch.countDown();
                })
                .doOnError(err -> {
                    err.printStackTrace();
                    latch.countDown();
                })
                .subscribe();

        latch.await();

        assert counter.get() > 0;
    }

    @Test
    public void stream_getWeather() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger counter = new AtomicInteger();

        Flux.from(path("/chat/stream").data("prompt", "杭州今天天气怎么样？")
                        .execAsSseStream("POST"))
                .doOnNext(sse -> {
                    System.out.println(sse);
                    counter.incrementAndGet();
                }).doOnComplete(() -> {
                    latch.countDown();
                })
                .doOnError(err -> {
                    err.printStackTrace();
                    latch.countDown();
                })
                .subscribe();

        latch.await();

        assert counter.get() > 0;
    }
}
