package webapp.agent;

import org.noear.solon.ai.agent.AgentSessionProvider;
import org.noear.solon.ai.agent.react.ReActAgent;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

@Mapping("/agent")
@Controller
public class AgentController {
    @Inject
    ReActAgent agent;

    @Inject
    AgentSessionProvider sessionProvider;

    @Mapping("call")
    public String call(String sessionId, String query) throws Throwable {
        return agent.prompt(query)
                .session(sessionProvider.getSession(sessionId))
                .call()
                .getContent();
    }
}
