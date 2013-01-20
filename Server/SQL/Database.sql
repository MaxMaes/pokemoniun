/*
Navicat MySQL Data Transfer

Source Server         : Pokémonium
Source Server Version : 50210
Source Host           : remote-mysql3.servage.net:3306
Source Database       : viperpray2

Target Server Type    : MYSQL
Target Server Version : 50210
File Encoding         : 65001

Date: 2012-05-23 11:05:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `logs`
-- ----------------------------
DROP TABLE IF EXISTS `logs`;
CREATE TABLE `logs` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`date`  varchar(25) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`name`  varchar(12) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`UID`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`action`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of logs
-- ----------------------------

-- ----------------------------
-- Table structure for `pn_bag`
-- ----------------------------
DROP TABLE IF EXISTS `pn_bag`;
CREATE TABLE `pn_bag` (
`member`  int(11) NOT NULL ,
`item`  int(11) NOT NULL ,
`quantity`  int(11) NOT NULL ,
UNIQUE INDEX `memberitem` USING BTREE (`member`, `item`) ,
INDEX `Memberid` USING BTREE (`member`) 
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=ascii COLLATE=ascii_general_ci

;

-- ----------------------------
-- Records of pn_bag
-- ----------------------------

-- ----------------------------
-- Table structure for `pn_bans`
-- ----------------------------
DROP TABLE IF EXISTS `pn_bans`;
CREATE TABLE `pn_bans` (
`ip`  varchar(48) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci

;

-- ----------------------------
-- Records of pn_bans
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for `pn_box`
-- ----------------------------
DROP TABLE IF EXISTS `pn_box`;
CREATE TABLE `pn_box` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`member`  int(11) NULL DEFAULT NULL ,
`pokemon0`  int(11) NULL DEFAULT NULL ,
`pokemon1`  int(11) NULL DEFAULT NULL ,
`pokemon2`  int(11) NULL DEFAULT NULL ,
`pokemon3`  int(11) NULL DEFAULT NULL ,
`pokemon4`  int(11) NULL DEFAULT NULL ,
`pokemon5`  int(11) NULL DEFAULT NULL ,
`pokemon6`  int(11) NULL DEFAULT NULL ,
`pokemon7`  int(11) NULL DEFAULT NULL ,
`pokemon8`  int(11) NULL DEFAULT NULL ,
`pokemon9`  int(11) NULL DEFAULT NULL ,
`pokemon10`  int(11) NULL DEFAULT NULL ,
`pokemon11`  int(11) NULL DEFAULT NULL ,
`pokemon12`  int(11) NULL DEFAULT NULL ,
`pokemon13`  int(11) NULL DEFAULT NULL ,
`pokemon14`  int(11) NULL DEFAULT NULL ,
`pokemon15`  int(11) NULL DEFAULT NULL ,
`pokemon16`  int(11) NULL DEFAULT NULL ,
`pokemon17`  int(11) NULL DEFAULT NULL ,
`pokemon18`  int(11) NULL DEFAULT NULL ,
`pokemon19`  int(11) NULL DEFAULT NULL ,
`pokemon20`  int(11) NULL DEFAULT NULL ,
`pokemon21`  int(11) NULL DEFAULT NULL ,
`pokemon22`  int(11) NULL DEFAULT NULL ,
`pokemon23`  int(11) NULL DEFAULT NULL ,
`pokemon24`  int(11) NULL DEFAULT NULL ,
`pokemon25`  int(11) NULL DEFAULT NULL ,
`pokemon26`  int(11) NULL DEFAULT NULL ,
`pokemon27`  int(11) NULL DEFAULT NULL ,
`pokemon28`  int(11) NULL DEFAULT NULL ,
`pokemon29`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of pn_box
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for `pn_friends`
-- ----------------------------
DROP TABLE IF EXISTS `pn_friends`;
CREATE TABLE `pn_friends` (
`id`  int(11) NOT NULL ,
`friendId`  int(11) NOT NULL ,
PRIMARY KEY (`id`, `friendId`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci

;

-- ----------------------------
-- Records of pn_friends
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for `pn_history`
-- ----------------------------
DROP TABLE IF EXISTS `pn_history`;
CREATE TABLE `pn_history` (
`member`  int(11) NOT NULL ,
`action`  int(11) NOT NULL ,
`with`  int(11) NOT NULL ,
`timestamp`  datetime NULL DEFAULT NULL ,
`details`  varchar(256) CHARACTER SET ascii COLLATE ascii_general_ci NULL DEFAULT NULL ,
UNIQUE INDEX `memberitem` USING BTREE (`member`, `action`) ,
INDEX `Memberid` USING BTREE (`member`) 
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=ascii COLLATE=ascii_general_ci

;

-- ----------------------------
-- Records of pn_history
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for `pn_members`
-- ----------------------------
DROP TABLE IF EXISTS `pn_members`;
CREATE TABLE `pn_members` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`username`  varchar(12) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`password`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`dob`  varchar(12) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`email`  varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`lastLoginTime`  varchar(42) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`lastLoginServer`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`lastLoginIP`  varchar(16) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`lastLanguageUsed`  int(11) NULL DEFAULT NULL ,
`sprite`  int(11) NULL DEFAULT NULL ,
`party`  int(11) NULL DEFAULT NULL ,
`money`  int(11) NULL DEFAULT NULL ,
`npcMul`  varchar(24) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`skHerb`  int(11) NULL DEFAULT NULL ,
`skCraft`  int(11) NULL DEFAULT NULL ,
`skFish`  int(11) NULL DEFAULT NULL ,
`skTrain`  int(11) NULL DEFAULT NULL ,
`skCoord`  int(11) NULL DEFAULT NULL ,
`skBreed`  int(11) NULL DEFAULT NULL ,
`x`  int(11) NULL DEFAULT NULL ,
`y`  int(11) NULL DEFAULT NULL ,
`mapX`  int(11) NULL DEFAULT NULL ,
`mapY`  int(11) NULL DEFAULT NULL ,
`bag`  int(11) NULL DEFAULT NULL ,
`badges`  varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`healX`  int(11) NULL DEFAULT NULL ,
`healY`  int(11) NULL DEFAULT NULL ,
`healMapX`  int(11) NULL DEFAULT NULL ,
`healMapY`  int(11) NULL DEFAULT NULL ,
`isSurfing`  varchar(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`adminLevel`  int(11) NULL DEFAULT NULL ,
`muted`  varchar(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT '' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of pn_members
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for `pn_mypokes`
-- ----------------------------
DROP TABLE IF EXISTS `pn_mypokes`;
CREATE TABLE `pn_mypokes` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`member`  int(11) NULL DEFAULT NULL ,
`party`  int(11) NULL DEFAULT NULL ,
`box0`  int(11) NULL DEFAULT NULL ,
`box1`  int(11) NULL DEFAULT NULL ,
`box2`  int(11) NULL DEFAULT NULL ,
`box3`  int(11) NULL DEFAULT NULL ,
`box4`  int(11) NULL DEFAULT NULL ,
`box5`  int(11) NULL DEFAULT NULL ,
`box6`  int(11) NULL DEFAULT NULL ,
`box7`  int(11) NULL DEFAULT NULL ,
`box8`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of pn_mypokes
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for `pn_party`
-- ----------------------------
DROP TABLE IF EXISTS `pn_party`;
CREATE TABLE `pn_party` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`member`  int(11) NULL DEFAULT NULL ,
`pokemon0`  int(11) NULL DEFAULT NULL ,
`pokemon1`  int(11) NULL DEFAULT NULL ,
`pokemon2`  int(11) NULL DEFAULT NULL ,
`pokemon3`  int(11) NULL DEFAULT NULL ,
`pokemon4`  int(11) NULL DEFAULT NULL ,
`pokemon5`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of pn_party
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for `pn_pokemon`
-- ----------------------------
DROP TABLE IF EXISTS `pn_pokemon`;
CREATE TABLE `pn_pokemon` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`name`  varchar(24) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`speciesName`  varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`exp`  varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`baseExp`  int(11) NULL DEFAULT NULL ,
`expType`  varchar(16) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`isFainted`  varchar(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`level`  int(11) NULL DEFAULT NULL ,
`happiness`  int(11) NULL DEFAULT NULL ,
`gender`  int(11) NULL DEFAULT NULL ,
`nature`  varchar(24) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`abilityName`  varchar(24) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`itemName`  varchar(28) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`isShiny`  varchar(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`originalTrainerName`  varchar(12) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`currentTrainerName`  varchar(12) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`contestStats`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`move0`  varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`move1`  varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`move2`  varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`move3`  varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`hp`  int(11) NULL DEFAULT NULL ,
`atk`  int(11) NULL DEFAULT NULL ,
`def`  int(11) NULL DEFAULT NULL ,
`speed`  int(11) NULL DEFAULT NULL ,
`spATK`  int(11) NULL DEFAULT NULL ,
`spDEF`  int(11) NULL DEFAULT NULL ,
`evHP`  int(11) NULL DEFAULT NULL ,
`evATK`  int(11) NULL DEFAULT NULL ,
`evDEF`  int(11) NULL DEFAULT NULL ,
`evSPD`  int(11) NULL DEFAULT NULL ,
`evSPATK`  int(11) NULL DEFAULT NULL ,
`evSPDEF`  int(11) NULL DEFAULT NULL ,
`ivHP`  int(11) NULL DEFAULT NULL ,
`ivATK`  int(11) NULL DEFAULT NULL ,
`ivDEF`  int(11) NULL DEFAULT NULL ,
`ivSPD`  int(11) NULL DEFAULT NULL ,
`ivSPATK`  int(11) NULL DEFAULT NULL ,
`ivSPDEF`  int(11) NULL DEFAULT NULL ,
`pp0`  int(11) NULL DEFAULT NULL ,
`pp1`  int(11) NULL DEFAULT NULL ,
`pp2`  int(11) NULL DEFAULT NULL ,
`pp3`  int(11) NULL DEFAULT NULL ,
`maxpp0`  int(11) NULL DEFAULT NULL ,
`maxpp1`  int(11) NULL DEFAULT NULL ,
`maxpp2`  int(11) NULL DEFAULT NULL ,
`maxpp3`  int(11) NULL DEFAULT NULL ,
`ppUp0`  int(11) NULL DEFAULT NULL ,
`ppUp1`  int(11) NULL DEFAULT NULL ,
`ppUp2`  int(11) NULL DEFAULT NULL ,
`ppUp3`  int(11) NULL DEFAULT NULL ,
`date`  varchar(28) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
AUTO_INCREMENT=1;

-- ----------------------------
-- Records of pn_pokemon
-- ----------------------------
BEGIN;
COMMIT;

--
-- Table structure for table `pn_cheatlog`
--
DROP TABLE IF EXISTS `pn_cheatlog`;
CREATE TABLE IF NOT EXISTS `pn_cheatlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `playerid` varchar(12) DEFAULT NULL,
  `date` varchar(25) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of pn_cheatlog
-- ----------------------------
BEGIN;
COMMIT;