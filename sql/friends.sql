/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : chatserver

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2017-01-03 20:31:22
*/

SET FOREIGN_KEY_CHECKS=0;

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
  KEY `FK_FRIENDS_FUSER_ID` (`friend_account`),
  CONSTRAINT `FK_FRIENDACCOUNT` FOREIGN KEY (`friend_account`) REFERENCES `user` (`account`) ON UPDATE CASCADE,
  CONSTRAINT `FK_USERACCOUNT` FOREIGN KEY (`user_account`) REFERENCES `user` (`account`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of friends
-- ----------------------------
