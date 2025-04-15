package webapp.mcpserver;

import com.jfinal.plugin.IPlugin;
import org.noear.solon.Solon;

/**
 * 这个类独立一个目录，可以让 Solon 扫描范围最小化
 * */
public class McpServerConfig implements IPlugin {
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
}
