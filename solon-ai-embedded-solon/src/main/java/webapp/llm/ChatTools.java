package webapp.llm;

import org.noear.solon.ai.annotation.ToolMapping;
import org.noear.solon.annotation.Param;

//定义聊天工具（如果有需要？）
public class ChatTools {
    @ToolMapping(description = "查询天气预报")
    public String getWeather(@Param(description = "城市位置") String location) {
        return "晴，14度";
    }
}
