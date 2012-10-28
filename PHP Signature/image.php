<?php
mysql_connect("remote-mysql3.servage.net", "viperpray2", "jjjkkk111");
mysql_select_db("viperpray2");
header("Content-Type: image/png");
$im = imagecreate(500, 110);
$background_color = imagecolorallocate($im, 0, 0, 0);
if(isset($_GET['player'])) {
	$user = userExists($_GET['player']);
	$text_color = imagecolorallocate($im, 255, 255, 255);
	if($user) {
		imagestring($im, 5, 9, 9, "Username:", $text_color);
		imagestring($im, 5, 125, 9, $user['username'], $text_color);
		imagestring($im, 5, 9, 32, "Last login:", $text_color);
		if(($user['lastLoginTime'] / 1000) > time() - (30 * 60 + (1 * 60 * 60) /* 1 hour timedifference */)) {
			$text_color = imagecolorallocate($im, 30, 210, 10);
			imagestring($im, 5, 125, 32, "Online", $text_color);
		} else {
			$text_color = imagecolorallocate($im, 240, 50, 50);
			imagestring($im, 5, 125, 32, date("d/m/Y H:i", $user['lastLoginTime'] / 1000), $text_color);
		}
		$text_color = imagecolorallocate($im, 255, 255, 255);
		imagestring($im, 5, 9, 55,  "Time played:", $text_color);
		imagestring($im, 5, 125, 55, "-", $text_color);
		imagestring($im, 5, 320, 9, "Badges:", $text_color);
		imagestring($im, 5, 465, 9, badges($user), $text_color);
		$pokedex = pokedex($user);
		imagestring($im, 5, 320, 32, "Pokemon seen:", $text_color);
		imagestring($im, 5, 465, 32, $pokedex['seen'], $text_color);
		imagestring($im, 5, 320, 55, "Pokemon caught:", $text_color);
		imagestring($im, 5, 465, 55, $pokedex['caught'], $text_color);
	} else {
		imagestring($im, 5, 9, 9, "User ".$_GET['player']." not found.", $text_color);
	}
}
$text_color = imagecolorallocate($im, 240, 240, 20);
imagestring($im, 5, 268, 80, "Play Pokemonium for FREE.", $text_color);
imagepng($im);
imagedestroy($im);

function userExists() {
	$query = mysql_query("SELECT * FROM `pn_members` WHERE `username` = '".$_GET['player']."'");
	if($query) {
		return mysql_fetch_assoc($query);
	}
}

function badges($user) {
	$badges = 0;
	for ($i = 0; $i < strlen($user['badges']); $i++) {
		$badge = substr($user['badges'], $i,1);
		if($badge == 1) {
			$badges++;
		}
	} 
	return $badges;
}

function pokedex($user) {
	$query = mysql_query("SELECT * FROM `pn_pokedex` LEFT JOIN `pn_members` ON(pn_members.id = pn_pokedex.memberId) WHERE pn_pokedex.memberId = ".$user['id']);
	$seen = 0;
	$caught = 0;
	if($query) {
		$pokedex = mysql_fetch_row($query);
		for ($i = 2; $i < count($pokedex); $i++) {
			if($pokedex[$i] == 1) {
				$seen++;
			} elseif($pokedex[$i] == 2) {
				$seen++;
				$caught++;
			}
		}
	}
	return array('seen' => $seen, 'caught' => $caught);
}
?>