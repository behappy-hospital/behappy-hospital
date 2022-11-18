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

### 技术栈

alibaba,cloud,springboot,最佳实践版本:https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E

**核心依赖**

jdk: 1.8
nodejs: 10.24.1


| 依赖                 | 版本       |
| -------------------- | ---------- |
| Spring Boot          | 2.6.11     |
| Spring Cloud         | 2021.0.4   |
| Spring Cloud Alibaba | 2021.0.4.0 |
| Mybatis Plus         | 3.5.2      |
| Swagger              | 3.0.0      |
| behappy redis        | 0.0.4      |
| wxpay-sdk            | 0.0.3      |
| Hutool               | 5.6.4      |
| xxl job              | 2.3.1      |
| druid                | 1.2.9      |

### 启动流程

0. clone[组件应用库](https://github.com/behappy-hospital/behappy-docker-application.git), 切到各自目录下执行`docker-compose up -d`

> 需要nacos,behappy-sentinel-dashboard,redis,rabbitmq,mongo,mysql

1. 执行`doc/sql`下的sql文件, 以及`behappy-xxl-admin/resources/db/tables_xxl_job.sql`

> 执行顺序
>
> 1. yygh_manager.sql   2. yygh初始化表结构.sql   3. yygh初始化数据.sql(已包含一条默认医院数据)   4. tables_xxl_job.sql

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

8. springboot admin查看各模块状态(http://localhost:8203/monitor [账户密码: root@root])

![img.png](doc/readme-img/img_9.png)

9. xxl job: http://localhost:8080/

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
├─behappy-xxl-admin --xxl-job admin
└─doc
    ├─01-教案
    ├─02-分析图
    ├─03-尚医通架构图
    ├─04-尚医通业务流程图
    └─05-sql
```
