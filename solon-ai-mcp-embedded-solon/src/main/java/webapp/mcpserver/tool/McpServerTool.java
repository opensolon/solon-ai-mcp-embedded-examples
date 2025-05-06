package webapp.mcpserver.tool;

import org.noear.solon.ai.annotation.ToolMapping;
import org.noear.solon.ai.mcp.server.annotation.McpServerEndpoint;
import org.noear.solon.annotation.Param;

/**
 * 自动构建服务端点服务
 * */
@McpServerEndpoint(sseEndpoint = "/mcp/demo1/sse")
public class McpServerTool {
    //
    // 建议开启编译参数：-parameters （否则，最好再配置参数的 name）
    //
    @ToolMapping(description = "查询天气预报")
    public String getWeather(@Param(description = "城市位置") String location) {
        return "晴，14度";
    }
}