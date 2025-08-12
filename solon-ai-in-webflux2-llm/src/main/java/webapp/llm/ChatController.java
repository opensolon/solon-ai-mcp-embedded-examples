package webapp.llm;

import org.noear.solon.ai.chat.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

//聊天模型演示
@RequestMapping("chat")
@RestController
public class ChatController {
    @Autowired
    ChatModel chatModel;

    @RequestMapping(value = "call", produces = MediaType.TEXT_PLAIN_VALUE)
    public String call(String prompt) throws Exception {
        return chatModel.prompt(prompt).call()
                .getMessage()
                .getContent();
    }

    @RequestMapping(value = "stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(String prompt) throws Exception {
        return Flux.from(chatModel.prompt(prompt).stream())
                //.subscribeOn(Schedulers.boundedElastic()) //加这个打印效果更好
                .filter(resp -> resp.hasContent())
                .map(resp -> resp.getContent());
    }
}