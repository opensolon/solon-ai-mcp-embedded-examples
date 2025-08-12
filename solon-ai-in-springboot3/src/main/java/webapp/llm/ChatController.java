package webapp.llm;

import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.annotation.Produces;
import org.noear.solon.core.util.RunUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
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
    public SseEmitter stream(String prompt) throws Exception {
        SseEmitter emitter = new SseEmitter(0L);

        Flux.from(chatModel.prompt(prompt).stream())
                .filter(resp -> resp.hasContent())
                .doOnNext(resp -> {
                    RunUtil.runOrThrow(() -> emitter.send(resp.getContent()));
                })
                .doOnError(err -> {
                    emitter.completeWithError(err);
                })
                .doOnComplete(() -> {
                    emitter.complete();
                })
                .subscribe();

        return emitter;
    }
}