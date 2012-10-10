# --------------------------------------------------------
# Host:                         127.0.0.1
# Server version:               5.5.8
# Server OS:                    Win32
# HeidiSQL version:             6.0.0.3603
# Date/time:                    2012-09-11 22:06:17
# --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

# Dumping structure for table pokemon.pn_bag
CREATE TABLE IF NOT EXISTS `pn_bag` (
  `member` int(11) NOT NULL,
  `item` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  UNIQUE KEY `memberitem` (`member`,`item`),
  KEY `Memberid` (`member`)
) ENGINE=MyISAM DEFAULT CHARSET=ascii;

# Dumping data for table pokemon.pn_bag: 23 rows
/*!40000 ALTER TABLE `pn_bag` DISABLE KEYS */;
INSERT INTO `pn_bag` (`member`, `item`, `quantity`) VALUES
	(6, 35, 5),
	(6, 206, 3),
	(6, 207, 2),
	(5, 35, 5),
	(4, 206, 3),
	(4, 35, 2),
	(7, 584, 1),
	(7, 207, 4),
	(7, 206, 23),
	(7, 202, 2),
	(7, 35, 2),
	(8, 35, 5),
	(9, 35, 5),
	(8, 206, 2),
	(8, 202, 1),
	(8, 207, 3),
	(10, 207, 10),
	(10, 35, 4),
	(10, 206, 30),
	(11, 35, 4),
	(11, 207, 1),
	(12, 35, 5),
	(11, 206, 1);
/*!40000 ALTER TABLE `pn_bag` ENABLE KEYS */;


# Dumping structure for table pokemon.pn_bans
CREATE TABLE IF NOT EXISTS `pn_bans` (
  `ip` varchar(48) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

# Dumping data for table pokemon.pn_bans: ~0 rows (approximately)
/*!40000 ALTER TABLE `pn_bans` DISABLE KEYS */;
/*!40000 ALTER TABLE `pn_bans` ENABLE KEYS */;


# Dumping structure for table pokemon.pn_history
CREATE TABLE IF NOT EXISTS `pn_history` (
  `member` int(11) NOT NULL,
  `action` int(11) NOT NULL,
  `with` int(11) NOT NULL,
  `timestamp` datetime DEFAULT NULL,
  `details` varchar(256) DEFAULT NULL,
  UNIQUE KEY `memberitem` (`member`,`action`),
  KEY `Memberid` (`member`)
) ENGINE=MyISAM DEFAULT CHARSET=ascii;

# Dumping data for table pokemon.pn_history: 0 rows
/*!40000 ALTER TABLE `pn_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `pn_history` ENABLE KEYS */;


# Dumping structure for table pokemon.pn_members
CREATE TABLE IF NOT EXISTS `pn_members` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(12) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `dob` varchar(12) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `lastLoginTime` varchar(42) DEFAULT NULL,
  `lastLoginServer` varchar(24) CHARACTER SET utf8 DEFAULT NULL,
  `lastLoginIP` varchar(16) DEFAULT NULL,
  `lastLanguageUsed` int(11) DEFAULT NULL,
  `sprite` int(11) DEFAULT NULL,
  `party` int(11) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `npcMul` varchar(24) DEFAULT NULL,
  `skHerb` int(11) DEFAULT NULL,
  `skCraft` int(11) DEFAULT NULL,
  `skFish` int(11) DEFAULT NULL,
  `skTrain` int(11) DEFAULT NULL,
  `skCoord` int(11) DEFAULT NULL,
  `skBreed` int(11) DEFAULT NULL,
  `x` int(11) DEFAULT NULL,
  `y` int(11) DEFAULT NULL,
  `mapX` int(11) DEFAULT NULL,
  `mapY` int(11) DEFAULT NULL,
  `bag` int(11) DEFAULT NULL,
  `badges` varchar(50) DEFAULT NULL,
  `healX` int(11) DEFAULT NULL,
  `healY` int(11) DEFAULT NULL,
  `healMapX` int(11) DEFAULT NULL,
  `healMapY` int(11) DEFAULT NULL,
  `isSurfing` varchar(5) DEFAULT NULL,
  `adminLevel` int(11) DEFAULT NULL,
  `muted` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

# Dumping data for table pokemon.pn_members: ~9 rows (approximately)
/*!40000 ALTER TABLE `pn_members` DISABLE KEYS */;
INSERT INTO `pn_members` (`id`, `username`, `password`, `dob`, `email`, `lastLoginTime`, `lastLoginServer`, `lastLoginIP`, `lastLanguageUsed`, `sprite`, `party`, `money`, `npcMul`, `skHerb`, `skCraft`, `skFish`, `skTrain`, `skCoord`, `skBreed`, `x`, `y`, `mapX`, `mapY`, `bag`, `badges`, `healX`, `healY`, `healMapX`, `healMapY`, `isSurfing`, `adminLevel`, `muted`) VALUES
	(4, 'test', '098f6bcd4621d373cade4e832627b4f6', '1/1/1991', 'asd@mail.com', '1288717683334', 'null', '127.0.0.1', 0, 11, 4, 76, NULL, 0, 0, 0, 117, 0, 0, 1248, 440, 2, -1, NULL, '000000000000000000000000000000000000000000', 256, 120, -50, -29, 'false', 100, 'false'),
	(5, 'test2', '93CE35698C293965FB636B30A1083880C34BE828FAE708FC8C400643D446F709459BDAE22E4B3E6DE60186991DA9D4C585A5D7897830D671664836B1D1EBC3AA', '1/1/1991', 'test2@mail.com', '0', 'null', NULL, NULL, 20, 5, 0, NULL, 0, 0, 0, 0, 0, 0, 512, 440, 3, 1, NULL, '00000000000000000000000000000000000000000000000000', 512, 440, 3, 1, 'false', 0, 'false'),
	(6, 'test3', 'B477CACBAA05271A6B9FA238E2E2F7442A1304DCE4C15A5A421FE3F113740A7DE9910AD468189DD2F73495440A1D71EC68244616308AF6DE262776DB86F3DD5B', '1/1/1991', 'test3@mail.com', '1288800080461', 'null', '127.0.0.1', 0, 20, 6, 25, NULL, 0, 0, 0, 68, 0, 0, 384, 248, 3, 1, NULL, '000000000000000000000000000000000000000000', 224, 120, -48, -14, 'false', 0, 'false'),
	(7, 'Anomis', 'F3E05E36FDC611A99447FB3A606CB90FD70841C233159BB383622FD8A77C3FDC30DF4027DC4F3FAF68C1BEA88394B36A0A9E74CA3310826294D0DA3F83E36600', '23/8/1991', 'anomi_s@rocketmail.com', '1288865888826', 'null', '127.0.0.1', 0, 11, 7, 350, NULL, 0, 0, 0, 361, 0, 0, 480, 376, -49, -44, NULL, '000000000000000000000000000000000000000000', 480, 376, -49, -44, 'false', 0, 'false'),
	(8, 'default', '56A332C88DECD8D4A1FBFDE687E27642842AEA2D2D1A1A1B1D0E578DED1430F35FFB9FA69A950DA07A49549545B5F063DA3865B6EF134AA0C46AB75DF823E827', '01/06/1990', 'd.faultx@Gmail.com', '1306359715132', 'null', '127.0.0.1', 0, 150, 8, 249, NULL, 0, 0, 0, 335, 0, 0, 2208, 216, 4, -3, NULL, '100000000000000000000000000000000000000000', 480, 376, -47, -31, 'false', 0, 'false'),
	(9, 'Jesus', '9FE2B4DB04CC8DA0BBEB11D217DECA3F9CFAF3A36A03EFAFC60F8161975B6BA324834BFB324198A05B23B92FAF36C32E1DE0742572440D915D8FCA790D80579B', '01/01/1990', 'jesuslives@jesus.com', '1306044282729', 'null', '127.0.0.1', 0, 11, 9, 0, NULL, 0, 0, 0, 0, 0, 0, 512, 440, 3, 1, NULL, '000000000000000000000000000000000000000000', 512, 440, 3, 1, 'false', 100, 'false'),
	(10, 'Faintful', 'CAAAC8AF75872E55685BE43B2C231DD06C597964B6152AFDBA8E706F3E75CA506553EB1C0DBAD5D171A6BC8B94C0BA0337F2EAD7D4F625F3CABD0170297DA293', '08/06/1995', 'noob@noob.nl', '1347388792097', 'null', '/127.0.0.1:8188', 0, 65, 10, 7394, NULL, 0, 0, 0, 566, 0, 0, 128, 1112, 3, 0, NULL, '000000000000000000000000000000000000000000', 224, 120, -48, -14, 'false', 2, 'false'),
	(11, 'Connor', '47808D7B40B9DB986B6806D95AAE4DE83923F8CA881EC5F190EBBECAB24355C79F0DF7622F3A9D7FA81EBE0508C5DA3D3A5B65896482F76E37F8FFA0280F1ED9', '21/06/1994', 'jul-bakker@hotmail.com', '1344352284236', 'null', '82.73.38.160', 0, 11, 11, 115, NULL, 0, 0, 0, 58, 0, 0, 512, 1016, 3, 0, NULL, '000000000000000000000000000000000000000000', 224, 120, -48, -14, 'false', 0, 'false'),
	(12, 'Faintful2', '6D7580429DD3F47C7496442581DA598342A3E3C2078F299AB0C2829920891031A64BF218377B49B0F5084F212C351C307910C68D05522A36345DD517277AD9C9', '08/06/1995', 'noob2@noob.nl', '1347388796374', 'null', '/127.0.0.1:8199', 0, 11, 12, 35, NULL, 0, 0, 0, 30, 0, 0, 224, 1080, 3, 0, NULL, '000000000000000000000000000000000000000000', 224, 120, -48, -14, 'false', 0, 'false');
/*!40000 ALTER TABLE `pn_members` ENABLE KEYS */;


# Dumping structure for table pokemon.pn_party
CREATE TABLE IF NOT EXISTS `pn_party` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `member` int(11) DEFAULT NULL,
  `pokemon0` int(11) DEFAULT NULL,
  `pokemon1` int(11) DEFAULT NULL,
  `pokemon2` int(11) DEFAULT NULL,
  `pokemon3` int(11) DEFAULT NULL,
  `pokemon4` int(11) DEFAULT NULL,
  `pokemon5` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

# Dumping data for table pokemon.pn_party: ~12 rows (approximately)
/*!40000 ALTER TABLE `pn_party` DISABLE KEYS */;
INSERT INTO `pn_party` (`id`, `member`, `pokemon0`, `pokemon1`, `pokemon2`, `pokemon3`, `pokemon4`, `pokemon5`) VALUES
	(1, 1, -1, -1, -1, -1, -1, -1),
	(2, 2, -1, -1, -1, -1, -1, -1),
	(3, 3, -1, -1, -1, -1, -1, -1),
	(4, 4, 1, -1, -1, -1, -1, -1),
	(5, 5, 4, -1, -1, -1, -1, -1),
	(6, 6, 5, 6, -1, -1, -1, -1),
	(7, 7, 8, 7, -1, 10, -1, -1),
	(8, 8, 11, -1, -1, -1, -1, -1),
	(9, 9, 12, -1, -1, -1, -1, -1),
	(10, 10, 13, 14, 17, -1, -1, -1),
	(11, 11, 16, -1, -1, -1, -1, -1),
	(12, 12, 18, -1, -1, -1, -1, -1);
/*!40000 ALTER TABLE `pn_party` ENABLE KEYS */;


# Dumping structure for table pokemon.pn_pokemon
CREATE TABLE IF NOT EXISTS `pn_pokemon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(24) DEFAULT NULL,
  `speciesName` varchar(32) DEFAULT NULL,
  `exp` varchar(32) DEFAULT NULL,
  `baseExp` int(11) DEFAULT NULL,
  `expType` varchar(16) DEFAULT NULL,
  `isFainted` varchar(5) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `happiness` int(11) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `nature` varchar(24) DEFAULT NULL,
  `abilityName` varchar(24) DEFAULT NULL,
  `itemName` varchar(28) DEFAULT NULL,
  `isShiny` varchar(5) DEFAULT NULL,
  `originalTrainerName` varchar(12) DEFAULT NULL,
  `currentTrainerName` varchar(12) DEFAULT NULL,
  `contestStats` varchar(255) DEFAULT NULL,
  `move0` varchar(32) DEFAULT NULL,
  `move1` varchar(32) DEFAULT NULL,
  `move2` varchar(32) DEFAULT NULL,
  `move3` varchar(32) DEFAULT NULL,
  `hp` int(11) DEFAULT NULL,
  `atk` int(11) DEFAULT NULL,
  `def` int(11) DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `spATK` int(11) DEFAULT NULL,
  `spDEF` int(11) DEFAULT NULL,
  `evHP` int(11) DEFAULT NULL,
  `evATK` int(11) DEFAULT NULL,
  `evDEF` int(11) DEFAULT NULL,
  `evSPD` int(11) DEFAULT NULL,
  `evSPATK` int(11) DEFAULT NULL,
  `evSPDEF` int(11) DEFAULT NULL,
  `ivHP` int(11) DEFAULT NULL,
  `ivATK` int(11) DEFAULT NULL,
  `ivDEF` int(11) DEFAULT NULL,
  `ivSPD` int(11) DEFAULT NULL,
  `ivSPATK` int(11) DEFAULT NULL,
  `ivSPDEF` int(11) DEFAULT NULL,
  `pp0` int(11) DEFAULT NULL,
  `pp1` int(11) DEFAULT NULL,
  `pp2` int(11) DEFAULT NULL,
  `pp3` int(11) DEFAULT NULL,
  `maxpp0` int(11) DEFAULT NULL,
  `maxpp1` int(11) DEFAULT NULL,
  `maxpp2` int(11) DEFAULT NULL,
  `maxpp3` int(11) DEFAULT NULL,
  `ppUp0` int(11) DEFAULT NULL,
  `ppUp1` int(11) DEFAULT NULL,
  `ppUp2` int(11) DEFAULT NULL,
  `ppUp3` int(11) DEFAULT NULL,
  `date` varchar(28) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

# Dumping data for table pokemon.pn_pokemon: ~18 rows (approximately)
/*!40000 ALTER TABLE `pn_pokemon` DISABLE KEYS */;
INSERT INTO `pn_pokemon` (`id`, `name`, `speciesName`, `exp`, `baseExp`, `expType`, `isFainted`, `level`, `happiness`, `gender`, `nature`, `abilityName`, `itemName`, `isShiny`, `originalTrainerName`, `currentTrainerName`, `contestStats`, `move0`, `move1`, `move2`, `move3`, `hp`, `atk`, `def`, `speed`, `spATK`, `spDEF`, `evHP`, `evATK`, `evDEF`, `evSPD`, `evSPATK`, `evSPDEF`, `ivHP`, `ivATK`, `ivDEF`, `ivSPD`, `ivSPATK`, `ivSPDEF`, `pp0`, `pp1`, `pp2`, `pp3`, `maxpp0`, `maxpp1`, `maxpp2`, `maxpp3`, `ppUp0`, `ppUp1`, `ppUp2`, `ppUp3`, `date`) VALUES
	(1, 'Charmander', 'Charmander', '357.5', 65, 'PARABOLIC', 'false', 8, 74, 1, 'Quirky', 'Blaze', '', 'false', 'test', 'test', '0,0,0,0,0', 'Scratch', 'Growl', 'Ember', 'null', 24, 13, 13, 17, 15, 13, 0, 0, 0, 0, 0, 0, 13, 0, 17, 29, 7, 12, 33, 40, 25, -1, 35, 40, 25, -1, 0, 0, 0, 0, '2010-11-01:22-10-55'),
	(2, 'Rattata', 'Rattata', '125.0', 57, 'MEDIUM', 'false', 5, 70, 1, 'Bold', '', '', 'false', 'test', 'test', '0,0,0,0,0', 'Tackle', 'Tail Whip', 'null', 'null', 19, 10, 9, 13, 7, 9, 0, 0, 0, 0, 0, 0, 29, 30, 28, 26, 8, 17, 35, 30, -1, -1, 35, 30, -1, -1, 0, 0, 0, 0, '2010-11-02:10-20-40'),
	(3, 'Pidgey', 'Pidgey', '57.4', 55, 'PARABOLIC', 'false', 3, 70, 2, 'Impish', '', '', 'false', 'test', 'test', '0,0,0,0,0', 'Tackle', 'null', 'null', 'null', 16, 8, 8, 8, 6, 7, 0, 0, 0, 0, 0, 0, 28, 20, 25, 17, 13, 17, 35, -1, -1, -1, 35, -1, -1, -1, 0, 0, 0, 0, '2010-11-02:10-38-00'),
	(4, 'Bulbasaur', 'Bulbasaur', '135.0', 64, 'PARABOLIC', 'false', 5, 70, 1, 'Quirky', 'Overgrow', '', 'false', 'test2', 'test2', '0,0,0,0,0', 'Tackle', 'null', 'Growl', 'null', 19, 10, 10, 10, 11, 12, 0, 0, 0, 0, 0, 0, 9, 15, 13, 29, 2, 21, 35, -1, 40, -1, 35, -1, 40, -1, 0, 0, 0, 0, '2010-11-02:20-03-07'),
	(5, 'Bulbasaur', 'Bulbasaur', '253.5', 64, 'PARABOLIC', 'false', 7, 72, 1, 'Quirky', 'Overgrow', '', 'false', 'test3', 'test3', '0,0,0,0,0', 'Tackle', 'Leech Seed', 'Growl', 'null', 12, 12, 11, 12, 14, 15, 0, 0, 0, 0, 0, 0, 2, 5, 0, 17, 7, 27, 31, 9, 40, -1, 35, 10, 40, -1, 0, 0, 0, 0, '2010-11-02:20-05-08'),
	(6, 'Hoothoot', 'Hoothoot', '16.0', 58, 'MEDIUM', 'false', 2, 70, 2, 'Careful', '', '', 'false', 'test3', 'test3', '0,0,0,0,0', 'Tackle', 'Growl', 'null', 'null', 14, 6, 6, 7, 6, 7, 0, 0, 0, 0, 0, 0, 19, 8, 21, 3, 28, 11, 35, 40, -1, -1, 35, 40, -1, -1, 0, 0, 0, 0, '2010-11-02:21-51-41'),
	(7, 'Charmander', 'Charmander', '1199.0', 65, 'PARABOLIC', 'false', 12, 20, 1, 'Quirky', 'Blaze', '', 'false', 'Anomis', 'Anomis', '0,0,0,0,0', 'Scratch', 'Growl', 'Ember', 'null', 34, 19, 17, 22, 21, 19, 0, 0, 0, 0, 0, 0, 27, 15, 17, 14, 16, 22, 35, 40, 25, -1, 35, 40, 25, -1, 0, 0, 0, 0, '2010-11-02:23-54-27'),
	(8, 'Geodude', 'Geodude', '134.1', 86, 'PARABOLIC', 'false', 4, 20, 1, 'Serious', '', '', 'false', 'Anomis', 'Anomis', '0,0,0,0,0', 'Tackle', 'Defense Curl', 'null', 'null', 18, 12, 14, 7, 7, 8, 0, 0, 0, 0, 0, 0, 23, 22, 27, 22, 2, 20, 35, 40, -1, -1, 35, 40, -1, -1, 0, 0, 0, 0, '2010-11-03:19-22-51'),
	(9, 'Jigglypuff', 'Jigglypuff', '53.7', 76, 'FAST', 'false', 4, 70, 1, 'Mild', '', '', 'false', 'Anomis', 'Anomis', '0,0,0,0,0', 'Sing', 'null', 'Defense Curl', 'null', 24, 9, 6, 6, 9, 7, 0, 0, 0, 0, 0, 0, 27, 31, 14, 8, 29, 13, 15, -1, 40, -1, 15, -1, 40, -1, 0, 0, 0, 0, '2010-11-03:19-23-47'),
	(10, 'Poliwag', 'Poliwag', '96.8', 77, 'PARABOLIC', 'false', 4, 20, 1, 'Careful', '', '', 'false', 'Anomis', 'Anomis', '0,0,0,0,0', 'Bubble', 'null', 'null', 'null', 17, 9, 8, 13, 7, 9, 0, 0, 0, 0, 0, 0, 18, 7, 4, 27, 11, 24, 30, -1, -1, -1, 30, -1, -1, -1, 0, 0, 0, 0, '2010-11-04:12-28-49'),
	(11, 'Charmander', 'Charmander', '643.0', 65, 'PARABOLIC', 'false', 10, 24, 1, 'Quirky', 'Blaze', '', 'false', 'default', 'default', '0,0,0,0,0', 'Scratch', 'Growl', 'Ember', 'null', 28, 17, 16, 18, 18, 16, 0, 0, 0, 0, 0, 0, 6, 16, 30, 8, 18, 16, 35, 40, 25, -1, 35, 40, 25, -1, 0, 0, 0, 0, '2011-05-02:17-13-38'),
	(12, 'Chimchar', 'Chimchar', '135.0', 65, 'PARABOLIC', 'false', 5, 20, 1, 'Quirky', 'Blaze', '', 'false', 'Jesus', 'Jesus', '0,0,0,0,0', 'Scratch', 'Leer', 'null', 'null', 20, 12, 9, 12, 11, 10, 0, 0, 0, 0, 0, 0, 14, 28, 11, 19, 10, 16, 35, 30, -1, -1, 35, 30, -1, -1, 0, 0, 0, 0, '2011-05-21:23-04-34'),
	(13, 'Charmander', 'Charmander', '2077.0', 65, 'PARABOLIC', 'false', 15, 72, 1, 'Quirky', 'Blaze', '', 'false', 'Faintful', 'Faintful', '0,0,0,0,0', 'Scratch', 'Growl', 'Ember', 'null', 37, 21, 19, 24, 25, 21, 0, 0, 0, 0, 0, 0, 17, 15, 20, 10, 28, 19, 35, 39, 3, -1, 35, 40, 25, -1, 0, 0, 0, 0, '2012-08-03:16-32-23'),
	(14, 'Rattata', 'Rattata', '66.5', 57, 'MEDIUM', 'false', 4, 70, 2, 'Quiet', 'Guts', '', 'false', 'Faintful', 'Faintful', '0,0,0,0,0', 'Tackle', 'Tail Whip', 'null', 'null', 17, 10, 8, 9, 7, 9, 0, 0, 0, 0, 0, 0, 28, 16, 15, 0, 8, 31, 35, 30, -1, -1, 35, 30, -1, -1, 0, 0, 0, 0, '2012-08-05:22-05-06'),
	(15, 'Rattata', 'Rattata', '95.5', 57, 'MEDIUM', 'false', 4, 70, 2, 'Jolly', 'Guts', '', 'false', 'Faintful', 'Faintful', '0,0,0,0,0', 'Tackle', 'Tail Whip', 'null', 'null', 17, 10, 8, 12, 6, 8, 0, 0, 0, 0, 0, 0, 27, 24, 17, 22, 13, 22, 35, 30, -1, -1, 35, 30, -1, -1, 0, 0, 0, 0, '2012-08-05:22-21-06'),
	(16, 'Torchic', 'Torchic', '194.5', 65, 'PARABOLIC', 'false', 6, 70, 1, 'Quirky', 'Blaze', '', 'false', 'Connor', 'Connor', '0,0,0,0,0', 'Scratch', 'Growl', 'null', 'null', 23, 12, 11, 11, 14, 11, 0, 0, 0, 0, 0, 0, 28, 2, 23, 26, 16, 0, 35, 40, -1, -1, 35, 40, -1, -1, 0, 0, 0, 0, '2012-08-06:14-33-15'),
	(17, 'Rattata', 'Rattata', '8.0', 57, 'MEDIUM', 'false', 2, 70, 1, 'Naive', 'Run Away', '', 'false', 'Connor', 'Faintful', '0,0,0,0,0', 'Tackle', 'Tail Whip', 'null', 'null', 13, 7, 6, 8, 6, 5, 0, 0, 0, 0, 0, 0, 17, 8, 9, 23, 28, 27, 35, 30, -1, -1, 35, 30, -1, -1, 0, 0, 0, 0, '2012-08-06:14-39-12'),
	(18, 'Cyndaquil', 'Cyndaquil', '223.5', 65, 'PARABOLIC', 'false', 6, 70, 1, 'Quirky', 'Blaze', '', 'false', 'Faintful2', 'Faintful2', '0,0,0,0,0', 'Tackle', 'Leer', 'null', 'null', 11, 11, 5, 13, 12, 11, 0, 0, 0, 0, 0, 0, 18, 8, 5, 11, 7, 4, 23, 30, -1, -1, 35, 30, -1, -1, 0, 0, 0, 0, '2012-08-07:09-19-58');
/*!40000 ALTER TABLE `pn_pokemon` ENABLE KEYS */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
