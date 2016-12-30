/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : chatserver

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-12-30 16:19:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_account` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKEY_USER_ACCOUNT` (`user_account`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES ('17', '1', '好友');
INSERT INTO `category` VALUES ('18', '2', '好友');
INSERT INTO `category` VALUES ('19', '3', '好友');

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_account` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `friend_account` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `category_id` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_FRIENDS_USER_ID` (`user_account`),
  KEY `FK_FRIENDS_CATEGORY_ID` (`category_id`),
  KEY `FK_FRIENDS_FUSER_ID` (`friend_account`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of friends
-- ----------------------------
INSERT INTO `friends` VALUES ('15', '1', '2', '17', '2016-12-30 15:09:26');
INSERT INTO `friends` VALUES ('16', '2', '1', '18', '2016-12-30 15:09:26');
INSERT INTO `friends` VALUES ('17', '1', '3', '17', '2016-12-30 15:21:42');
INSERT INTO `friends` VALUES ('18', '3', '1', '19', '2016-12-30 15:21:42');

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

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('0', 'system', 'system', 'system', '2016-12-28 11:16:23', null, '1');
INSERT INTO `user` VALUES ('1', '1', '1', '1', '2016-12-28 21:52:54', '0', '1');
INSERT INTO `user` VALUES ('10', '2', '2', '2', '2016-12-29 11:17:58', '0', '1');
INSERT INTO `user` VALUES ('11', '11', '11', '11', '2016-12-29 12:11:48', '0', '1');
INSERT INTO `user` VALUES ('12', '12', '12', '12', '2016-12-29 14:34:59', '0', '0');
INSERT INTO `user` VALUES ('13', '3', '3', '3', '2016-12-29 17:16:24', '0', '0');
