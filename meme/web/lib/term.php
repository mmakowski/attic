<?php

class Term {
	public $id;
	public $term;
	public $normalised_term;
	public $description;
	
	/**
	 * @return constructed term
	 * @param $arr array of term properties (e.g. as loaded from the database)
	 */
	public static function fromArray($arr) {
		if (!$arr) return null;
		$term = new Term();
		$term->id = $arr['id'];
		$term->term = $arr['term'];
		$term->normalised_term = $arr['normalised_term'];
		$term->description = $arr['description'];
		return $term;
	}

	/**
	 * @return loaded term
	 * @param $db database connection
	 * @param $term_id term id
	 */
	public static function loadFromDb($db, $term_id) {
		if (!$term_id) return null;
		$q = 'SELECT * FROM terms WHERE id = ' . $term_id;
		$res = mysql_query($q, $db) or show_error("MySQL error: " . mysql_error());
		$term = Term::fromArray(mysql_fetch_array($res, MYSQL_ASSOC));
		mysql_free_result($res);
		return $term;
	}

	/**
	 * @return id of a given term. Will create a new term and assing new id if term does not exist.
	 * @param $db database connection
	 * @param $term term (string)
	 */
	public static function getIdFromDbOrCreate($db, $term_str) {
		if (!$term_str) return null;
		$q = "SELECT id FROM terms WHERE normalised_term = '".Term::normaliseString(strtolower($term_str))."'";
		$res = mysql_query($q, $db) or show_error("MySQL error: " . mysql_error());
		$arr = mysql_fetch_array($res, MYSQL_ASSOC);
		mysql_free_result($res);
		if ($arr) {
			return $arr['id'];
		} 
		$term = new Term();
		$term->term = $term_str;
		return $term->saveToDb($db);
	}
	
	/**
	 * @return id of the term
	 * @param $db database connection
	 */
	public function saveToDb($db) {
		$this->normalise();
		if ($this->id) {
			// update
			$q = "update terms set "
				."  term = '$this->term', "
				."  normalised_term = '$this->normalised_term', "
				."  description = '$this->description' "
				."where id = $this->id";
		} else {
			// insert new
			$q = "insert into terms "
				."  (user_id, term, normalised_term, description) "
				."values "
				."  ('".$_SESSION['userId']."', '$this->term', '$this->normalised_term', '$this->description')";
		}
	    mysql_query($q, $db) or show_error("MySQL error: " . mysql_error());
		if (!$this->id)
			$this->id = mysql_insert_id($db);
		return $this->id;
	}
	
	function normalise() {
		$this->normalised_term = Term::normaliseString($this->term);
	}
	
	static function normaliseString($str) {
		return strtr(strtolower($str), ' &?=', '____');
	}
}

?>
