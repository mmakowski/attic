<?php
    require('lib/db.php');
    require('lib/term.php');
    require('lib/link.php');

	header('Content-type: image/svg+xml');
	
	// TODO: move to a function
	$_SESSION['userId'] = 1;
	$db = get_db();
	$term_id = $_GET['term_id'];
	$term = Term::loadFromDb($db, $term_id) or show_error("Term $term_id not found");
	$links = Link::loadAllFromDbFromTermId($db, $term_id);
	$link_map = array();
	$link_map[$term->term] = array();
	foreach ($links as $link) {
		$link_map[$term->term][] = $link->to_term;
	}
	
	$tmpfname = tempnam("/home2/mmakowski/tmp", "meme_term_map");
	$tmp = fopen($tmpfname, "w");
	foreach ($link_map as $key => $vals) {
		fwrite($tmp, $key.': ');
		$first = true;
		foreach ($vals as $val) {
			if ($first) $first = false; else fwrite($tmp, ', ');
			fwrite($tmp, $val);
		}
		fwrite($tmp, "\n");
		foreach ($vals as $val) {
			fwrite($tmp, "$val:\n");
		}
	}
	
	fwrite($tmp, "\n");
	fclose($tmp);
	
	passthru("/home2/mmakowski/meme/bin/map < $tmpfname");
	
	unlink($tmpfname);
?>
