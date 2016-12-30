/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : chatserver

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-12-30 14:51:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '昵称',
  `account` varchar(15) COLLATE utf8_unicode_ci NOT NULL COMMENT '账户',
  `password` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '密码',
  `register_time` datetime DEFAULT NULL,
  `sex` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_online` int(1) NOT NULL DEFAULT '0' COMMENT '是否在线，1在线，0离线',
  PRIMARY KEY (`id`),
  KEY `account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
