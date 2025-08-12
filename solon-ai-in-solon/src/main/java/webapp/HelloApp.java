package webapp;

import org.noear.solon.Solon;
import org.noear.solon.ai.chat.tool.MethodToolProvider;
import org.noear.solon.ai.mcp.McpChannel;
import org.noear.solon.ai.mcp.server.McpServerEndpointProvider;
import org.noear.solon.ai.mcp.server.prompt.MethodPromptProvider;
import org.noear.solon.ai.mcp.server.resource.MethodResourceProvider;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;
import webapp.mcpserver.tool.McpServerTool2;

@Controller
public class HelloApp {
    public static void main(String[] args) {
        Solon.start(HelloApp.class, args, app -> {
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
        });
    }
}
