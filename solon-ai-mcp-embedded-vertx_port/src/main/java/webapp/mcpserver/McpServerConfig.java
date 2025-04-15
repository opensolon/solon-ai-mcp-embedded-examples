package webapp.mcpserver;

import io.vertx.core.AbstractVerticle;
import org.noear.solon.Solon;

/**
 * 这个类独立一个目录，可以让 Solon 扫描范围最小化
 * */
public class McpServerConfig extends AbstractVerticle {

    @Override
    public void start() {
        Solon.start(McpServerConfig.class, new String[]{"--cfg=mcpserver.yml"});
    }


    @Override
    public void stop() {
        if (Solon.app() != null) {
            Solon.stopBlock(false, Solon.cfg().stopDelay());
        }
    }
}
