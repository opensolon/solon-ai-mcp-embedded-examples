package webapp.agent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.noear.solon.ai.agent.AgentSession;
import org.noear.solon.ai.agent.AgentSessionProvider;
import org.noear.solon.ai.agent.react.ReActAgent;
import org.noear.solon.ai.agent.session.InMemoryAgentSession;
import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@ApplicationScoped
public class AgentConfig {

    @Produces
    public ReActAgent getAgent(ChatModel chatModel) {
        return ReActAgent.of(chatModel)
                .build();
    }

    @Produces
    public AgentSessionProvider getSessionProvider() {
        Map<String, AgentSession> map = new LinkedHashMap<>();
        return (sessionId) -> map.computeIfAbsent(sessionId,
                k -> InMemoryAgentSession.of(k));
    }
}
