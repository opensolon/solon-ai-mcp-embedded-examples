package webapp;

import com.jfinal.config.*;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
import org.noear.solon.Solon;
import webapp.llm.ChatController;
import webapp.llm.RagController;
import webapp.mcpserver.McpServerConfig;

public class HelloApp extends JFinalConfig {

    /**
     * 注意：用于启动的 main 方法可以在任意 java 类中创建，在此仅为方便演示
     * 才将 main 方法放在了 DemoConfig 中
     * <p>
     * 开发项目时，建议新建一个 App.java 或者 Start.java 这样的专用
     * 启动入口类放置用于启动的 main 方法
     */
    public static void main(String[] args) {
        if(Solon.app() != null) {
            return;
        }

        UndertowServer.create(HelloApp.class)
                .setDevMode(false)
                .setPort(8080)
                .onDeploy((cl, di) -> {
                    di.getFilters().get("jfinal").setAsyncSupported(true);
                }).start();
    }

    public void configConstant(Constants me) {
        me.setDevMode(false);
    }

    public void configRoute(Routes me) {
        me.add("/chat", ChatController.class);
        me.add("/rag", RagController.class);
    }

    public void configEngine(Engine me) {
    }

    public void configPlugin(Plugins me) {
        me.add(mcpServerConfig);
    }

    public void configInterceptor(Interceptors me) {
    }

    public void configHandler(Handlers me) {
        me.add(mcpServerConfig);
    }

    private McpServerConfig mcpServerConfig = new McpServerConfig();
}
