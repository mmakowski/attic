<?php
    require('lib/db.php');
    require('lib/term.php');
?>
<!doctype html PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
	<head>
        <title>Meme</title>
        <meta content="text/html; CHARSET=utf-8" http-equiv="content-type" />
        <link href="common.css" rel="stylesheet" type="text/css" />
	</head>
	<body> 
		<a href="edit.php">new</a><br/>
<?php		
	//phpinfo();
	$_SESSION['userId'] = 1;
	$db = get_db();
	$q = 'SELECT id, term FROM terms WHERE user_id = ' . $_SESSION['userId'] . ' ORDER BY term';
	$res = mysql_query($q, $db) or show_error("MySQL error: " . mysql_error());    
	$last_letter = '';
	while ($r = mysql_fetch_array($res, MYSQL_ASSOC)) {
		$curr_letter = substr(strtoupper($r['term']), 0, 1);
		if ($curr_letter != $last_letter) {
			echo "<h1>$curr_letter</h1>";
			$last_letter = $curr_letter;
		}
		if ($r['term'][0])
		echo '<a href="view.php?term_id='.$r['id'].'">'.$r['term'].'</a><br/>';
	}
	mysql_free_result($res);
?>
	</body>
</html>