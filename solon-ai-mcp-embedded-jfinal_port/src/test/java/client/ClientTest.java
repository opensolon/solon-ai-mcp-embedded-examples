package client;

import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.ai.chat.message.ChatMessage;
import org.noear.solon.ai.mcp.client.McpClientToolProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientTest {
    public static void main(String[] args) throws Exception {
        McpClientToolProvider toolProvider = McpClientToolProvider.builder()
                .apiUrl("http://localhost:8602/mcp/demo1/sse")
                .build();

        //工具
        Map<String, Object> map = Collections.singletonMap("location", "杭州");
        String rst = toolProvider.callToolAsText("getWeather", map).getContent();
        System.out.println(rst);
        assert "晴，14度".equals(rst);

        //提示语
        List<ChatMessage> messageList = toolProvider.getPromptAsMessages("askQuestion", Collections.singletonMap("topic", "demo"));
        System.out.println(messageList);

        //资源
        String resourceContent = toolProvider.readResourceAsText("config://app-version").getContent();
        System.out.println(resourceContent);

        System.out.println("---------------");

        /// ////////////////////

        toolProvider = McpClientToolProvider.builder()
                .apiUrl("http://localhost:8602/mcp/demo2/sse")
                .build();

        //工具
        map = Collections.singletonMap("location", "杭州");
        rst = toolProvider.callToolAsText("getWeather", map).getContent();
        System.out.println(rst);
        assert "晴，14度".equals(rst);

        //提示语
        messageList = toolProvider.getPromptAsMessages("askQuestion", Collections.singletonMap("topic", "demo"));
        System.out.println(messageList);

        //资源
        resourceContent = toolProvider.readResourceAsText("config://app-version").getContent();
        System.out.println(resourceContent);
    }

    public void demo(McpClientToolProvider toolProvider) throws Exception {
        ChatModel chatModel = ChatModel.of("...")
                .defaultToolsAdd(toolProvider) //添加默认工具
                .build();

        chatModel.prompt("杭州今天的天气怎么样？")
                .call();
    }
}