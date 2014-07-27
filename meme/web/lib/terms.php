<?php

class Term {
	public $id;
	public $term;
	public $normalised_term;
	public $description;
	
	public static function fromArray($arr) {
		if (!$arr) return null;
		$term = new Term();
		$term->id = $arr['id'];
		$term->term = $arr['term'];
		$term->normalised_term = $arr['normalised_term'];
		$term->description = $arr['description'];
		return $term;
	}

	public static function loadFromDb($db, $term_id) {
		if (!$term_id) return null;
		$q = 'SELECT * FROM terms WHERE id = ' . $term_id;
		$res = mysql_query($q, $db) or show_error("MySQL error: " . mysql_error());
		$term = Term::fromArray(mysql_fetch_array($res, MYSQL_ASSOC));
		mysql_free_result($res);
		return $term;
	}

	public function saveToDb($db) {
		// TODO: handle existing terms
		$q = 'INSERT INTO terms '
			. '(user_id, term, normalised_term, description) VALUES ('
			. $_SESSION['userId'] . ', '
			. "'".$this->term."', "
			. "'".$this->normalised_term."', "
			. "'".$this->description."')";
	    mysql_query($q, $db) or show_error("MySQL error: " . mysql_error());
	}
	
	public function normalise() {
		$this->normalised_term = strtr(strtolower($this->term), ' &?=', '____');
	}
}
    
function show_all_terms($db) {
	$q = 'SELECT id, term FROM terms WHERE user_id = ' . $_SESSION['userId'] . ' ORDER BY term';
	$res = mysql_query($q, $db) or show_error("MySQL error: " . mysql_error());    
	while ($r = mysql_fetch_array($res, MYSQL_ASSOC)) {
		echo '<a href="view.php?term_id='.$r['id'].'">'.$r['term'].'</a><br/>';
	}
	mysql_free_result($res);
}

?>
