<?php

define("DB_HOST", "127.0.0.1");
define("DB_USER","mmakowski_meme");
define("DB_NAME","mmakowski_meme");
define("DB_PASSWORD","fill in the password");

function show_error($msg) {
	echo $msg;
	exit;
}

function get_db() {
	$db = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD) 
		or show_error("MySQL error when connecting: " . mysql_error());
	mysql_select_db(DB_NAME, $db)
		or show_error("MySQL error when selecting database: " . mysql_error());
	return $db;	
}
?>
