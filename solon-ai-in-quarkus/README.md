#### 第一步 启动服务
```maven 
# -Dquarkus.analytics.disabled=true 的目的是为了启动加速，避免老是提问你是否要参与问卷调查一类的
mvn quarkus:dev -Dquarkus.analytics.disabled=true
```
或者右侧maven有一个启动插件，通过它启动也可以
![img.png](img.png)