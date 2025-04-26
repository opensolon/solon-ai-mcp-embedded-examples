package webapp.mcpserver;

import io.vertx.core.AbstractVerticle;
import org.noear.solon.Solon;
import org.noear.solon.ai.chat.tool.MethodToolProvider;
import org.noear.solon.ai.mcp.server.McpServerEndpointProvider;
import webapp.mcpserver.tool.McpServerTool2;

/**
 * 这个类独立一个目录，可以让 Solon 扫描范围最小化
 * */
public class McpServerConfig extends AbstractVerticle {

    @Override
    public void start() {
        Solon.start(McpServerConfig.class, new String[]{"--cfg=mcpserver.yml"});

        //手动构建 mcp 服务端点
        McpServerEndpointProvider serverEndpointProvider = McpServerEndpointProvider.builder()
                .sseEndpoint("/mcp/demo2/sse")
                .build();
        serverEndpointProvider.addTool(new MethodToolProvider(new McpServerTool2()));
        serverEndpointProvider.postStart();
    }


    @Override
    public void stop() {
        if (Solon.app() != null) {
            Solon.stopBlock(false, Solon.cfg().stopDelay());
        }
    }
}
