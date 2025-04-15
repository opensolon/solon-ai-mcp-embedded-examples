package webapp;

import com.jfinal.config.*;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
import webapp.mcpserver.McpServerConfig;

public class HelloApp extends JFinalConfig {

    /**
     * 注意：用于启动的 main 方法可以在任意 java 类中创建，在此仅为方便演示
     *      才将 main 方法放在了 DemoConfig 中
     *
     *      开发项目时，建议新建一个 App.java 或者 Start.java 这样的专用
     *      启动入口类放置用于启动的 main 方法
     */
    public static void main(String[] args) {
        UndertowServer.start(HelloApp.class, 8080, false);
    }

    public void configConstant(Constants me) {
        me.setDevMode(false);
    }

    public void configRoute(Routes me) {

    }

    public void configEngine(Engine me) {}
    public void configPlugin(Plugins me) {
        me.add(folkMqConfig);
    }
    public void configInterceptor(Interceptors me) {}
    public void configHandler(Handlers me) {
        me.add(folkMqConfig);
    }

    private McpServerConfig folkMqConfig = new McpServerConfig();
}
