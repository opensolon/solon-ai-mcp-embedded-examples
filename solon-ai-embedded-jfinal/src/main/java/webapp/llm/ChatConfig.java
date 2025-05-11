package webapp.llm;

import org.noear.solon.ai.chat.ChatModel;

//配置聊天模型
public class ChatConfig {
    private static ChatModel chatModel;

    /**
     * 与大模型集成
     */
    public static ChatModel getChatModel() {
        if (chatModel == null) {
            chatModel = ChatModel.of(_Constants.chat_apiUrl)
                    .provider(_Constants.chat_provider)
                    .model(_Constants.chat_model)
                    .defaultToolsAdd(new ChatTools()) //添加默认工具
                    .build();
        }

        return chatModel;
    }
}
