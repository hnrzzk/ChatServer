/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : chatserver

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-12-30 14:52:51
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
  KEY `FKEY_USER_ACCOUNT` (`user_account`),
  CONSTRAINT `FKEY_USER_ACCOUNT` FOREIGN KEY (`user_account`) REFERENCES `user` (`account`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
