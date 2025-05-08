package client;

import org.junit.jupiter.api.Test;
import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.ai.chat.ChatResponse;
import org.noear.solon.ai.chat.message.ChatMessage;
import org.noear.solon.ai.mcp.client.McpClientProvider;
import org.noear.solon.test.SolonTest;
import webapp.HelloApp;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@SolonTest(HelloApp.class)
public class McpClientTest {
    /**
     * 工具直接调用
     */
    @Test
    public void case1() throws Exception {
        McpClientProvider toolProvider = McpClientProvider.builder()
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

        /// /////////////////


        toolProvider = McpClientProvider.builder()
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

    //换成自己的模型配置（参考：https://solon.noear.org/article/918）
    private static final String apiUrl = "http://127.0.0.1:11434/api/chat";
    private static final String provider = "ollama";
    private static final String model = "qwen2.5:1.5b"; //"llama3.2";//deepseek-r1:1.5b;

    /**
     * 与大模型集成
     */
    @Test
    public void case2() throws Exception {
        McpClientProvider toolProvider = McpClientProvider.builder()
                .apiUrl("http://localhost:8602/mcp/demo1/sse")
                .build();

        ChatModel chatModel = ChatModel.of(apiUrl)
                .provider(provider)
                .model(model)
                .defaultToolsAdd(toolProvider) //添加默认工具
                .build();

        ChatResponse resp = chatModel.prompt("杭州今天的天气怎么样？")
                .call();

        System.out.println(resp.getMessage());
    }
}