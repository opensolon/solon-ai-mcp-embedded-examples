package webapp.llm;

import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.annotation.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

//聊天模型演示
@RequestMapping("chat")
@RestController
public class ChatController {
    @Autowired
    ChatModel chatModel;

    @Produces(MediaType.TEXT_PLAIN_VALUE)
    @RequestMapping("call")
    public String call(String prompt) throws Exception {
        return chatModel.prompt(prompt).call()
                .getMessage()
                .getContent();
    }

    @Produces(MediaType.TEXT_EVENT_STREAM_VALUE)
    @RequestMapping("stream")
    public Flux<String> stream(String prompt) throws Exception {
        return Flux.from(chatModel.prompt(prompt).stream())
                .map(resp -> resp.getMessage().getContent());
    }
}