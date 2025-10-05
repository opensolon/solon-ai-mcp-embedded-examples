package webapp.web2mcp;

import org.noear.solon.ai.annotation.ToolMapping;
import org.noear.solon.ai.mcp.McpChannel;
import org.noear.solon.ai.mcp.server.annotation.McpServerEndpoint;
import org.noear.solon.annotation.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * webapi 与 mcp-server 共用代码（控制器上，分别加上各自的注解）
 */
@McpServerEndpoint(channel = McpChannel.STREAMABLE, mcpEndpoint = "mcp/web/")
@RequestMapping("web/api")
@RestController
public class WebController {
    @ToolMapping(description = "查询天气预报")
    @RequestMapping("getWeather")
    public String getWeather(@Param(description = "城市") String city) {
        return "晴，14度";
    }
}