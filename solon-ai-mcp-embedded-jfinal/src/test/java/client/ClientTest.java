package client;

import org.noear.solon.ai.mcp.client.McpClientToolProvider;

import java.util.HashMap;
import java.util.Map;

public class ClientTest {
    public static void main(String[] args) throws Exception {
        McpClientToolProvider toolProvider = McpClientToolProvider.builder()
                .apiUrl("http://localhost:8080/mcp/demo1/sse")
                .build();

        Map<String, Object> map = new HashMap<>();
        map.put("location", "杭州");
        String rst = toolProvider.callToolAsText("getWeather", map);
        System.out.println(rst);
        assert "晴，14度".equals(rst);

        /// //////////////////////

        toolProvider = McpClientToolProvider.builder()
                .apiUrl("http://localhost:8080/mcp/demo2/sse")
                .build();

        map = new HashMap<>();
        map.put("location", "杭州");
        rst = toolProvider.callToolAsText("getWeather", map);
        System.out.println(rst);
        assert "晴，14度".equals(rst);
    }
}