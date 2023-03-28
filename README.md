## 简介

`multi-tenancy-datasource-spring-boot-starter` 是一个基于`springboot`的多租户的开发神器.包含了多租户的多种业务封装。

## 特性

- 数据库隔离模型: 基于`Postgresql`的 `schema` 级别隔离
- Redis隔离模型: 为每个key增加租户前缀
- 租户标识解析: 从`Cookie`, `Header`, `Domain`,`URL Path`,`Env`中解析租户标识
- 租户标识传播: 支持`Seata`, `Feign`, `RestTemplate`
- 数据源: 多个租户复用数据库`Connection`减少资源消耗,也可使用独立数据源
- 租户业务执行器: 设置和释放当前线程的租户上下文
- 动态注册租户: 基于`Redis Stream`在运行期注册租户
- baseline jdk17. support Graalvm.

```
--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.invoke=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.text=ALL-UNNAMED --add-opens java.desktop/java.awt.font=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED
```

## Redis

- 为每个租户增加不同的key前缀来做数据隔离

## DB

![architecture.png](architecture.png)

## 使用方式

- Maven

```
<dependency>
    <groupId>cc.sofast.infrastructure</groupId>
    <artifactId>multi-tenancy-datasource-spring-boot-starter</artifactId>
    <version>{lastverion}</version>
</dependency>
```

- Gradle

```
implementation 'cc.sofast.infrastructure:multi-tenancy-datasource-spring-boot-starter:{lastverion}'
```
