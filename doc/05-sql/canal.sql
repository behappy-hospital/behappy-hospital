CREATE USER canal IDENTIFIED BY 'canal';
GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%';
FLUSH PRIVILEGES;
-- 创建canal user
CREATE DATABASE /*!32312 IF NOT EXISTS*/ `canal_manager` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `canal_manager`;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for canal_adapter_config
-- ----------------------------
DROP TABLE IF EXISTS `canal_adapter_config`;
CREATE TABLE `canal_adapter_config` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                        `category` varchar(45) NOT NULL,
                                        `name` varchar(45) NOT NULL,
                                        `status` varchar(45) DEFAULT NULL,
                                        `content` text NOT NULL,
                                        `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_cluster
-- ----------------------------
DROP TABLE IF EXISTS `canal_cluster`;
CREATE TABLE `canal_cluster` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `name` varchar(63) NOT NULL,
                                 `zk_hosts` varchar(255) NOT NULL,
                                 `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_config
-- ----------------------------
DROP TABLE IF EXISTS `canal_config`;
CREATE TABLE `canal_config` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                `cluster_id` bigint(20) DEFAULT NULL,
                                `server_id` bigint(20) DEFAULT NULL,
                                `name` varchar(45) NOT NULL,
                                `status` varchar(45) DEFAULT NULL,
                                `content` text NOT NULL,
                                `content_md5` varchar(128) NOT NULL,
                                `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `sid_UNIQUE` (`server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_instance_config
-- ----------------------------
DROP TABLE IF EXISTS `canal_instance_config`;
CREATE TABLE `canal_instance_config` (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                         `cluster_id` bigint(20) DEFAULT NULL,
                                         `server_id` bigint(20) DEFAULT NULL,
                                         `name` varchar(45) NOT NULL,
                                         `status` varchar(45) DEFAULT NULL,
                                         `content` text NOT NULL,
                                         `content_md5` varchar(128) DEFAULT NULL,
                                         `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         PRIMARY KEY (`id`),
                                         UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_node_server
-- ----------------------------
DROP TABLE IF EXISTS `canal_node_server`;
CREATE TABLE `canal_node_server` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `cluster_id` bigint(20) DEFAULT NULL,
                                     `name` varchar(63) NOT NULL,
                                     `ip` varchar(63) NOT NULL,
                                     `admin_port` int(11) DEFAULT NULL,
                                     `tcp_port` int(11) DEFAULT NULL,
                                     `metric_port` int(11) DEFAULT NULL,
                                     `status` varchar(45) DEFAULT NULL,
                                     `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_user
-- ----------------------------
DROP TABLE IF EXISTS `canal_user`;
CREATE TABLE `canal_user` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `username` varchar(31) NOT NULL,
                              `password` varchar(128) NOT NULL,
                              `name` varchar(31) NOT NULL,
                              `roles` varchar(31) NOT NULL,
                              `introduction` varchar(255) DEFAULT NULL,
                              `avatar` varchar(255) DEFAULT NULL,
                              `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Records of canal_user
-- ----------------------------
BEGIN;
INSERT INTO `canal_user` VALUES (1, 'admin', '6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', 'Canal Manager', 'admin', NULL, NULL, '2019-07-14 00:05:28');
INSERT INTO `canal_manager`.`canal_config`(`id`, `cluster_id`, `server_id`, `name`, `status`, `content`, `content_md5`, `modified_time`) VALUES (2, NULL, 2, 'canal.properties', NULL, '#################################################\n######### 		common argument		#############\n#################################################\n# tcp bind ip\ncanal.ip =\n# register ip to zookeeper\ncanal.register.ip =\ncanal.port = 11111\ncanal.metrics.pull.port = 11112\n# canal instance user/passwd\n# canal.user = canal\n# canal.passwd = E3619321C1A937C46A0D8BD1DAC39F93B27D4458\n\n# canal admin config\n#canal.admin.manager = 127.0.0.1:8089\ncanal.admin.port = 11110\ncanal.admin.user = admin\ncanal.admin.passwd = 6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9\n# admin auto register\n#canal.admin.register.auto = true\n#canal.admin.register.cluster =\n#canal.admin.register.name =\n\ncanal.zkServers =\n# flush data to zk\ncanal.zookeeper.flush.period = 1000\ncanal.withoutNetty = false\n# tcp, kafka, rocketMQ, rabbitMQ\ncanal.serverMode = rabbitMQ\n# flush meta cursor/parse position to file\ncanal.file.data.dir = ${canal.conf.dir}\ncanal.file.flush.period = 1000\n## memory store RingBuffer size, should be Math.pow(2,n)\ncanal.instance.memory.buffer.size = 16384\n## memory store RingBuffer used memory unit size , default 1kb\ncanal.instance.memory.buffer.memunit = 1024 \n## meory store gets mode used MEMSIZE or ITEMSIZE\ncanal.instance.memory.batch.mode = MEMSIZE\ncanal.instance.memory.rawEntry = true\n\n## detecing config\ncanal.instance.detecting.enable = false\n#canal.instance.detecting.sql = insert into retl.xdual values(1,now()) on duplicate key update x=now()\ncanal.instance.detecting.sql = select 1\ncanal.instance.detecting.interval.time = 3\ncanal.instance.detecting.retry.threshold = 3\ncanal.instance.detecting.heartbeatHaEnable = false\n\n# support maximum transaction size, more than the size of the transaction will be cut into multiple transactions delivery\ncanal.instance.transaction.size =  1024\n# mysql fallback connected to new master should fallback times\ncanal.instance.fallbackIntervalInSeconds = 60\n\n# network config\ncanal.instance.network.receiveBufferSize = 16384\ncanal.instance.network.sendBufferSize = 16384\ncanal.instance.network.soTimeout = 30\n\n# binlog filter config\ncanal.instance.filter.druid.ddl = true\ncanal.instance.filter.query.dcl = false\ncanal.instance.filter.query.dml = false\ncanal.instance.filter.query.ddl = false\ncanal.instance.filter.table.error = false\ncanal.instance.filter.rows = false\ncanal.instance.filter.transaction.entry = false\ncanal.instance.filter.dml.insert = false\ncanal.instance.filter.dml.update = false\ncanal.instance.filter.dml.delete = false\n\n# binlog format/image check\ncanal.instance.binlog.format = ROW,STATEMENT,MIXED \ncanal.instance.binlog.image = FULL,MINIMAL,NOBLOB\n\n# binlog ddl isolation\ncanal.instance.get.ddl.isolation = false\n\n# parallel parser config\ncanal.instance.parser.parallel = true\n## concurrent thread number, default 60% available processors, suggest not to exceed Runtime.getRuntime().availableProcessors()\n#canal.instance.parser.parallelThreadSize = 16\n## disruptor ringbuffer size, must be power of 2\ncanal.instance.parser.parallelBufferSize = 256\n\n# table meta tsdb info\ncanal.instance.tsdb.enable = true\ncanal.instance.tsdb.dir = ${canal.file.data.dir:../conf}/${canal.instance.destination:}\ncanal.instance.tsdb.url = jdbc:h2:${canal.instance.tsdb.dir}/h2;CACHE_SIZE=1000;MODE=MYSQL;\ncanal.instance.tsdb.dbUsername = canal\ncanal.instance.tsdb.dbPassword = canal\n# dump snapshot interval, default 24 hour\ncanal.instance.tsdb.snapshot.interval = 24\n# purge snapshot expire , default 360 hour(15 days)\ncanal.instance.tsdb.snapshot.expire = 360\n\n#################################################\n######### 		destinations		#############\n#################################################\ncanal.destinations = \n# conf root dir\ncanal.conf.dir = ../conf\n# auto scan instance dir add/remove and start/stop instance\ncanal.auto.scan = true\ncanal.auto.scan.interval = 5\n# set this value to \'true\' means that when binlog pos not found, skip to latest.\n# WARN: pls keep \'false\' in production env, or if you know what you want.\ncanal.auto.reset.latest.pos.mode = false\n\ncanal.instance.tsdb.spring.xml = classpath:spring/tsdb/h2-tsdb.xml\n#canal.instance.tsdb.spring.xml = classpath:spring/tsdb/mysql-tsdb.xml\n\ncanal.instance.global.mode = manager\ncanal.instance.global.lazy = false\ncanal.instance.global.manager.address = ${canal.admin.manager}\n#canal.instance.global.spring.xml = classpath:spring/memory-instance.xml\ncanal.instance.global.spring.xml = classpath:spring/file-instance.xml\n#canal.instance.global.spring.xml = classpath:spring/default-instance.xml\n\n##################################################\n######### 	      MQ Properties      #############\n##################################################\n# aliyun ak/sk , support rds/mq\ncanal.aliyun.accessKey =\ncanal.aliyun.secretKey =\ncanal.aliyun.uid=\n\ncanal.mq.flatMessage = true\ncanal.mq.canalBatchSize = 50\ncanal.mq.canalGetTimeout = 100\n# Set this value to \"cloud\", if you want open message trace feature in aliyun.\ncanal.mq.accessChannel = local\n\ncanal.mq.database.hash = true\ncanal.mq.send.thread.size = 30\ncanal.mq.build.thread.size = 8\n\n##################################################\n######### 		     Kafka 		     #############\n##################################################\nkafka.bootstrap.servers = 127.0.0.1:6667\nkafka.acks = all\nkafka.compression.type = none\nkafka.batch.size = 16384\nkafka.linger.ms = 1\nkafka.max.request.size = 1048576\nkafka.buffer.memory = 33554432\nkafka.max.in.flight.requests.per.connection = 1\nkafka.retries = 0\n\nkafka.kerberos.enable = false\nkafka.kerberos.krb5.file = \"../conf/kerberos/krb5.conf\"\nkafka.kerberos.jaas.file = \"../conf/kerberos/jaas.conf\"\n\n##################################################\n######### 		    RocketMQ	     #############\n##################################################\nrocketmq.producer.group = test\nrocketmq.enable.message.trace = false\nrocketmq.customized.trace.topic =\nrocketmq.namespace =\nrocketmq.namesrv.addr = 127.0.0.1:9876\nrocketmq.retry.times.when.send.failed = 0\nrocketmq.vip.channel.enabled = false\nrocketmq.tag = \n\n##################################################\n######### 		    RabbitMQ	     #############\n##################################################\nrabbitmq.host =192.168.56.100\nrabbitmq.virtual.host =canal\nrabbitmq.exchange =amq.direct\nrabbitmq.username =root\nrabbitmq.password =root\nrabbitmq.deliveryMode =', '146e5ae262e66e6ef6957675282c0a5f', '2023-03-08 14:29:33');
INSERT INTO `canal_manager`.`canal_instance_config`(`id`, `cluster_id`, `server_id`, `name`, `status`, `content`, `content_md5`, `modified_time`) VALUES (2, NULL, 2, 'instance', '1', '#################################################\n## mysql serverId , v1.0.26+ will autoGen\n# canal.instance.mysql.slaveId=0\n\n# enable gtid use true/false\ncanal.instance.gtidon=false\n\n# position info\ncanal.instance.master.address=192.168.56.100:3306\ncanal.instance.master.journal.name=\ncanal.instance.master.position=\ncanal.instance.master.timestamp=\ncanal.instance.master.gtid=\n\n# rds oss binlog\ncanal.instance.rds.accesskey=\ncanal.instance.rds.secretkey=\ncanal.instance.rds.instanceId=\n\n# table meta tsdb info\ncanal.instance.tsdb.enable=true\n#canal.instance.tsdb.url=jdbc:mysql://127.0.0.1:3306/canal_tsdb\n#canal.instance.tsdb.dbUsername=canal\n#canal.instance.tsdb.dbPassword=canal\n\n#canal.instance.standby.address =\n#canal.instance.standby.journal.name =\n#canal.instance.standby.position =\n#canal.instance.standby.timestamp =\n#canal.instance.standby.gtid=\n\n# username/password\ncanal.instance.dbUsername=canal\ncanal.instance.dbPassword=canal\ncanal.instance.connectionCharset = UTF-8\n# enable druid Decrypt database password\ncanal.instance.enableDruid=false\n#canal.instance.pwdPublicKey=MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALK4BUxdDltRRE5/zXpVEVPUgunvscYFtEip3pmLlhrWpacX7y7GCMo2/JM6LeHmiiNdH1FWgGCpUfircSwlWKUCAwEAAQ==\n\n# table regex\ncanal.instance.filter.regex=.*\\\\..*\n# table black regex\ncanal.instance.filter.black.regex=\n# table field filter(format: schema1.tableName1:field1/field2,schema2.tableName2:field1/field2)\n#canal.instance.filter.field=test1.t_product:id/subject/keywords,test2.t_company:id/name/contact/ch\n# table field black filter(format: schema1.tableName1:field1/field2,schema2.tableName2:field1/field2)\n#canal.instance.filter.black.field=test1.t_product:subject/product_image,test2.t_company:id/name/contact/ch\n\n# mq config\ncanal.mq.topic=instance\n# dynamic topic route by schema or table regex\n#canal.mq.dynamicTopic=mytest1.user,mytest2\\\\..*,.*\\\\..*\ncanal.mq.partition=0\n# hash partition config\n#canal.mq.partitionsNum=3\n#canal.mq.partitionHash=test.table:id^name,.*\\\\..*\n#################################################\n', '886029b25e89d21b0efaf020581a9b6c', '2023-03-08 14:27:34');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
