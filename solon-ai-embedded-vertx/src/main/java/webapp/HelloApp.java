package webapp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.noear.solon.Solon;
import webapp.mcpserver.McpServerConfig;

public class HelloApp extends AbstractVerticle {
    public static void main(String[] args) throws Exception {
        if(Solon.app() != null) {
            return;
        }

        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);

        vertx.deployVerticle(new HelloApp(router));
        vertx.deployVerticle(new McpServerConfig(router));

        //上面是异步的，卡一下方便单测运行
        Thread.sleep(1000);
    }

    private final Router router;

    public HelloApp(Router router) {
        this.router = router;
    }

    @Override
    public void start() {
        router.get("/").handler(req -> {
            req.response().putHeader("content-type", "text/plain")
                    .end("Hello world: " + req.request().getParam("name"));
        });

        vertx.createHttpServer().requestHandler(router::handle).listen(8080);
    }
}
