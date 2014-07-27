<?php
    require('lib/db.php');
	require('lib/term.php');
	require('lib/link.php');
	require('lib/http.php');
	
	$_SESSION['userId'] = 1;
	$term = Term::fromArray($_POST);
	$db = get_db();
	$term->saveToDb($db);
	$links = Link::getAllFromFormAndTerm($_POST, $term);
	foreach ($links as $link) 
		$link->saveToDb($db);
	redirect_to("view.php?term_id=$term->id");
?>
