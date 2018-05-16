# Host: localhost  (Version: 5.5.47)
# Date: 2017-05-14 11:58:33
# Generator: MySQL-Front 5.3  (Build 4.234)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "courses"
#

DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses` (
  `Cno` char(4) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '' COMMENT '课程号',
  `name` varchar(30) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '课程名',
  PRIMARY KEY (`Cno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci COMMENT='课程表';

#
# Data for table "courses"
#

INSERT INTO `courses` VALUES ('9001','高等数学'),('9002','大学体验英语'),('9003','全新版大学日语'),('9004','政治经济学'),('9005','西方音乐史'),('9999','大学生就业指导');

#
# Structure for table "m1234567890123"
#

DROP TABLE IF EXISTS `m1234567890123`;
CREATE TABLE `m1234567890123` (
  `Qno` int(2) unsigned NOT NULL AUTO_INCREMENT COMMENT '题号索引',
  `question` varchar(255) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '' COMMENT '具体问题',
  `A` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '优' COMMENT '选项A',
  `B` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '良' COMMENT '选项B',
  `C` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '中',
  `D` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '差',
  PRIMARY KEY (`Qno`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci COMMENT='评教模板 - 测试';

#
# Data for table "m1234567890123"
#

INSERT INTO `m1234567890123` VALUES (1,'您感觉教师为授课做的准备工作是否充分？','优','良','中','差'),(2,'教师授课是否认真？','优','良','中','差'),(3,'是否有上课迟到、提前下课、缺课等情况?','没有','上课迟到','提前下课','缺课'),(4,'是否严格要求学生，积极维持课堂纪律?','优','良','中','差'),(5,'是否定时定期进行辅导答疑？','经常','有一些','偶尔','从不'),(6,'教师语言表达是否清楚、流利?','优','良','中','差'),(7,'教师讲课是否思路清晰、阐述准确、有逻辑性?','优','良','中','差'),(8,'您对该教师理论水平和教学能力的评价？','优','良','中','差'),(9,'授课内容是否充实，知识点丰富？','优','良','中','差'),(10,'授课内容是否结合实践，适当引入学科新知识、新成果？','优','良','中','差'),(11,'教师是否善于启发引导，经常与学生互动？','优','良','中','差'),(12,'是否理论联系实际，注重学生能力的培养？','优','良','中','差'),(13,'是否经常布置思考题及文献阅读？','经常','有一些','偶尔','从不'),(14,'讲课是否有吸引力，能激发学生学习兴趣？','优','良','中','差'),(15,'通过该课程学习，您是否掌握了课程的主要知识内容？','掌握','基本掌握','一般','很少');

#
# Structure for table "m1482677874617"
#

DROP TABLE IF EXISTS `m1482677874617`;
CREATE TABLE `m1482677874617` (
  `Qno` int(2) unsigned NOT NULL AUTO_INCREMENT COMMENT '题号索引',
  `question` varchar(255) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '' COMMENT '具体问题',
  `A` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '优' COMMENT '选项A',
  `B` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '良' COMMENT '选项B',
  `C` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '中',
  `D` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '差',
  PRIMARY KEY (`Qno`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci;

#
# Data for table "m1482677874617"
#

INSERT INTO `m1482677874617` VALUES (1,'qweqweqwe','选项A','选项B','选项C','选项D'),(2,'qweqweqwe','选项A','选项B','选项C','选项D'),(3,'qweqweqweqwe','选项A','选项B','选项C','选项D');

#
# Structure for table "model_index"
#

DROP TABLE IF EXISTS `model_index`;
CREATE TABLE `model_index` (
  `model_id` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '模板id，新建的模板数据表都以id命名',
  `name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  PRIMARY KEY (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci COMMENT='模板索引，id和name的映射';

#
# Data for table "model_index"
#

INSERT INTO `model_index` VALUES ('m1234567890123','模板1'),('m1482677874617','模板2');

#
# Structure for table "students"
#

DROP TABLE IF EXISTS `students`;
CREATE TABLE `students` (
  `Sno` char(4) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '学号',
  `name` varchar(10) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '' COMMENT '姓名',
  `sex` varchar(4) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '男',
  `class` varchar(2) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '班级',
  `phone` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  PRIMARY KEY (`Sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci COMMENT='学生表';

#
# Data for table "students"
#

INSERT INTO `students` VALUES ('4001','陆展博','男','12','123','北京'),('4002','张伟','男','13','231','3123'),('4003','吕小布','男','12','213123','213213');

#
# Structure for table "students_assess"
#

DROP TABLE IF EXISTS `students_assess`;
CREATE TABLE `students_assess` (
  `Sno` char(4) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '' COMMENT '学号',
  `T3002` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT 'N',
  `T3003` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT 'N',
  `T3008` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT 'N',
  PRIMARY KEY (`Sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci COMMENT='学生评教情，列为教师ID，可动态添加列';

#
# Data for table "students_assess"
#

INSERT INTO `students_assess` VALUES ('4001','Y','N','N'),('4002','N','N','N'),('4003','Y','N','N');

#
# Structure for table "teachers"
#

DROP TABLE IF EXISTS `teachers`;
CREATE TABLE `teachers` (
  `Tno` char(4) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '教师号',
  `name` varchar(10) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '教师姓名',
  `Cno` char(4) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `sex` varchar(4) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '男' COMMENT '性别',
  `phone` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  PRIMARY KEY (`Tno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci COMMENT='教师表';

#
# Data for table "teachers"
#

INSERT INTO `teachers` VALUES ('3002','秦羽墨','9002','女','1213','上海'),('3003','关谷神奇','9003','男','21312','3123'),('3008','张大炮','9999','男','12431421','35213414512');

#
# Structure for table "teach_class"
#

DROP TABLE IF EXISTS `teach_class`;
CREATE TABLE `teach_class` (
  `Tno` char(4) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '教师号',
  `class` varchar(2) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '班级',
  PRIMARY KEY (`Tno`,`class`),
  CONSTRAINT `teach_class_ibfk_1` FOREIGN KEY (`Tno`) REFERENCES `teachers` (`Tno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci COMMENT='授课表';

#
# Data for table "teach_class"
#

INSERT INTO `teach_class` VALUES ('3002','11'),('3002','12'),('3003','11'),('3003','12'),('3008','11'),('3008','12');

#
# Structure for table "started_assess"
#

DROP TABLE IF EXISTS `started_assess`;
CREATE TABLE `started_assess` (
  `Tno` char(4) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '待评价教师',
  `model` varchar(15) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '启用模板ID',
  `StarterID` char(4) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '' COMMENT '发起者，评教管理员ID',
  `StartTime` date NOT NULL DEFAULT '0000-00-00' COMMENT '开始日期',
  `EndTime` date NOT NULL DEFAULT '0000-00-00' COMMENT '截止日期',
  PRIMARY KEY (`Tno`),
  KEY `started_assess_ibfk_2` (`model`),
  CONSTRAINT `started_assess_ibfk_1` FOREIGN KEY (`Tno`) REFERENCES `teachers` (`Tno`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `started_assess_ibfk_2` FOREIGN KEY (`model`) REFERENCES `model_index` (`model_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci COMMENT='已发起的评教,每个老师只能发起一次评教';

#
# Data for table "started_assess"
#

INSERT INTO `started_assess` VALUES ('3002','m1234567890123','2002','2016-12-10','2017-01-10'),('3003','m1482677874617','2002','2016-12-10','2017-01-10');

#
# Structure for table "total_assesses"
#

DROP TABLE IF EXISTS `total_assesses`;
CREATE TABLE `total_assesses` (
  `Tno` char(4) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '教师号',
  `Sno` char(4) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '学号',
  `model` varchar(15) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '启用的模板',
  `qs_num` char(2) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '' COMMENT '问题数量',
  `Q1` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '问题1',
  `Q2` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '',
  `Q3` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '',
  `Q4` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '',
  `Q5` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '',
  `Q6` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '',
  `Q7` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '',
  `Q8` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '',
  `Q9` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '',
  `Q10` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '',
  `Q11` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '',
  `Q12` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '',
  `Q13` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '',
  `Q14` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '',
  `Q15` char(1) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`Tno`,`Sno`),
  KEY `Sno` (`Sno`),
  KEY `total_assesses_ibfk_3` (`model`),
  CONSTRAINT `total_assesses_ibfk_1` FOREIGN KEY (`Tno`) REFERENCES `teachers` (`Tno`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `total_assesses_ibfk_2` FOREIGN KEY (`Sno`) REFERENCES `students` (`Sno`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `total_assesses_ibfk_3` FOREIGN KEY (`model`) REFERENCES `model_index` (`model_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci COMMENT='学生评价汇总表';

#
# Data for table "total_assesses"
#

INSERT INTO `total_assesses` VALUES ('3002','4001','m1234567890123','15','A','A','A','A','A','A','B','B','C','C','C','D','D','D','D'),('3002','4003','m1234567890123','15','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A');

#
# Structure for table "users"
#

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` char(4) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '' COMMENT 'User Id,这里假设系统管理员为1xxx，评教管理员为2xxx，教师为3xxx， 学生为4xxx',
  `password` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '密码',
  `role` varchar(10) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '角色 ‘SYSADMIN’,''ASSADMIN'',‘TEACHER’，‘STUDENT’',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci COMMENT='用户角色分配表';

#
# Data for table "users"
#

INSERT INTO `users` VALUES ('1001','b8c37e33defde51cf91e1e03e51657da','SYSADMIN'),('2002','4ba29b9f9e5732ed33761840f4ba6c53','ASSADMIN'),('3002','d806ca13ca3449af72a1ea5aedbed26a','TEACHER'),('3003','a4380923dd651c195b1631af7c829187','TEACHER'),('3008','c02f9de3c2f3040751818aacc7f60b74','TEACHER'),('4001','ffc58105bf6f8a91aba0fa2d99e6f106','STUDENT'),('4002','254ed7d2de3b23ab10936522dd547b78','STUDENT'),('4003','3ce83f544dbe69bb4fb19211fc442c2f','STUDENT');

#
# Structure for table "system_admin"
#

DROP TABLE IF EXISTS `system_admin`;
CREATE TABLE `system_admin` (
  `sys_id` char(4) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '' COMMENT '管理员ID',
  `name` char(10) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '系统管理员姓名',
  `sex` varchar(4) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '男',
  `phone` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  PRIMARY KEY (`sys_id`),
  CONSTRAINT `system_admin_ibfk_1` FOREIGN KEY (`sys_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci COMMENT='管理员';

#
# Data for table "system_admin"
#

INSERT INTO `system_admin` VALUES ('1001','曾小贤','男','10087','上海');

#
# Structure for table "assess_admin"
#

DROP TABLE IF EXISTS `assess_admin`;
CREATE TABLE `assess_admin` (
  `ass_id` char(4) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '' COMMENT '考评管理员账号',
  `name` char(10) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '' COMMENT '考评管理员姓名',
  `sex` varchar(4) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '男',
  `phone` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  PRIMARY KEY (`ass_id`),
  CONSTRAINT `assess_admin_ibfk_1` FOREIGN KEY (`ass_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci COMMENT='考评管理员';

#
# Data for table "assess_admin"
#

INSERT INTO `assess_admin` VALUES ('2002','唐悠悠','女','12123','上海');
