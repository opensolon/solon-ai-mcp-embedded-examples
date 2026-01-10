package webapp.agent;

import org.noear.solon.ai.agent.AgentSession;
import org.noear.solon.ai.agent.AgentSessionProvider;
import org.noear.solon.ai.agent.react.ReActAgent;
import org.noear.solon.ai.agent.session.InMemoryAgentSession;
import webapp.llm.ChatConfig;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author noear 2026/1/10 created
 *
 */
public class AgentConfig {
    private static ReActAgent agent;
    private static AgentSessionProvider sessionProvider;

    public static ReActAgent getAgent() {
        if (agent == null) {
            agent = ReActAgent.of(ChatConfig.getChatModel())
                    .build();
        }

        return agent;
    }

    public static AgentSessionProvider getSessionProvider() {
        if (sessionProvider == null) {
            Map<String, AgentSession> map = new LinkedHashMap<>();
            sessionProvider = (sessionId) -> map.computeIfAbsent(sessionId,
                    k -> InMemoryAgentSession.of(k));
        }

        return sessionProvider;
    }
}
