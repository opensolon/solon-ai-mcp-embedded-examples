package webapp.mcpserver;

import io.quarkus.arc.Arc;
import io.quarkus.arc.ManagedContext;
import io.quarkus.runtime.Startup;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;
import org.noear.solon.Solon;
import org.noear.solon.ai.chat.tool.MethodToolProvider;
import org.noear.solon.ai.embedding.EmbeddingModel;
import org.noear.solon.ai.mcp.McpChannel;
import org.noear.solon.ai.mcp.server.IMcpServerEndpoint;
import org.noear.solon.ai.mcp.server.McpServerEndpointProvider;
import org.noear.solon.ai.mcp.server.annotation.McpServerEndpoint;
import org.noear.solon.ai.mcp.server.prompt.MethodPromptProvider;
import org.noear.solon.ai.mcp.server.resource.MethodResourceProvider;
import org.noear.solon.web.vertx.VxWebHandler;

import java.util.ArrayList;
import java.util.List;

import webapp.llm._Constants;
import webapp.mcpserver.tool.McpServerTool2;

/**
 * 这个类独立一个目录，可以让 Solon 扫描范围最小化
 * */
@Startup
@ApplicationScoped
public class McpServerConfig  extends AbstractVerticle  {

    @Inject
    Router router;

    @Inject
    @Any
    Instance<IMcpServerEndpoint> serverEndpoints;


    @Inject
    VxWebHandler handler;

    @Produces
    @ApplicationScoped
    public VxWebHandler handler() {
        System.out.println("=== VxWebHandler ===");
        return new VxWebHandler();
    }

    public McpServerConfig() {
       // this.handler = new VxWebHandler();
    }

    @PostConstruct
    @Override
    public void start() {
        System.out.println("McpServerConfig.start");
        router.routeWithRegex("/mcp/.*").handler(req -> {
            System.out.println("=== mcp ===");
            // 获取上下文
//            ManagedContext requestContext = Arc.container().requestContext();
//            // 激活上下文
//            requestContext.activate();
            handler.handle(req.request());
            System.out.println("=== mcp end ===");
//            req.next();
        });

        Solon.start(McpServerConfig.class, new String[]{"--cfg=mcpserver.yml"}, app->{
            //添加全局鉴权过滤器示例（如果不需要，可以删掉）
            app.enableScanning(false); //不扫描
            app.filter(new McpServerAuth());
        });

        //手动构建 mcp 服务端点（只是演示，可以去掉）
        McpServerEndpointProvider endpointProvider = McpServerEndpointProvider.builder()
                .name("McpServerTool2")
                .channel(McpChannel.SSE)
                .sseEndpoint("/mcp/demo2/sse")
                .build();
        endpointProvider.addTool(new MethodToolProvider(new McpServerTool2()));
        endpointProvider.addResource(new MethodResourceProvider(new McpServerTool2()));
        endpointProvider.addPrompt(new MethodPromptProvider(new McpServerTool2()));
        endpointProvider.postStart();

        //手动加入到 solon 容器（只是演示，可以去掉）
        Solon.context().wrapAndPut(endpointProvider.getName(), endpointProvider);

        /**
         * quarkus 组件转为端点
         * */

        quarkusCom2Endpoint();
    }

    @Override
    public void stop() {
        if (Solon.app() != null) {
            Solon.stopBlock(false, Solon.cfg().stopDelay());
        }
    }

    // quarkus 组件转为端点
    protected void quarkusCom2Endpoint() {
        //提取实现容器里 IMcpServerEndpoint 接口的 bean ，并注册为服务端点
        if (serverEndpoints!=null){

            for (IMcpServerEndpoint serverEndpoint : serverEndpoints) {

                Class<?> serverEndpointClz = serverEndpoint.getClass();
                System.out.println("serverEndpoints "+serverEndpointClz.getSimpleName());
                McpServerEndpoint anno = serverEndpointClz.getAnnotation(McpServerEndpoint.class);

                if (anno == null) {
                    continue;
                }

                McpServerEndpointProvider serverEndpointProvider = McpServerEndpointProvider.builder()
                        .from(serverEndpointClz, anno)
                        .build();

                serverEndpointProvider.addTool(new MethodToolProvider(serverEndpointClz, serverEndpoint));
                serverEndpointProvider.addResource(new MethodResourceProvider(serverEndpointClz, serverEndpoint));
                serverEndpointProvider.addPrompt(new MethodPromptProvider(serverEndpointClz, serverEndpoint));

                serverEndpointProvider.postStart();

                //可以再把 serverEndpointProvider 手动转入 SpringBoot 容器
            }
        }

    }
}
