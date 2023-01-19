# behappy-hospital(尚医通-改)

> 所有文档/sql在doc目录下

### 介绍

- 背景

本项目是在尚硅谷-尚医通项目基础上改造, 整合sentinel, xxl, boot-admin等技术, 完善了业务代码, 修改了许多原项目存在的bug

- 目的

```
我很喜欢b站的一位up主`不高兴就喝水`, 他举过一个例子
我们平时在街头上来来往往会遇到很多人, 但回到家会发现对任何一个人都没有印象
但是如果漫雪纷飞的街头上, 你吃着关东煮, 被一6尺7寸的老大哥撞了下, 关东煮撒了, 签子把你的羽绒服戳穿了个大洞, 白雪皑皑, 让你分不清天上下的是雪还是绒毛
估计这一遭, 能让你对这老大哥记一辈子
所以我的目的, 就是想可以借着这个实战项目持续输出一些技术/代码能力, 以此加深/巩固自己对这些技术/代码能力的认识
```

### TODO

1. swagger3 -> open api
2. xxljob 排查

### 技术栈

alibaba,cloud,springboot,最佳实践版本:https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E

**核心依赖**

jdk: 17
nodejs: 10.24.1


| 依赖                 | 版本         |
| -------------------- |------------|
| Spring Boot          | 3.0.1      |
| Spring Cloud         | 2022.0.0   |
| Spring Cloud Alibaba | 2022.0.0.0-RC1 |
| Mybatis Plus         | 3.5.3.1    |
| Swagger              | 3.0.0      |
| behappy redis        | 3.0.0      |
| wxpay-sdk            | 0.0.3      |
| Hutool               | 5.8.11      |
| xxl job              | 2.3.1      |
| druid                | 1.2.15      |

### 配置章节

> 以下标注todo的都是需要留意进行配置的,实际上如果仅是希望项目能跑起来，只需要改下组件连接信息即可【mysql/redis/nacos。。。】
> 推荐使用`https://github.com/behappy-hospital/behappy-docker-application`仓库下的组件，版本以及配置是经我测试过的，然后仅需修改下面配置中的192.168.56.103为你的ip即可

#### 统一配置 - boostrap.yml
```yaml
spring:
  profiles:
    active: @profiles.active@
  application:
    name: @project.artifactId@
  cloud:
    nacos:
      # todo 这里是nacos统一配置地址，之后配置中心和注册中心都会使用此属性
      server-addr: 192.168.56.103:8848
      #      config:
      #        file-extension: yml
      # 如果需要环境隔离的话可以另行配置namespace
      #        namespace: 96d736a4-1fa4-407a-af7a-f5e9009c5075
#      discovery:
#        namespace: d83c21cc-2614-41ea-ad2b-17a4ef3d596f
#        group: behappy
      # todo nacos账户名密码，默认
      username: nacos
      password: nacos
```

#### behappy-cmn
> 以下是主要介绍的配置，开发时以实际代码内的为准

```yaml
server:
  # 除了微信回调用的8160端口不能改，其他的可自行更改，以下不再赘述
  port: 8202
spring:
  cloud:
    # todo openfeign 4.0配置改动
    openfeign:
      okhttp:
        enabled: false
      httpclient:
        enabled: true
        max-connections: 200 # 最大的连接数
        max-connections-per-route: 50 # 每个路径的最大连接数
      client:
        config:
          default: # default全局的配置
            loggerLevel: basic # 日志级别，BASIC就是基本的请求和响应信息
            connectTimeout: 10000
            readTimeout: 10000
      compression:
        request:
          enabled: true
        response:
          enabled: true
      # https://github.com/alibaba/spring-cloud-alibaba/issues/3024
      lazy-attributes-resolution: true
  # 使用redisson作为redis客户端框架，所有配置都可以参照官方，以下不再赘述
  redis:
    redisson:
      config: |-
        # 例: 单节点设置
        singleServerConfig:
          # todo（节点地址）redis://-非ssl,rediss://-ssl
          address: redis://192.168.56.103:6379
          database: 0
          password: null
          ......
        # 默认是Kryo序列化模式，修改成jackson
        codec: !<org.redisson.codec.JsonJacksonCodec> {}
  mvc:
    # 此配置是用来解决swagger3在springboot2.6之后的冲突问题（2.6之后使用PathPattern代替AntPathMatcher）
    pathmatch:
      matching-strategy: ANT_PATH_MATCHER
  datasource:
    # todo mysql配置
    driver-class-name: com.mysql.cj.jdbc.Driver
    password: root
    username: root
    url: jdbc:mysql://192.168.56.103:3306/yygh_cmn?allowMultiQueries=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
    druid:
      aop-patterns: org.xiaowu.behappy.*  #监控SpringBean
      filters: stat,wall     # 底层开启功能，stat（sql监控），wall（防火墙）
      stat-view-servlet:   # 配置监控页功能
        enabled: true
        login-username: admin
        login-password: admin
        resetEnable: false
      web-stat-filter:  # 监控web
        enabled: true
        urlPattern: /*
        exclusions: '*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*'
      filter:
        stat:    # 对上面filters里面的stat的详细配置
          slow-sql-millis: 1000
          logSlowSql: true
          enabled: true
        wall:
          enabled: true
          config:
            drop-table-allow: false
# mybatis-plus配置
mybatis-plus:
  mapper-locations: classpath:/mapper/*.xml
  global-config:
    banner: false
    db-config:
      table-underline: true
      logic-delete-value: 1
      logic-not-delete-value: 0
  configuration:
    map-underscore-to-camel-case: true
    # sql日志
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
# behappy-redis,我封了下redisson,如无特殊需求，可不进行配置
behappy:
  redis:
    # 控制台启动时是否打印banner
    banner-shown: true
    # 使用spring的CacheManager
    cache-manager:
      # 统一key前缀，如BEHAPPY:test:1
      prefix: BEHAPPY
      configs:
        - key: test
          # 当前key的ttl
          ttl: 0
          maxIdleTime: 0
          maxSize: 0
thread:
  pool:
    # 线程名称前缀
    name-prefix: @project.artifactId@
# feign 使用httpclient，开启sentinel以及压缩
feign:
  sentinel:
    enabled: true
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: ALWAYS
```

#### behappy-executor-task
> 以下是主要介绍的配置，开发时以实际代码内的为准

```yaml
server:
  port: 8208
spring:
  mvc:
    pathmatch:
      matching-strategy: ANT_PATH_MATCHER
  # todo rabbitmq配置信息，需修改host，port，以及账户密码
  rabbitmq:
    host: 192.168.56.103
    port: 5672
    # 指定虚拟主机
    virtual-host: /
    username: root
    password: root
    # 开启发送端确认
    publisher-confirm-type: simple
    #　开启发送端消息抵达队列确认
    publisher-returns: true
    # 只要抵达队列，以异步优先回调我们这个 return/confirm
    template:
      mandatory: true
    # 手动ack消息
    listener:
      simple:
        acknowledge-mode: manual
# todo 连接xxljob admin的信息，本地启动则不需要修改以下信息
xxl:
  job:
    accessToken: default_token
    admin:
      addresses: http://192.168.56.103:8080/xxl-job-admin
    executor:
      ip: ''
      address: ''
      port: 9999
      appname: @project.artifactId@
      logpath: /data/applogs/xxl-job/jobhandler
      logretentiondays: 7
```

#### behappy-gateway
> 以下是主要介绍的配置，开发时以实际代码内的为准

```yaml
# 网关不需要更改，除非你希望更改访问方式
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
      routes:
        - id: behappy-hosp
          uri: lb://behappy-hosp
          predicates:
            - Path=/*/hosp/**
#          filters:
#            - RewritePath=/api/(?<segment>.*),/$\{segment}
        - id: behappy-cmn
          uri: lb://behappy-cmn
          predicates:
            - Path=/*/cmn/**
        - id: behappy-user
          uri: lb://behappy-user
          predicates:
            - Path=/*/user/**
        - id: behappy-user
          uri: lb://behappy-user
          predicates:
            - Path=/*/ucenter/**
        - id: behappy-msm
          uri: lb://behappy-msm
          predicates:
            - Path=/*/msm/**
        - id: behappy-oss
          uri: lb://behappy-oss
          predicates:
            - Path=/*/oss/**
        - id: behappy-order
          uri: lb://behappy-order
          predicates:
            - Path=/*/order/**
```

#### behappy-hosp
> 以下是主要介绍的配置，开发时以实际代码内的为准

```yaml
# todo
spring:
  rabbitmq:
    host: 192.168.56.103
    port: 5672
    # 指定虚拟主机
    virtual-host: /
    username: root
    password: root
    # 开启发送端确认
    publisher-confirm-type: simple
    #　开启发送端消息抵达队列确认
    publisher-returns: true
    # 只要抵达队列，以异步优先回调我们这个 return/confirm
    template:
      mandatory: true
    # 手动ack消息 消息不容易丢失
    listener:
      simple:
        acknowledge-mode: manual
  mvc:
    pathmatch:
      matching-strategy: ANT_PATH_MATCHER
  # todo
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    password: root
    username: root
    url: jdbc:mysql://192.168.56.103:3306/yygh_hosp?allowMultiQueries=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
  # todo mongo配置信息，yygh_hosp库我在behappy-docker-application项目下提供mongo镜像已经初始化好了，所以如果你使用的是我提供的镜像mongo信息不需要更改
  data:
    #uri: mongodb://用户名:密码@服务器IP:端口/数据库名
    # 上方为明确指定某个数据的用户进行连接
    # 也可以使用admin 数据库中的用户进行连接  统一到admin 数据库进行认证
    # admin 用户认证 url 写法： mongodb://账户:密码@ip:端口/数据库名?authSource=admin&authMechanism=SCRAM-SHA-1
    mongodb:
      uri: mongodb://root:root@192.168.56.103:27017/yygh_hosp?authSource=admin&authMechanism=SCRAM-SHA-1
```

#### behappy-manager
> 以下是主要介绍的配置，开发时以实际代码内的为准

```yaml
spring:
  # todo
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.56.103:3306/yygh_manage?characterEncoding=utf-8&useSSL=false
    username: root
    password: root
```

#### behappy-monitor
> 以下是主要介绍的配置，开发时以实际代码内的为准

```yaml
server:
  port: 8203
# todo 如果不要修改monitor初始账户密码，则不需要更改此处配置
spring:
  security:
    user:
      name: root
      password: root
```

#### behappy-msm
> 以下是主要介绍的配置，开发时以实际代码内的为准

```yaml
spring:
  redis:
    # todo redis配置信息
    redisson:
      ......
  # rabbitmq配置
  rabbitmq:
    host: 192.168.56.103
    port: 5672
    # 指定虚拟主机
    virtual-host: /
    username: root
    password: root
# todo 阿里云sms配置
aliyun:
  sms:
    access-key-id:
    access-key-secret:
    sign-name: ABC商城
    template-code: SMS_206546316
```

#### behappy-order
> 以下是主要介绍的配置，开发时以实际代码内的为准

```yaml
spring:
  # todo redis配置
  redis:
    redisson:
      config: |-
        # 例: 单节点设置
        singleServerConfig:
          # （节点地址）redis://-非ssl,rediss://-ssl
          address: redis://192.168.56.103:6379
          database: 0
          password: null
  # todo rabbitmq配置
  rabbitmq:
    host: 192.168.56.103
    port: 5672
    # 指定虚拟主机
    virtual-host: /
    username: root
    password: root
  # todo mysql配置
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    password: root
    username: root
    url: jdbc:mysql://192.168.56.103:3306/yygh_order?allowMultiQueries=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
  # todo mongo配置
  data:
    #uri: mongodb://用户名:密码@服务器IP:端口/数据库名
    # 上方为明确指定某个数据的用户进行连接
    # 也可以使用admin 数据库中的用户进行连接  统一到admin 数据库进行认证
    # admin 用户认证 url 写法： mongodb://账户:密码@ip:端口/数据库名?authSource=admin&authMechanism=SCRAM-SHA-1
    mongodb:
      uri: mongodb://root:root@192.168.56.103:27017/yygh_hosp?authSource=admin&authMechanism=SCRAM-SHA-1
# todo wx支付，此处用的是谷粒商城提供的，如果不更改，则回调在http://localhost:8160
wx:
  pay:
    app-id: wx74862e0dfcf69954
    partner: 1558950191
    partner-key: T6m9iK73b0kn9g5v426MKfHQH7X8rKwb
    cert: cert/apiclient_cert.p12
# todo 阿里云sms配置    
aliyun:
  sms:
    template-code: SMS_206546316
```

#### behappy-oss
> 以下是主要介绍的配置，开发时以实际代码内的为准

```yaml
# todo 阿里云oss配置
aliyun:
  oss:
    access-key-id:
    access-key-secret:
    bucket: behappy-xiaowu
    endpoint: oss-cn-beijing.aliyuncs.com
```

#### behappy-user
> 以下是主要介绍的配置，开发时以实际代码内的为准

```yaml
server:
  # wx.open.redirect-url回调会跳转到此端口
  port: 8160
spring:
  cloud:
    # todo sentinel dashboard配置，不配也不报错
    sentinel:
      # 取消Sentinel控制台懒加载
      eager: true
      transport:
        dashboard: pig-sentinel:5003
  redis:
    # todo redis配置
    redisson:
      config: |-
        # 例: 单节点设置
        singleServerConfig:
          # （节点地址）redis://-非ssl,rediss://-ssl
          address: redis://192.168.56.103:6379
          database: 0
          password: null      
  # todo wx支付，此处用的是谷粒商城提供的
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    password: root
    username: root
    url: jdbc:mysql://192.168.56.103:3306/yygh_user?allowMultiQueries=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
# todo wx支付，此处用的是谷粒商城提供的
wx:
  open:
    app-id: wxed9954c01bb89b47
    app-secret: a7482517235173ddb4083788de60b90e
    redirect-url: http://localhost:8160/api/ucenter/wx/callback
    yygh-base-url: http://localhost:3000
```


### 启动流程

0. clone[组件应用库](https://github.com/behappy-hospital/behappy-docker-application.git), 切到各自目录下执行`docker-compose up -d`

> 需要nacos,behappy-sentinel-dashboard,redis,rabbitmq,mongo,mysql

1. 执行`doc/sql`下的sql文件

2. 启动项目, 各模块作用已标明在文档中

后端必须启动的模块

```yaml
BehappyCmnApplication
BehappyGatewayApplication
BehappyHospApplication
BehappyManagerApplication
BehappyMsmApplication
BehappyOrderApplication
BehappyUserApplication
```

前端模块

```yaml
behappy-hospital-user --平台页面
behappy-hospital-admin -- 后台管理
```

### 操作流程(项目启动成功后)

0. 登陆manager服务, 设置医院信息

![img.png](doc/readme-img/img.png)

1. 导入医院数据

![img_1.png](doc/readme-img/img_1.png)

2. 找到behappy-manager/resources下的department.json, 复制粘贴-科室列表

![img_2.png](doc/readme-img/img_2.png)

3. 找到behappy-manager/resources下的schedule.json, 复制粘贴-排班列表
4. 登陆behappy-hospital-user, 完成注册登陆

> **使用localhost访问,不要用ipv4**

- 使用手机号或者微信登陆(目前是模拟发送短信, 在MsmService中可自行打开注释. 微信登陆使用谷粒学院的key和secret)

![img_3.png](doc/readme-img/img_3.png)

- 登陆后会回调到`myheader.vue的loginCallback方法中`, 如果openid为空则说明此用户为新用户, 需要绑定手机号. 接着打开手机登录层，绑定手机号
- 查看Msm模块的日志, 能找到验证码

5. 完成实名验证

- 已取消了图片验证, 可以不传图片, 如果传图片, 记得将oss模块中的key和secret补全

![img_5.png](doc/readme-img/img_5.png)

- 登陆到admin, 通过认证

![img_4.png](doc/readme-img/img_4.png)

6. 下单流程

- 添加就诊人
- 找到`多发性硬化专科门诊`科室(仅此科室有数据)
- 进行挂号

![img_7.png](doc/readme-img/img_7.png)

- 进行支付(目前3秒轮询查询订单状态, 待已支付后窗口关闭)
  ![img_6.png](doc/readme-img/img_6.png)

7. 退单流程
   退单逻辑在`OrderService-cancelOrder, 可自行将时间限定注释打开`
   ![img_8.png](doc/readme-img/img_8.png)

8. springboot admin查看各模块状态(http://localhost:8203/ [账户密码: root@root])

![img.png](doc/readme-img/img_9.png)

9. xxl job: http://192.168.56.103:8080/

- 添加task

![img_1.png](doc/readme-img/img_10.png)

![img_2.png](doc/readme-img/img_11.png)

- 添加任务(handler对应代码在`ReminderXxlJob`)

![img_3.png](doc/readme-img/img_12.png)

> 执行日志查看
![img_4.png](doc/readme-img/img_13.png)

> msm服务也接收到了mq的提醒消息
![img.png](doc/readme-img/img_14.png)

### 模块

```yaml
├─behappy-api --放feign和VO/DTO
│  ├─behappy-api-cmn
│  ├─behappy-api-common
│  ├─behappy-api-hosp
│  ├─behappy-api-manager
│  ├─behappy-api-msm
│  ├─behappy-api-order
│  └─behappy-api-user
├─behappy-cmn --数据字典模块
├─behappy-common --公共模块
│  ├─behappy-common-core --核心模块
│  ├─behappy-common-mongo --mongo模块
│  ├─behappy-common-mybatis --mybatis模块
│  ├─behappy-common-rmq --rabbit mq模块
│  └─behappy-common-sentinel --sentinel/feign配置模块
├─behappy-executor-task --放定时任务, 整合xxl-job使用
├─behappy-gateway --网关模块[swagger地址](http://localhost:8088/swagger-ui/index.html)
├─behappy-hosp --医院模块
├─behappy-manager --后台管理模块
├─behappy-monitor --springboot admin
├─behappy-msm --短信模块
├─behappy-order --订单模块
├─behappy-oss --oss模块
├─behappy-statistics --信息统计模块
├─behappy-user --用户模块
└─doc
    ├─01-教案
    ├─02-分析图
    ├─03-尚医通架构图
    ├─04-尚医通业务流程图
    └─05-sql
```
