package webapp.mcpserver;

import com.jfinal.handler.Handler;
import com.jfinal.plugin.IPlugin;
import org.noear.solon.Solon;
import org.noear.solon.ai.chat.tool.MethodToolProvider;
import org.noear.solon.ai.mcp.server.McpServerEndpointProvider;
import org.noear.solon.ai.mcp.server.prompt.MethodPromptProvider;
import org.noear.solon.ai.mcp.server.resource.MethodResourceProvider;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ContextUtil;
import org.noear.solon.web.servlet.SolonServletContext;
import webapp.HelloApp;
import webapp.mcpserver.tool.McpServerTool2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 这个类独立一个目录，可以让 Solon 扫描范围最小化
 * */
public class McpServerConfig extends Handler implements IPlugin {
    public boolean start() {
        // todo: 此处将 source 设为 (HelloApp.class)；从根据扫描，可以使用所有的 solon 能力
        Solon.start(HelloApp.class, new String[]{"--cfg=mcpserver.yml"});

        //手动构建 mcp 服务端点（只是演示，可以去掉）
        McpServerEndpointProvider endpointProvider = McpServerEndpointProvider.builder()
                .name("McpServerTool2")
                .sseEndpoint("/mcp/demo2/sse")
                .build();
        endpointProvider.addTool(new MethodToolProvider(new McpServerTool2()));
        endpointProvider.addResource(new MethodResourceProvider(new McpServerTool2()));
        endpointProvider.addPrompt(new MethodPromptProvider(new McpServerTool2()));
        endpointProvider.postStart();

        //手动加入到 solon 容器（只是演示，可以去掉）
        Solon.context().wrapAndPut(endpointProvider.getName(), endpointProvider);

        return true;
    }

    public boolean stop() {
        if (Solon.app() != null) {
            Solon.stopBlock(false, Solon.cfg().stopDelay());
        }
        return true;
    }

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        //todo: 不通过路径过滤，可接管所有的请求
        Context ctx = new SolonServletContext(request, response);

        try {
            //Solon处理(可能是空处理)
            Solon.app().tryHandle(ctx);

            if (isHandled != null && isHandled.length > 0) {
                isHandled[0] = true;
            }
        } catch (Throwable e) {
            ctx.errors = e;

            throw e;
        } finally {
            ContextUtil.currentRemove();
        }
    }
}
