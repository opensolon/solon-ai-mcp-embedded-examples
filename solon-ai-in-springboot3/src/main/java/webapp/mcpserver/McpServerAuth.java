package webapp.mcpserver;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;

/**
 * 仅为示例（也可以用 Servlet 过滤器）
 */
public class McpServerAuth implements Filter {
    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        //如何鉴权“按需”设定（path 的过滤，与端点路径对应起来）
        if (ctx.pathNew().startsWith("/mcp/")
                && ctx.pathNew().endsWith("/message") == false) { //message 端点不需要签权
            String authStr = ctx.param("user"); //或者 ctx.header(...)
            if ("no".equals(authStr)) { //模拟 401 效果
                ctx.status(401);
                ctx.setHandled(true);
                return;
            }

            //业务检测
        }

        chain.doFilter(ctx);
    }
}
