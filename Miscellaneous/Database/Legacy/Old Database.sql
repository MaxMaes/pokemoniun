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
AUTO_INCREMENT=1;

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
DEFAULT CHARACTER SET=ascii COLLATE=ascii_general_ci;

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
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci;

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
AUTO_INCREMENT=1;

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
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci;

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
DEFAULT CHARACTER SET=ascii COLLATE=ascii_general_ci;

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
AUTO_INCREMENT=1;

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
AUTO_INCREMENT=1;

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
AUTO_INCREMENT=1;

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
) 
ENGINE=InnoDB 
DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of pn_cheatlog
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for `pn_pokedex`
-- ----------------------------
DROP TABLE IF EXISTS `pn_pokedex`;
CREATE TABLE `pn_pokedex` (
  `pokedexId` int(11) NOT NULL AUTO_INCREMENT,
  `memberId` int(11) NOT NULL,
  `1` int(11) NOT NULL,
  `2` int(11) NOT NULL,
  `3` int(11) NOT NULL,
  `4` int(11) NOT NULL,
  `5` int(11) NOT NULL,
  `6` int(11) NOT NULL,
  `7` int(11) NOT NULL,
  `8` int(11) NOT NULL,
  `9` int(11) NOT NULL,
  `10` int(11) NOT NULL,
  `11` int(11) NOT NULL,
  `12` int(11) NOT NULL,
  `13` int(11) NOT NULL,
  `14` int(11) NOT NULL,
  `15` int(11) NOT NULL,
  `16` int(11) NOT NULL,
  `17` int(11) NOT NULL,
  `18` int(11) NOT NULL,
  `19` int(11) NOT NULL,
  `20` int(11) NOT NULL,
  `21` int(11) NOT NULL,
  `22` int(11) NOT NULL,
  `23` int(11) NOT NULL,
  `24` int(11) NOT NULL,
  `25` int(11) NOT NULL,
  `26` int(11) NOT NULL,
  `27` int(11) NOT NULL,
  `28` int(11) NOT NULL,
  `29` int(11) NOT NULL,
  `30` int(11) NOT NULL,
  `31` int(11) NOT NULL,
  `32` int(11) NOT NULL,
  `33` int(11) NOT NULL,
  `34` int(11) NOT NULL,
  `35` int(11) NOT NULL,
  `36` int(11) NOT NULL,
  `37` int(11) NOT NULL,
  `38` int(11) NOT NULL,
  `39` int(11) NOT NULL,
  `40` int(11) NOT NULL,
  `41` int(11) NOT NULL,
  `42` int(11) NOT NULL,
  `43` int(11) NOT NULL,
  `44` int(11) NOT NULL,
  `45` int(11) NOT NULL,
  `46` int(11) NOT NULL,
  `47` int(11) NOT NULL,
  `48` int(11) NOT NULL,
  `49` int(11) NOT NULL,
  `50` int(11) NOT NULL,
  `51` int(11) NOT NULL,
  `52` int(11) NOT NULL,
  `53` int(11) NOT NULL,
  `54` int(11) NOT NULL,
  `55` int(11) NOT NULL,
  `56` int(11) NOT NULL,
  `57` int(11) NOT NULL,
  `58` int(11) NOT NULL,
  `59` int(11) NOT NULL,
  `60` int(11) NOT NULL,
  `61` int(11) NOT NULL,
  `62` int(11) NOT NULL,
  `63` int(11) NOT NULL,
  `64` int(11) NOT NULL,
  `65` int(11) NOT NULL,
  `66` int(11) NOT NULL,
  `67` int(11) NOT NULL,
  `68` int(11) NOT NULL,
  `69` int(11) NOT NULL,
  `70` int(11) NOT NULL,
  `71` int(11) NOT NULL,
  `72` int(11) NOT NULL,
  `73` int(11) NOT NULL,
  `74` int(11) NOT NULL,
  `75` int(11) NOT NULL,
  `76` int(11) NOT NULL,
  `77` int(11) NOT NULL,
  `78` int(11) NOT NULL,
  `79` int(11) NOT NULL,
  `80` int(11) NOT NULL,
  `81` int(11) NOT NULL,
  `82` int(11) NOT NULL,
  `83` int(11) NOT NULL,
  `84` int(11) NOT NULL,
  `85` int(11) NOT NULL,
  `86` int(11) NOT NULL,
  `87` int(11) NOT NULL,
  `88` int(11) NOT NULL,
  `89` int(11) NOT NULL,
  `90` int(11) NOT NULL,
  `91` int(11) NOT NULL,
  `92` int(11) NOT NULL,
  `93` int(11) NOT NULL,
  `94` int(11) NOT NULL,
  `95` int(11) NOT NULL,
  `96` int(11) NOT NULL,
  `97` int(11) NOT NULL,
  `98` int(11) NOT NULL,
  `99` int(11) NOT NULL,
  `100` int(11) NOT NULL,
  `101` int(11) NOT NULL,
  `102` int(11) NOT NULL,
  `103` int(11) NOT NULL,
  `104` int(11) NOT NULL,
  `105` int(11) NOT NULL,
  `106` int(11) NOT NULL,
  `107` int(11) NOT NULL,
  `108` int(11) NOT NULL,
  `109` int(11) NOT NULL,
  `110` int(11) NOT NULL,
  `111` int(11) NOT NULL,
  `112` int(11) NOT NULL,
  `113` int(11) NOT NULL,
  `114` int(11) NOT NULL,
  `115` int(11) NOT NULL,
  `116` int(11) NOT NULL,
  `117` int(11) NOT NULL,
  `118` int(11) NOT NULL,
  `119` int(11) NOT NULL,
  `120` int(11) NOT NULL,
  `121` int(11) NOT NULL,
  `122` int(11) NOT NULL,
  `123` int(11) NOT NULL,
  `124` int(11) NOT NULL,
  `125` int(11) NOT NULL,
  `126` int(11) NOT NULL,
  `127` int(11) NOT NULL,
  `128` int(11) NOT NULL,
  `129` int(11) NOT NULL,
  `130` int(11) NOT NULL,
  `131` int(11) NOT NULL,
  `132` int(11) NOT NULL,
  `133` int(11) NOT NULL,
  `134` int(11) NOT NULL,
  `135` int(11) NOT NULL,
  `136` int(11) NOT NULL,
  `137` int(11) NOT NULL,
  `138` int(11) NOT NULL,
  `139` int(11) NOT NULL,
  `140` int(11) NOT NULL,
  `141` int(11) NOT NULL,
  `142` int(11) NOT NULL,
  `143` int(11) NOT NULL,
  `144` int(11) NOT NULL,
  `145` int(11) NOT NULL,
  `146` int(11) NOT NULL,
  `147` int(11) NOT NULL,
  `148` int(11) NOT NULL,
  `149` int(11) NOT NULL,
  `150` int(11) NOT NULL,
  `151` int(11) NOT NULL,
  `152` int(11) NOT NULL,
  `153` int(11) NOT NULL,
  `154` int(11) NOT NULL,
  `155` int(11) NOT NULL,
  `156` int(11) NOT NULL,
  `157` int(11) NOT NULL,
  `158` int(11) NOT NULL,
  `159` int(11) NOT NULL,
  `160` int(11) NOT NULL,
  `161` int(11) NOT NULL,
  `162` int(11) NOT NULL,
  `163` int(11) NOT NULL,
  `164` int(11) NOT NULL,
  `165` int(11) NOT NULL,
  `166` int(11) NOT NULL,
  `167` int(11) NOT NULL,
  `168` int(11) NOT NULL,
  `169` int(11) NOT NULL,
  `170` int(11) NOT NULL,
  `171` int(11) NOT NULL,
  `172` int(11) NOT NULL,
  `173` int(11) NOT NULL,
  `174` int(11) NOT NULL,
  `175` int(11) NOT NULL,
  `176` int(11) NOT NULL,
  `177` int(11) NOT NULL,
  `178` int(11) NOT NULL,
  `179` int(11) NOT NULL,
  `180` int(11) NOT NULL,
  `181` int(11) NOT NULL,
  `182` int(11) NOT NULL,
  `183` int(11) NOT NULL,
  `184` int(11) NOT NULL,
  `185` int(11) NOT NULL,
  `186` int(11) NOT NULL,
  `187` int(11) NOT NULL,
  `188` int(11) NOT NULL,
  `189` int(11) NOT NULL,
  `190` int(11) NOT NULL,
  `191` int(11) NOT NULL,
  `192` int(11) NOT NULL,
  `193` int(11) NOT NULL,
  `194` int(11) NOT NULL,
  `195` int(11) NOT NULL,
  `196` int(11) NOT NULL,
  `197` int(11) NOT NULL,
  `198` int(11) NOT NULL,
  `199` int(11) NOT NULL,
  `200` int(11) NOT NULL,
  `201` int(11) NOT NULL,
  `202` int(11) NOT NULL,
  `203` int(11) NOT NULL,
  `204` int(11) NOT NULL,
  `205` int(11) NOT NULL,
  `206` int(11) NOT NULL,
  `207` int(11) NOT NULL,
  `208` int(11) NOT NULL,
  `209` int(11) NOT NULL,
  `210` int(11) NOT NULL,
  `211` int(11) NOT NULL,
  `212` int(11) NOT NULL,
  `213` int(11) NOT NULL,
  `214` int(11) NOT NULL,
  `215` int(11) NOT NULL,
  `216` int(11) NOT NULL,
  `217` int(11) NOT NULL,
  `218` int(11) NOT NULL,
  `219` int(11) NOT NULL,
  `220` int(11) NOT NULL,
  `221` int(11) NOT NULL,
  `222` int(11) NOT NULL,
  `223` int(11) NOT NULL,
  `224` int(11) NOT NULL,
  `225` int(11) NOT NULL,
  `226` int(11) NOT NULL,
  `227` int(11) NOT NULL,
  `228` int(11) NOT NULL,
  `229` int(11) NOT NULL,
  `230` int(11) NOT NULL,
  `231` int(11) NOT NULL,
  `232` int(11) NOT NULL,
  `233` int(11) NOT NULL,
  `234` int(11) NOT NULL,
  `235` int(11) NOT NULL,
  `236` int(11) NOT NULL,
  `237` int(11) NOT NULL,
  `238` int(11) NOT NULL,
  `239` int(11) NOT NULL,
  `240` int(11) NOT NULL,
  `241` int(11) NOT NULL,
  `242` int(11) NOT NULL,
  `243` int(11) NOT NULL,
  `244` int(11) NOT NULL,
  `245` int(11) NOT NULL,
  `246` int(11) NOT NULL,
  `247` int(11) NOT NULL,
  `248` int(11) NOT NULL,
  `249` int(11) NOT NULL,
  `250` int(11) NOT NULL,
  `251` int(11) NOT NULL,
  `252` int(11) NOT NULL,
  `253` int(11) NOT NULL,
  `254` int(11) NOT NULL,
  `255` int(11) NOT NULL,
  `256` int(11) NOT NULL,
  `257` int(11) NOT NULL,
  `258` int(11) NOT NULL,
  `259` int(11) NOT NULL,
  `260` int(11) NOT NULL,
  `261` int(11) NOT NULL,
  `262` int(11) NOT NULL,
  `263` int(11) NOT NULL,
  `264` int(11) NOT NULL,
  `265` int(11) NOT NULL,
  `266` int(11) NOT NULL,
  `267` int(11) NOT NULL,
  `268` int(11) NOT NULL,
  `269` int(11) NOT NULL,
  `270` int(11) NOT NULL,
  `271` int(11) NOT NULL,
  `272` int(11) NOT NULL,
  `273` int(11) NOT NULL,
  `274` int(11) NOT NULL,
  `275` int(11) NOT NULL,
  `276` int(11) NOT NULL,
  `277` int(11) NOT NULL,
  `278` int(11) NOT NULL,
  `279` int(11) NOT NULL,
  `280` int(11) NOT NULL,
  `281` int(11) NOT NULL,
  `282` int(11) NOT NULL,
  `283` int(11) NOT NULL,
  `284` int(11) NOT NULL,
  `285` int(11) NOT NULL,
  `286` int(11) NOT NULL,
  `287` int(11) NOT NULL,
  `288` int(11) NOT NULL,
  `289` int(11) NOT NULL,
  `290` int(11) NOT NULL,
  `291` int(11) NOT NULL,
  `292` int(11) NOT NULL,
  `293` int(11) NOT NULL,
  `294` int(11) NOT NULL,
  `295` int(11) NOT NULL,
  `296` int(11) NOT NULL,
  `297` int(11) NOT NULL,
  `298` int(11) NOT NULL,
  `299` int(11) NOT NULL,
  `300` int(11) NOT NULL,
  `301` int(11) NOT NULL,
  `302` int(11) NOT NULL,
  `303` int(11) NOT NULL,
  `304` int(11) NOT NULL,
  `305` int(11) NOT NULL,
  `306` int(11) NOT NULL,
  `307` int(11) NOT NULL,
  `308` int(11) NOT NULL,
  `309` int(11) NOT NULL,
  `310` int(11) NOT NULL,
  `311` int(11) NOT NULL,
  `312` int(11) NOT NULL,
  `313` int(11) NOT NULL,
  `314` int(11) NOT NULL,
  `315` int(11) NOT NULL,
  `316` int(11) NOT NULL,
  `317` int(11) NOT NULL,
  `318` int(11) NOT NULL,
  `319` int(11) NOT NULL,
  `320` int(11) NOT NULL,
  `321` int(11) NOT NULL,
  `322` int(11) NOT NULL,
  `323` int(11) NOT NULL,
  `324` int(11) NOT NULL,
  `325` int(11) NOT NULL,
  `326` int(11) NOT NULL,
  `327` int(11) NOT NULL,
  `328` int(11) NOT NULL,
  `329` int(11) NOT NULL,
  `330` int(11) NOT NULL,
  `331` int(11) NOT NULL,
  `332` int(11) NOT NULL,
  `333` int(11) NOT NULL,
  `334` int(11) NOT NULL,
  `335` int(11) NOT NULL,
  `336` int(11) NOT NULL,
  `337` int(11) NOT NULL,
  `338` int(11) NOT NULL,
  `339` int(11) NOT NULL,
  `340` int(11) NOT NULL,
  `341` int(11) NOT NULL,
  `342` int(11) NOT NULL,
  `343` int(11) NOT NULL,
  `344` int(11) NOT NULL,
  `345` int(11) NOT NULL,
  `346` int(11) NOT NULL,
  `347` int(11) NOT NULL,
  `348` int(11) NOT NULL,
  `349` int(11) NOT NULL,
  `350` int(11) NOT NULL,
  `351` int(11) NOT NULL,
  `352` int(11) NOT NULL,
  `353` int(11) NOT NULL,
  `354` int(11) NOT NULL,
  `355` int(11) NOT NULL,
  `356` int(11) NOT NULL,
  `357` int(11) NOT NULL,
  `358` int(11) NOT NULL,
  `359` int(11) NOT NULL,
  `360` int(11) NOT NULL,
  `361` int(11) NOT NULL,
  `362` int(11) NOT NULL,
  `363` int(11) NOT NULL,
  `364` int(11) NOT NULL,
  `365` int(11) NOT NULL,
  `366` int(11) NOT NULL,
  `367` int(11) NOT NULL,
  `368` int(11) NOT NULL,
  `369` int(11) NOT NULL,
  `370` int(11) NOT NULL,
  `371` int(11) NOT NULL,
  `372` int(11) NOT NULL,
  `373` int(11) NOT NULL,
  `374` int(11) NOT NULL,
  `375` int(11) NOT NULL,
  `376` int(11) NOT NULL,
  `377` int(11) NOT NULL,
  `378` int(11) NOT NULL,
  `379` int(11) NOT NULL,
  `380` int(11) NOT NULL,
  `381` int(11) NOT NULL,
  `382` int(11) NOT NULL,
  `383` int(11) NOT NULL,
  `384` int(11) NOT NULL,
  `385` int(11) NOT NULL,
  `386` int(11) NOT NULL,
  `387` int(11) NOT NULL,
  `388` int(11) NOT NULL,
  `389` int(11) NOT NULL,
  `390` int(11) NOT NULL,
  `391` int(11) NOT NULL,
  `392` int(11) NOT NULL,
  `393` int(11) NOT NULL,
  `394` int(11) NOT NULL,
  `395` int(11) NOT NULL,
  `396` int(11) NOT NULL,
  `397` int(11) NOT NULL,
  `398` int(11) NOT NULL,
  `399` int(11) NOT NULL,
  `400` int(11) NOT NULL,
  `401` int(11) NOT NULL,
  `402` int(11) NOT NULL,
  `403` int(11) NOT NULL,
  `404` int(11) NOT NULL,
  `405` int(11) NOT NULL,
  `406` int(11) NOT NULL,
  `407` int(11) NOT NULL,
  `408` int(11) NOT NULL,
  `409` int(11) NOT NULL,
  `410` int(11) NOT NULL,
  `411` int(11) NOT NULL,
  `412` int(11) NOT NULL,
  `413` int(11) NOT NULL,
  `414` int(11) NOT NULL,
  `415` int(11) NOT NULL,
  `416` int(11) NOT NULL,
  `417` int(11) NOT NULL,
  `418` int(11) NOT NULL,
  `419` int(11) NOT NULL,
  `420` int(11) NOT NULL,
  `421` int(11) NOT NULL,
  `422` int(11) NOT NULL,
  `423` int(11) NOT NULL,
  `424` int(11) NOT NULL,
  `425` int(11) NOT NULL,
  `426` int(11) NOT NULL,
  `427` int(11) NOT NULL,
  `428` int(11) NOT NULL,
  `429` int(11) NOT NULL,
  `430` int(11) NOT NULL,
  `431` int(11) NOT NULL,
  `432` int(11) NOT NULL,
  `433` int(11) NOT NULL,
  `434` int(11) NOT NULL,
  `435` int(11) NOT NULL,
  `436` int(11) NOT NULL,
  `437` int(11) NOT NULL,
  `438` int(11) NOT NULL,
  `439` int(11) NOT NULL,
  `440` int(11) NOT NULL,
  `441` int(11) NOT NULL,
  `442` int(11) NOT NULL,
  `443` int(11) NOT NULL,
  `444` int(11) NOT NULL,
  `445` int(11) NOT NULL,
  `446` int(11) NOT NULL,
  `447` int(11) NOT NULL,
  `448` int(11) NOT NULL,
  `449` int(11) NOT NULL,
  `450` int(11) NOT NULL,
  `451` int(11) NOT NULL,
  `452` int(11) NOT NULL,
  `453` int(11) NOT NULL,
  `454` int(11) NOT NULL,
  `455` int(11) NOT NULL,
  `456` int(11) NOT NULL,
  `457` int(11) NOT NULL,
  `458` int(11) NOT NULL,
  `459` int(11) NOT NULL,
  `460` int(11) NOT NULL,
  `461` int(11) NOT NULL,
  `462` int(11) NOT NULL,
  `463` int(11) NOT NULL,
  `464` int(11) NOT NULL,
  `465` int(11) NOT NULL,
  `466` int(11) NOT NULL,
  `467` int(11) NOT NULL,
  `468` int(11) NOT NULL,
  `469` int(11) NOT NULL,
  `470` int(11) NOT NULL,
  `471` int(11) NOT NULL,
  `472` int(11) NOT NULL,
  `473` int(11) NOT NULL,
  `474` int(11) NOT NULL,
  `475` int(11) NOT NULL,
  `476` int(11) NOT NULL,
  `477` int(11) NOT NULL,
  `478` int(11) NOT NULL,
  `479` int(11) NOT NULL,
  `480` int(11) NOT NULL,
  `481` int(11) NOT NULL,
  `482` int(11) NOT NULL,
  `483` int(11) NOT NULL,
  `484` int(11) NOT NULL,
  `485` int(11) NOT NULL,
  `486` int(11) NOT NULL,
  `487` int(11) NOT NULL,
  `488` int(11) NOT NULL,
  `489` int(11) NOT NULL,
  `490` int(11) NOT NULL,
  `491` int(11) NOT NULL,
  `492` int(11) NOT NULL,
  `493` int(11) NOT NULL,
  PRIMARY KEY (`pokedexId`),
  UNIQUE KEY `pokedexId` (`pokedexId`)
) 
ENGINE=InnoDB 
AUTO_INCREMENT=1 
DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of pn_pokedex
-- ----------------------------

BEGIN;
COMMIT;