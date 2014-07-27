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
	<script language="JavaScript">
	function addLink() {
		var parent = document.getElementById('linkInputs');
		var linkCountField = document.getElementById('linkCount');
		var linkCount = linkCountField.value;
		var newLinkRelationship = document.createElement('input');
		newLinkRelationship.setAttribute('name', 'linkRelationship_' + linkCount);
		parent.appendChild(newLinkRelationship);
		var colon = document.createElement('span');
		colon.innerHTML = ': ';
		parent.appendChild(colon);
		var newLinkInput = document.createElement('input');
		newLinkInput.setAttribute('name', 'linkToTerm_' + linkCount);
		parent.appendChild(newLinkInput);
		parent.appendChild(document.createElement('br'));
		linkCountField.value = ++linkCount;
	}		
	</script>
	<body> 
		<a href="index.php">index</a>
<?php		
	$_SESSION['userId'] = 1;
	$db = get_db();
	$term_id = $_GET['term_id'];
	$term = Term::loadFromDb($db, $term_id) or new Term();
?>
	<form action="save.php" method="post">
		<input type="hidden" value="<?php echo $term->id ?>" name="id"/>
		<input id="term" name="term" size="40" type="text" value="<?php echo $term->term; ?>" /><br/>
		<textarea cols="40" id="description" name="description" rows="10"><?php echo $term->description; ?></textarea><br/>
		<div id="linkInputs"> 
<?php
	$links = Link::loadAllFromDbFromTermId($db, $term_id);
	$i = 0;
	for ($i = 0; $i < count($links); $i++) {
?>
			<input type="hidden" name="linkId_<? echo $i; ?>" value="<? echo $links[$i]->id; ?>"/>
			<input type="text" name="linkRelationship_<? echo $i; ?>" value="<? echo $links[$i]->relationship; ?>"/>: 
			<input type="text" name="linkToTerm_<? echo $i; ?>" value="<? echo $links[$i]->to_term; ?>"/><br/>
<?
	}
?>			
		</div>
		<a href="javascript:;" onclick="addLink();">add link</a>
		<input type="hidden" value="<? echo $i; ?>" id="linkCount" name="linkCount"/>
		<input name="save" type="submit" value="Save" /><br/>
	</form>
	</body>
</html>