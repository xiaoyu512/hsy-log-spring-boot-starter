# hsy-log-spring-boot-starter

> 日志打印以及日志推送消息中间件  
> 如果对你有帮助还请点个 **Star**

1. 编译install之后,引入依赖

```xml
<dependency>
    <groupId>com.hsy</groupId>
    <artifactId>hsy-log-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

2. 如使用中间件需添加相应依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
    <optional>true</optional>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <optional>true</optional>
</dependency>
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
    <optional>true</optional>
</dependency>
```

3. 在配置文件中配置中间件信息,使用什么添加什么配置，默认打印到日志文件,使用中间件默认不打印到日志文件,如需打印到日志文件，需配置hsy.log.console为true

```yaml
spring:
  redis: # redis配置
    host: redis
    port: 6379
    password: 123456
  rabbitmq: # rabbitmq配置
    host: rabbitmq
    port: 5672
    username: admin
    password: 123456
    publisher-returns: true
    publisher-confirm-type: correlated
    listener:
      type: simple
      simple:
        acknowledge-mode: manual
  kafka: # kafka配置
    bootstrapServers: kafka:9092
    producer:
      acks: 1
      retries: 0
      client-id: ${spring.application.name}
    consumer:
      groupId: test
      client-id: ${spring.application.name}
      fetchMaxWait: 6000
      enableAutoCommit: false
      autoOffsetReset: latest
    listener:
      ack-mode: manual
      pollTimeout: 1500
      missing-topics-fatal: false
hsy: # 日志配置,不配置使用默认
  log:
    type: redis|rabbit|kafka # 使用中间件类型
    console: false|true # 是否输出到控制台及文件
    redis: # redis配置
      key: log-test # redis的key
      max-count: 2 # 缓存最大条数
    rabbit: # rabbitmq配置
      exchange: exchange.log # 交换机
      queue: queue.log # 队列
      routing-key: routing.log # 路由
      headers: # 消息header配置
        log: log-test
    kafka: # kafka配置
      topic: log # 主题
      key: log # 消息key
      headers: # 消息header配置
        log: log-test
```

4. 然后在启动类添加`@EnableLog`注解即可启用日志自动配置
5. 然后就可以在需要打印的方法上使用日志注解
   > 5.1 耗时注解,添加描述即可
   > ```java
   > @LogTime("登录")
   > ```
   > 5.1 日志注解,value:描述 input:是否打印输入参数 output:是否打印输出参数 logType:日志类型
   > ```java
   > @Log(value = "登录", input = true, output = true, logType = LogType.SELECT)
   > ```
