package webapp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import webapp.mcpserver.McpServerConfig;

public class HelloApp extends AbstractVerticle {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);

        vertx.deployVerticle(new HelloApp(router));
        vertx.deployVerticle(new McpServerConfig());
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
