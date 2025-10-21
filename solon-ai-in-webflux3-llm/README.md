
### test

http://localhost:8080/chat/call?prompt=hello

http://localhost:8080/chat/stream?prompt=hello



### 提醒


* 可以直接运行 `test/java/client/McpClientTest` 单测（会自动启动服务端）
* Llm 包下的模型，需要先修改本地的模型配置（不然，会出错）


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