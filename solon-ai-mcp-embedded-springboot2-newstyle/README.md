# solon-ai-mcp-embedded-examples


这个示例可以使用 springboot 容器的能力，开发 McpServerEndpoint


### 提醒

* Solon.start 默认会扫描 “源类” 包下面的所有类
    * 即 `@McpServerEndpoint` 注解的类，放在 `mcpserver/tool/` 下面，会被自动扫描
    * 如果没有这种 “包括关系”，要使用 `@Import` 导入注解。具体从 solon 官网了解



### 示例


| 示例                                            | 说明              |              |
|-----------------------------------------------|-----------------|--------------|
| solon-ai-mcp-embedded-jfinal                  |                 |              |
| solon-ai-mcp-embedded-jfinal-port             | mcp-server 独立端口 |              |
| solon-ai-mcp-embedded-solon                   |                 |              |
| solon-ai-mcp-embedded-springboot2             |                 |              |
| solon-ai-mcp-embedded-springboot2-newstyle    |                 | 更适合 sb2 集成   |
| solon-ai-mcp-embedded-springboot2_port        | mcp-server 独立端口 |              |
| solon-ai-mcp-embedded-springboot3             |                 |              |
| solon-ai-mcp-embedded-springboot3-newstyle    |                 | 更适合 sb3 集成   |
| solon-ai-mcp-embedded-springboot3_port        | mcp-server 独立端口 |              |
| solon-ai-mcp-embedded-vertx                   |                 |              |
| solon-ai-mcp-embedded-vertx_port              | mcp-server 独立端口 |              |
