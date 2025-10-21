package webapp.llm.tool;

import org.noear.solon.ai.annotation.ToolMapping;
import org.noear.solon.annotation.Param;

public class WeatherTools {
    @ToolMapping(description = "查询天气预报")
    public String getWeather(@Param(description = "城市位置") String location) {
        return "晴，14度";
    }
}
