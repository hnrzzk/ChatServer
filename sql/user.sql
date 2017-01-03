/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : chatserver

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2017-01-03 20:31:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '昵称',
  `account` varchar(15) COLLATE utf8_unicode_ci NOT NULL COMMENT '账户',
  `password` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '密码',
  `register_time` datetime DEFAULT NULL,
  `sex` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_online` int(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否在线，1在线，0离线',
  `is_gag` int(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否禁言 0不禁言，1禁言',
  PRIMARY KEY (`id`),
  KEY `account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('00000', 'system', 'system', 'system', '2016-12-28 11:16:23', null, '1', '0');
INSERT INTO `user` VALUES ('00001', '1', '1', '1', '2016-12-28 21:52:54', '0', '0', '1');
INSERT INTO `user` VALUES ('00010', '2', '2', '2', '2016-12-29 11:17:58', '0', '0', '1');
INSERT INTO `user` VALUES ('00011', '11', '11', '11', '2016-12-29 12:11:48', '0', '0', '1');
INSERT INTO `user` VALUES ('00012', '12', '12', '12', '2016-12-29 14:34:59', '0', '0', '1');
INSERT INTO `user` VALUES ('00013', '3', '3', '3', '2016-12-29 17:16:24', '0', '0', '1');
