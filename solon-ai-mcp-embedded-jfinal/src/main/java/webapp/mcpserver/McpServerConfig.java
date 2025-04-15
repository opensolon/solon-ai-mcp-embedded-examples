package webapp.mcpserver;

import com.jfinal.handler.Handler;
import com.jfinal.plugin.IPlugin;
import org.noear.solon.Solon;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ContextUtil;
import org.noear.solon.web.servlet.SolonServletContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 这个类独立一个目录，可以让 Solon 扫描范围最小化
 * */
public class McpServerConfig extends Handler implements IPlugin {
    public boolean start() {
        Solon.start(McpServerConfig.class, new String[]{"--cfg=mcpserver.yml"});
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
        if (target.startsWith("/folkmq/")) {
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
        } else {
            if (next != null) {
                next.handle(target, request, response, isHandled);
            }
        }
    }
}
