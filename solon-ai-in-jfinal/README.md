# solon-ai-mcp-embedded-examples

### 提醒

* Solon.start 默认会扫描 “源类” 包下面的所有类
    * 即 `@McpServerEndpoint` 注解的类，放在 `mcpserver/tool/` 下面，会被自动扫描
    * 如果没有这种 “包括关系”，要使用 `@Import` 导入注解。具体从 solon 官网了解

* 可以直接运行 `test/java/client/McpClientTest` 单测（会自动启动服务端）
* 运运行 Llm 单测，需要先修改本地的模型配置（不然，会出错）



### 说明

示例内容包括：

* llm（需要修改 llm/_Constants 下的常量配置，否则启动会出错）
  * chatModel 基本使用，及单测
  * tool call 基本使用，及单测
  * rag 基本使用，及单测
    * embeddingModel
    * repository
    * splitter
* mcp
  * server 基本使用（需要 solon 容器支持）
  * client 基本使用，及单测


### 示例

| 示例                          | 说明                       |
|-----------------------------|--------------------------|
| solon-ai-in-jfinal          |                          |
| solon-ai-in-jfinal-newstyle | 使用了 solon mvc 体系（支持异步响应） |
| solon-ai-in-solon           |                          |
| solon-ai-in-springboot2     |                          |
| solon-ai-in-springboot3     |                          |
| solon-ai-in-vertx           |                          |
|                             |                          | 
| solon-ai-in-webflux2-llm    | 只有 llm 示例（没有 mcp）        |
| solon-ai-in-webflux3-llm    | 只有 llm 示例（没有 mcp）        |