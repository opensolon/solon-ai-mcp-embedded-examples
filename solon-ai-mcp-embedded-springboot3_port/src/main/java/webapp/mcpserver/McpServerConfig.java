package webapp.mcpserver;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.noear.solon.Solon;
import org.noear.solon.ai.chat.tool.MethodToolProvider;
import org.noear.solon.ai.mcp.server.McpServerEndpointProvider;
import org.springframework.context.annotation.Configuration;
import webapp.mcpserver.tool.McpServerTool2;

/**
 * 这个类独立一个目录，可以让 Solon 扫描范围最小化
 * */
@Configuration
public class McpServerConfig {
    @PostConstruct
    public void start() {
        Solon.start(McpServerConfig.class, new String[]{"--cfg=mcpserver.yml"});

        //手动构建 mcp 服务端点（只是演示，可以去掉）
        McpServerEndpointProvider serverEndpointProvider = McpServerEndpointProvider.builder()
                .sseEndpoint("/mcp/demo2/sse")
                .build();
        serverEndpointProvider.addTool(new MethodToolProvider(new McpServerTool2()));
        serverEndpointProvider.postStart();
    }

    @PreDestroy
    public void stop() {
        if (Solon.app() != null) {
            Solon.stopBlock(false, Solon.cfg().stopDelay());
        }
    }
}
