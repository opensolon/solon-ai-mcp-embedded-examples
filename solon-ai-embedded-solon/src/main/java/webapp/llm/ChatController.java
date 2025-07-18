package webapp.llm;

import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Produces;
import org.noear.solon.core.util.MimeType;
import reactor.core.publisher.Flux;

//聊天模型演示
@Mapping("chat")
@Controller
public class ChatController {
    @Inject
    ChatModel chatModel;

    @Produces(MimeType.TEXT_PLAIN_VALUE)
    @Mapping("call")
    public String call(String prompt) throws Exception {
        return chatModel.prompt(prompt).call()
                .getMessage()
                .getContent();
    }

    @Produces(MimeType.TEXT_EVENT_STREAM_VALUE)
    @Mapping("stream")
    public Flux<String> stream(String prompt) throws Exception {
        return Flux.from(chatModel.prompt(prompt).stream())
                .filter(resp -> resp.hasContent())
                .map(resp -> resp.getContent());
    }
}