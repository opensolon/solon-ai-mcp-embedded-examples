package webapp.mcpserver;

import org.noear.solon.Solon;
import org.noear.solon.ai.chat.tool.MethodToolProvider;
import org.noear.solon.ai.mcp.server.McpServerEndpointProvider;
import org.noear.solon.ai.mcp.server.annotation.McpServerEndpoint;
import org.noear.solon.ai.mcp.server.prompt.MethodPromptProvider;
import org.noear.solon.ai.mcp.server.resource.MethodResourceProvider;
import org.noear.solon.web.servlet.SolonServletFilter;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import webapp.mcpserver.tool.McpServerTool2;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * 这个类独立一个目录，可以让 Solon 扫描范围最小化
 * */
@Configuration
public class McpServerConfig {
    @PostConstruct
    public void start() {
        Solon.start(McpServerConfig.class, new String[]{"--cfg=mcpserver.yml"});

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
    }

    @PreDestroy
    public void stop() {
        if (Solon.app() != null) {
            Solon.stopBlock(false, Solon.cfg().stopDelay());
        }
    }

    @Bean
    public McpServerConfig init(List<IMcpServerEndpoint> serverEndpoints) {
        //提取实现容器里 IMcpServerEndpoint 接口的 bean ，并注册为服务端点
        for (IMcpServerEndpoint serverEndpoint : serverEndpoints) {
            Class<?> serverEndpointClz = AopUtils.getTargetClass(serverEndpoint);
            McpServerEndpoint anno = AnnotationUtils.findAnnotation(serverEndpointClz, McpServerEndpoint.class);

            if (anno == null) {
                continue;
            }

            McpServerEndpointProvider serverEndpointProvider = McpServerEndpointProvider.builder()
                    .from(serverEndpointClz, anno)
                    .build();

            serverEndpointProvider.addTool(new MethodToolProvider(serverEndpointClz, serverEndpoint));
            serverEndpointProvider.addResource(new MethodResourceProvider(serverEndpointClz, serverEndpoint));
            serverEndpointProvider.addPrompt(new MethodPromptProvider(serverEndpointClz, serverEndpoint));

            serverEndpointProvider.postStart();

            //可以再把 serverEndpointProvider 手动转入 SpringBoot 容器
        }

        //为了能让这个 init 能正常运行
        return this;
    }

    @Bean
    public FilterRegistrationBean mcpServerFilter() {
        FilterRegistrationBean<SolonServletFilter> filter = new FilterRegistrationBean<>();
        filter.setName("SolonFilter");
        filter.addUrlPatterns("/mcp/*");
        filter.setFilter(new SolonServletFilter());
        return filter;
    }
}