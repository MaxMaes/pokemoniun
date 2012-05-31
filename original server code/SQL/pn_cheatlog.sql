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

