package client;

import org.junit.jupiter.api.Test;
import org.noear.solon.net.http.textstream.ServerSentEvent;
import org.noear.solon.rx.SimpleSubscriber;
import org.noear.solon.test.HttpTester;
import org.noear.solon.test.SolonTest;
import webapp.HelloApp;

import java.util.concurrent.CountDownLatch;

@SolonTest(HelloApp.class)
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

        path("/chat/stream").data("prompt", "hello").
                execAsSseStream("POST")
                .subscribe(new SimpleSubscriber<ServerSentEvent>()
                        .doOnNext(sse -> {
                            System.out.println(sse);
                        }).doOnComplete(() -> {
                            latch.countDown();
                        })
                        .doOnError(err -> {
                            err.printStackTrace();
                            latch.countDown();
                        }));

        latch.await();
    }

    @Test
    public void stream_getWeather() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        path("/chat/stream").data("prompt", "杭州今天天气怎么样？")
                .execAsSseStream("POST")
                .subscribe(new SimpleSubscriber<ServerSentEvent>()
                        .doOnNext(sse -> {
                            System.out.println(sse);
                        }).doOnComplete(() -> {
                            latch.countDown();
                        })
                        .doOnError(err -> {
                            err.printStackTrace();
                            latch.countDown();
                        }));

        latch.await();
    }
}
