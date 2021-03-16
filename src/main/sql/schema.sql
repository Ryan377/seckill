-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;
-- 使用数据库
use seckill;
-- 创建秒杀库存表
CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` varchar(120) NOT NULL COMMENT '商品名称',
`number` int NOT NULL COMMENT '库存数量',
`start_time` TIMESTAMP NOT NULL COMMENT '秒杀开始时间',
`end_time` TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY(seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=UTF8 COMMENT='秒杀库存表';

-- 初始化数据
INSERT INTO
  seckill(name,number,start_time,end_time)
VALUES
  ('1元秒杀iPhoneX',100,'2018-3-10 20:00:00','2018-3-11 20:00:00'),
  ('1元秒杀小米6',1000,'2018-3-10 20:00:00','2018-3-11 20:00:00'),
  ('1元秒杀大F键盘',300,'2018-3-10 20:00:00','2018-3-11 20:00:00'),
  ('1元秒杀铁三角耳机',500,'2018-3-10 20:00:00','2018-3-11 20:00:00'),
  ('1元秒杀PS4',600,'2018-3-10 20:00:00','2018-3-11 20:00:00');

-- 秒杀成功明细表
CREATE TABLE success_killed(
`seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
`user_phone` bigint NOT NULL COMMENT '用户手机号码',
`state` tinyint NOT NULL DEFAULT -1 COMMENT '状态标识：-1 无效，0 成功，1 已付款，2 已发货',
`create_time` TIMESTAMP NOT NULL COMMENT '秒杀时间',
PRIMARY KEY(seckill_id,user_phone),/*联合主键*/
key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT='秒杀成功明细表';

--2018年3月8日22:30:10 加入数据
insert into
  seckill(name,number,start_time,end_time)
  values
  ('100元秒杀无人机',100,'2018-3-8 20:31:48','2018-3-16 20:31:48');



