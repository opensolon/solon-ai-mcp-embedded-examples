# solon-ai-mcp-embedded-examples

### 提醒

* Solon.start 默认会扫描 “源类” 包下面的所有类（这个示例用根类，作为扫描“源”）
    * 放在 `webapp` 下面，会被自动扫描
    * 如果没有这种 “包括关系”，要使用 `@Import` 导入注解。具体从 solon 官网了解

* 可以直接运行 `test/java/client/McpClientTest` 单测（会自动启动服务端）
* Llm 包下的模型，需要先修改本地的模型配置（不然，会出错）

如果不需要 llm 参考（把整个包删掉，省得配置）


### 说明

示例内容包括：

* llm
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