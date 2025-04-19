package client;

import org.noear.solon.ai.mcp.client.McpClientToolProvider;

import java.util.HashMap;
import java.util.Map;

public class ClientTest {
    public static void main(String[] args) throws Exception {
        McpClientToolProvider toolProvider = McpClientToolProvider.builder()
                .apiUrl("http://localhost:8602/mcp/sse")
                .build();

        Map<String, Object> map = new HashMap<>();
        map.put("location", "杭州");
        String rst = toolProvider.callToolAsText("getWeather", map);
        System.out.println(rst);
    }
}
