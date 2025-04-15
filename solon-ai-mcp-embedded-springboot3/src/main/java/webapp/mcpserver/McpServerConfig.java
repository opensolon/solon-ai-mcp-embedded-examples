package webapp.mcpserver;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.noear.solon.Solon;
import org.noear.solon.web.servlet.SolonServletFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 这个类独立一个目录，可以让 Solon 扫描范围最小化
 * */
@Configuration
public class McpServerConfig {
    @PostConstruct
    public void start() {
        //启动 solon
        Solon.start(McpServerConfig.class, new String[]{"--cfg=mcpserver.yml"});
    }

    @PreDestroy
    public void stop() {
        if (Solon.app() != null) {
            //停止 solon（根据配置，可支持两段式安全停止）
            Solon.stopBlock(false, Solon.cfg().stopDelay());
        }
    }

    @Bean
    public FilterRegistrationBean folkmqAdmin(){
        //通过 Servlet Filter 实现 http 能力对接
        FilterRegistrationBean<SolonServletFilter> filter = new FilterRegistrationBean<>();
        filter.setName("SolonFilter");
        filter.addUrlPatterns("/folkmq/*");
        filter.setFilter(new SolonServletFilter());
        return filter;
    }
}
