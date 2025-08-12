package webapp.mcpserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import org.noear.solon.Solon;
import org.noear.solon.ai.chat.tool.MethodToolProvider;
import org.noear.solon.ai.mcp.McpChannel;
import org.noear.solon.ai.mcp.server.McpServerEndpointProvider;
import org.noear.solon.ai.mcp.server.prompt.MethodPromptProvider;
import org.noear.solon.ai.mcp.server.resource.MethodResourceProvider;
import org.noear.solon.web.vertx.VxWebHandler;
import webapp.mcpserver.tool.McpServerTool2;

/**
 * 这个类独立一个目录，可以让 Solon 扫描范围最小化
 * */
public class McpServerConfig extends AbstractVerticle {
    private final Router router;
    private final VxWebHandler handler;

    public McpServerConfig(Router router) {
        this.router = router;
        this.handler = new VxWebHandler();
    }

    @Override
    public void start() {
        router.routeWithRegex("/mcp/.*").handler(req -> {
            handler.handle(req.request());
        });

        Solon.start(McpServerConfig.class, new String[]{"--cfg=mcpserver.yml"});

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
    }

    @Override
    public void stop() {
        if (Solon.app() != null) {
            Solon.stopBlock(false, Solon.cfg().stopDelay());
        }
    }
}
