package client;

import org.noear.solon.ai.mcp.client.McpClientToolProvider;

import java.util.HashMap;
import java.util.Map;

public class ClientTest {
    public static void main(String[] args) throws Exception {
        McpClientToolProvider toolProvider = new McpClientToolProvider("http://localhost:8080");

        Map<String, Object> map = new HashMap<>();
        map.put("location", "杭州");
        toolProvider.callToolAsText("getWeather", map);
    }
}
