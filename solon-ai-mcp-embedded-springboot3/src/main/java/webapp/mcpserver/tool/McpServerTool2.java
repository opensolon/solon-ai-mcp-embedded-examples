package webapp.mcpserver.tool;

import org.noear.solon.ai.annotation.ToolMapping;
import org.noear.solon.annotation.Param;

/**
 * 用于手动构建
 * */
public class McpServerTool2 {
    //
    // 建议开启编译参数：-parameters （否则，最好再配置参数的 name）
    //
    @ToolMapping(description = "查询天气预报")
    public String getWeather(@Param(description = "城市位置") String location) {
        return "晴，14度";
    }
}