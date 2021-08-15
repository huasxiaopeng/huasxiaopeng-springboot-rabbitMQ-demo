springboot-rabbitmq-work   springboot整合rabbitmq 一般使用

springboot-rabbitmq-deadletter   springboot整合rabbitmq 死信队列(备胎队列)
数据库 
CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_name` varchar(30) DEFAULT NULL,
  `order_id` varchar(30) DEFAULT NULL,
  `order_status` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4


*rabbit解决订单超时关闭*
springboot-rabbitmq-delay   springboot使用死信队列,实现订单30分钟关闭
原理:列(绑下单投放消息到A交换机(过期时间30分钟),消息到aa队定死信交换机),不设置aa队列的消费者(故此消息一直未消费).
	 30分钟后,过期消息投递到死信交换机,死信队列,又死信消费者消费,判断订单id时候支付,支付->return  未支付->关闭订单,返还库存


*分布式事务处理*
order-producer  rabbit处理分布式事务处理 ,订单服务  生产者
dl-consumer     rabbit处理分布式事务处理 ,派单服务  补单消费者,派单消费者

CREATE TABLE `platoon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4

CREATE TABLE `order_test` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `order_createtime` datetime DEFAULT NULL,
  `order_money` double(10,2) DEFAULT NULL,
  `order_state` int(1) DEFAULT NULL,
  `commodity_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4

1.分布式事务产生的背景?
	1.RPC通讯中每个服务都有自己独立的数据源,每个数据源都互不影响.
	2.在单个项目中存在多个不同jdbc连接(多数据源)
	
2.如何基于我们的MQ解决我们的分布式事务的问题(最终一致性)
	1.确保我们的生产者往我们的MQ投递消息一定要成功.(生产者消息确认机制confirm),实现重试.
	2.确保我们的消费者能够消费成功(手动ack机制),如果消费失败情况下,MQ自动帮消费者重试.
	3.确保我们的生产者第一事务先执行成功,如果执行失败采用补单队列.
	
