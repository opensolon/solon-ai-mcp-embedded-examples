# solon-ai-mcp-embedded-examples

这个示例可以使用 springboot 容器的能力，开发 McpServerEndpoint


### 提醒

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


| 示例                                     | 说明                       |
|----------------------------------------|--------------------------|
| solon-ai-embedded-jfinal               |                          |
| solon-ai-embedded-jfinal-newstyle      | 使用了 solon mvc 体系（支持异步响应） |
| solon-ai-embedded-solon                |                          |
| solon-ai-embedded-springboot2          |                          |
| solon-ai-embedded-springboot3          |                          |
| solon-ai-embedded-vertx                |                          |
