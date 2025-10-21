package webapp.mcpserver;

import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;
import io.quarkus.arc.ManagedContext;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.noear.solon.web.vertx.VxWebHandler;

import java.io.IOException;
import java.lang.reflect.Method;

@Provider
@PreMatching  // 必须使用这个标记，不然不存在的路径无法执行，直接会被拦截
public class DynamicRoutingFilter implements ContainerRequestFilter {


    @Inject
    RoutingContext routingContext;

    @Inject
    Router router;

    @Inject
    VxWebHandler handler;


    @Inject
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
//
//        System.out.println(request.params());
//        System.out.println(request.headers());
//
//        System.out.println(request.body());


        String realPath = ctx.getUriInfo().getPath();
//        String method = ctx.getMethod();
        String patternPath = "/api/hello/:name";

//        router.routeWithRegex("/mcp/.*").handler(req -> {
//            // 获取上下文
//            ManagedContext requestContext = Arc.container().requestContext();
//            // 激活上下文
//            requestContext.activate();
//            handler.handle(req.request());
//        });

//        router.route(patternPath).handler(rc -> {
//            // 这个的目的是激活 router 对象，当第二次进入的时候，就可以获取到实际的动态 name 了，算是一个bug，所以还是需要直接用 request 直接获取参数即可
//            rc.next();
//        });

//        if (realPath.contains("mcp")){
//            // 获取请求上下文
//            ManagedContext requestContext = Arc.container().requestContext();
//            // 激活上下文
//            requestContext.activate();
//            handler.handle(request);
//        }

        // 如果符合规则的话，则会触发实现
        if(PathMatcher.isMatch(patternPath,realPath)){
            String target = "org.noear.quarkus.path.HelloNamePath#hello";
            // 解析 target e.g. "com.example.Hello#hello"
            String[] parts = target.split("#");
            String className = parts[0];
            String methodName = parts[1];

            try {
                Class<?> cls = Class.forName(className, false, Thread.currentThread().getContextClassLoader());

                Object bean = null;
                try {
                    InstanceHandle handle = Arc.container().instance(cls);
                    if (handle != null && handle.isAvailable()) {
                        bean = handle.get();
                    }
                } catch (Exception ignored) {}

                // 优先尝试接收 ContainerRequestContext
                try {
                    // 这个就是对应的参数对象方法获取了
                    Method m = cls.getMethod(methodName, RoutingContext.class);
                    // 这个就能触发内部方法，并且quarkus的相应注入对象，就能获取到， 其他拦截前的响应都会失效，但是通过 ctx.abortWith 即可触发 quarkus 自带的一系列后置响应拦截实现
                    Object ret = m.invoke(bean, routingContext);
                    // 如果方法自己写响应可以返回 null 或 void —— 你需要决定如何判断
                    if (ret instanceof Response) {
                        ctx.abortWith((Response) ret);
                    } else if (ret instanceof String) {
                        ctx.abortWith(Response.ok(ret).build());
                    } else {
                        ctx.abortWith(Response.noContent().build());
                    }
                    return;
                } catch (NoSuchMethodException ex) {
                    // 尝试无参方法
                    Method m = cls.getMethod(methodName);
                    Object ret = m.invoke(bean);
                    if (ret instanceof Response) {
                        ctx.abortWith((Response) ret);
                    } else if (ret instanceof String) {
                        ctx.abortWith(Response.ok(ret).build());
                    } else {
                        ctx.abortWith(Response.noContent().build());
                    }
                    return;
                }
            } catch (Throwable t) {
                // 出错返回 500
                ctx.abortWith(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("dynamic route invoke error: " + t.getMessage()).build());
            }
        }





    }
}
