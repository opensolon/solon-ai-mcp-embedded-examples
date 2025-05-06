package webapp.mcpserver;

import com.jfinal.plugin.IPlugin;
import org.noear.solon.Solon;
import org.noear.solon.ai.chat.tool.MethodToolProvider;
import org.noear.solon.ai.mcp.server.McpServerEndpointProvider;
import org.noear.solon.ai.mcp.server.prompt.MethodPromptProvider;
import org.noear.solon.ai.mcp.server.resource.MethodResourceProvider;
import webapp.mcpserver.tool.McpServerTool2;

/**
 * 这个类独立一个目录，可以让 Solon 扫描范围最小化
 * */
public class McpServerConfig implements IPlugin {
    public boolean start() {
        Solon.start(McpServerConfig.class, new String[]{"--cfg=mcpserver.yml"});

        //手动构建 mcp 服务端点（只是演示，可以去掉）
        McpServerEndpointProvider serverEndpointProvider = McpServerEndpointProvider.builder()
                .sseEndpoint("/mcp/demo2/sse")
                .build();
        serverEndpointProvider.addTool(new MethodToolProvider(new McpServerTool2()));
        serverEndpointProvider.addResource(new MethodResourceProvider(new McpServerTool2()));
        serverEndpointProvider.addPrompt(new MethodPromptProvider(new McpServerTool2()));
        serverEndpointProvider.postStart();

        return true;
    }

    public boolean stop() {
        if (Solon.app() != null) {
            Solon.stopBlock(false, Solon.cfg().stopDelay());
        }
        return true;
    }
}
