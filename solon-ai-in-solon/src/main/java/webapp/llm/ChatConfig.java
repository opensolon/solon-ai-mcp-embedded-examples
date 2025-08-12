package webapp.llm;

import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import webapp.llm.tool.WeatherTools;

//配置聊天模型
@Configuration
public class ChatConfig {
    /**
     * 与大模型集成
     */
    @Bean
    public ChatModel chatModel() throws Exception {
        return ChatModel.of(_Constants.chat_apiUrl)
                .provider(_Constants.chat_provider)
                .model(_Constants.chat_model)
                .defaultToolsAdd(new WeatherTools()) //添加默认工具
                .build();
    }
}
