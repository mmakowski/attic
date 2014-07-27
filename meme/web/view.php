<?php
    require('lib/db.php');
    require('lib/term.php');
    require('lib/link.php');
?>
<!doctype html PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
	<head>
        <title>Meme</title>
        <meta content="text/html; CHARSET=utf-8" http-equiv="content-type" />
        <link href="common.css" rel="stylesheet" type="text/css" />
	</head>
	<body> 
<?php		
	$_SESSION['userId'] = 1;
	$db = get_db();
	$term_id = $_GET['term_id'];
	$term = Term::loadFromDb($db, $term_id) or show_error("Term $term_id not found");
	$links_from = Link::loadAllFromDbFromTermId($db, $term_id);
	$links_to = Link::loadAllFromDbToTermId($db, $term_id);
?>
		<a href="index.php">index</a>
		<a href="edit.php?term_id=<? echo $term_id;?>">edit</a>
		<embed src="img.php?term_id=<? echo $term_id; ?>" width="500" height="500" style="float:right"/>
		<h1><? echo $term->term; ?></h1>
		<p><? echo str_replace("\n", "<br/>\n", $term->description); ?></p>
<?php
	if (sizeof($links_from) > 0)
		echo '<h2>Associations</h2>';
	foreach ($links_from as $link) {
		if ($link->relationship)
			echo "$link->relationship: ";
		echo "<a href=\"view.php?term_id=$link->to_term_id\"/>$link->to_term</a><br/>";
	}
?>
<?php
	if (sizeof($links_to) > 0)
		echo '<h2>Association of</h2>';
	foreach ($links_to as $link) {
		echo "<a href=\"view.php?term_id=$link->from_term_id\"/>$link->from_term</a>";
		if ($link->relationship)
			echo " ($link->relationship)";
		echo '<br/>';
	}
?>
	</body>
</html>