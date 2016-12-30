/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : chatserver

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-12-30 14:52:43
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
  CONSTRAINT `FK_FRIENDS_CATEGORY_ID` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_FRIEND_ACCOUNT` FOREIGN KEY (`friend_account`) REFERENCES `user` (`account`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_ACCOUNT` FOREIGN KEY (`user_account`) REFERENCES `user` (`account`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
