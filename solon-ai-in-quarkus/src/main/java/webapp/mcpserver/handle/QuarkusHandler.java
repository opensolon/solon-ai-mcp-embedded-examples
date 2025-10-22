package webapp.mcpserver.handle;


import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import org.noear.solon.Solon;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.solon.lang.Nullable;
import org.noear.solon.web.vertx.VxHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

public class QuarkusHandler implements VxHandler {
    static final Logger log = LoggerFactory.getLogger(QuarkusHandler.class);
    @Nullable
    private Executor executor;
    @Nullable
    private Handler handler;

    public QuarkusHandler() {
    }

    protected void preHandle(Context ctx) throws IOException {
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }


    public void handle(HttpServerRequest request) {
        HttpServerResponse response = request.response();

        try {
            // GET 请求没有 body，直接处理
            if ("GET".equals(request.method().name())) {
                this.handleDo(request, null, false);
            } else {
                // 对于非 GET 请求，我们使用一个变量来存储 body
                // Buffer 可以动态增长，非常适合收集数据块
                Buffer body = Buffer.buffer();

                // 1. 设置数据块处理器，用于接收 body 数据
                request.handler(body::appendBuffer);

                // 2. 设置请求结束处理器，在请求完全接收后（无论有无 body）触发
                request.endHandler(v -> {
                    // 请求已经结束，body 中包含了完整的请求体（如果有的话）
                    this.handleDo(request, body, true);
                });

                // (可选但推荐) 3. 设置异常处理器，防止客户端突然断开连接等问题
                request.exceptionHandler(ex -> {
                    log.error("Request processing failed", ex);
                    if (!response.ended()) {
                        response.setStatusCode(500).end("Request failed");
                    }
                });

            }
        } catch (Throwable var4) {
            Throwable ex = var4;
            log.warn(ex.getMessage(), ex);
            if (!response.ended()) {
                response.setStatusCode(500);
                response.end();
            }
        }
    }

    private void handleDo(HttpServerRequest request, Buffer requestBody, boolean disPool) {
        QuarkusContext ctx = new QuarkusContext(request, requestBody);
        if (this.executor != null && !disPool) {
            try {
                this.executor.execute(() -> {
                    this.handle0(ctx);
                });
            } catch (RejectedExecutionException var6) {
                this.handle0(ctx);
            }
        } else {
            this.handle0(ctx);
        }

    }

    private void handle0(QuarkusContext ctx) {
        try {
            ctx.contentType("text/plain;charset=UTF-8");
            this.preHandle(ctx);
            if (this.handler == null) {
                Solon.app().tryHandle(ctx);
            } else {
                this.handler.handle(ctx);
            }

            if (!ctx.asyncStarted()) {
                ctx.innerCommit();
            }
        } catch (Throwable var3) {
            Throwable e = var3;
            log.warn(e.getMessage(), e);
            ctx.innerGetResponse().setStatusCode(500);
            ctx.innerGetResponse().end();
        }

    }
}
