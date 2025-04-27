package webapp;

import org.noear.solon.Solon;
import org.noear.solon.ai.chat.tool.MethodToolProvider;
import org.noear.solon.ai.mcp.server.McpServerEndpointProvider;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;
import webapp.mcpserver.tool.McpServerTool2;

@Controller
public class HelloApp {
    public static void main(String[] args) {
        Solon.start(HelloApp.class, args, app -> {
            //手动构建 mcp 服务端点（只是演示，可以去掉）
            McpServerEndpointProvider serverEndpointProvider = McpServerEndpointProvider.builder()
                    .sseEndpoint("/mcp/demo2/sse")
                    .build();
            serverEndpointProvider.addTool(new MethodToolProvider(new McpServerTool2()));

            app.context().lifecycle(serverEndpointProvider);
        });
    }
}
