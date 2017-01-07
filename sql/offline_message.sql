/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50715
Source Host           : localhost:3306
Source Database       : chatserver

Target Server Type    : MYSQL
Target Server Version : 50715
File Encoding         : 65001

Date: 2017-01-08 00:39:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `offline_message`
-- ----------------------------
DROP TABLE IF EXISTS `offline_message`;
CREATE TABLE `offline_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `command_type` int(11) NOT NULL,
  `account` varchar(20) NOT NULL,
  `message` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `is_send` int(1) NOT NULL DEFAULT '0',
  `message_type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of offline_message
-- ----------------------------
INSERT INTO `offline_message` VALUES ('4', '0', '1', '{\"message\":\"hello\",\"receiveAccount\":\"1\",\"sendAccount\":\"2\"}', '2017-01-07 23:35:43', '0', '4');
INSERT INTO `offline_message` VALUES ('5', '0', '2', '{\"message\":\"hello\",\"receiveAccount\":\"2\",\"sendAccount\":\"1\"}', '2017-01-07 23:42:34', '0', '4');
INSERT INTO `offline_message` VALUES ('6', '0', '2', '{\"message\":\"hello\",\"receiveAccount\":\"2\",\"sendAccount\":\"1\"}', '2017-01-07 23:42:45', '0', '4');
INSERT INTO `offline_message` VALUES ('7', '3', '2', '{\"accept\":false,\"categoryName\":\"好友\",\"friendAccount\":\"2\",\"userAccount\":\"1\"}', '2017-01-07 23:44:42', '0', '2');
