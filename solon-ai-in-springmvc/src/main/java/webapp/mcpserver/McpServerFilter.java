package webapp.mcpserver;

import org.noear.solon.web.servlet.SolonServletFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

/**
 *
 * @author noear 2025/10/21 created
 *
 */
@WebFilter(filterName = "SolonFilter", urlPatterns = "/mcp/*")
public class McpServerFilter extends SolonServletFilter implements Filter {

}
