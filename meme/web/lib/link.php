<?php

class Link {
	public $id;
	public $from_term_id;
	public $to_term_id;
	public $relationship;
	public $from_term_name;
	public $to_term_name;
	
	/**
	 * @return link oject with supplied properties
	 * @param $arr an array with link properties
	 */
	public static function fromArray($arr) {
		if (!$arr) return null;
		$link = new Link();
		$link->id = $arr['id'];
		$link->from_term_id = $arr['from_term_id'];
		$link->to_term_id = $arr['to_term_id'];
		$link->relationship = $arr['relationship'];
		$link->from_term = $arr['from_term'];
		$link->to_term = $arr['to_term'];
		return $link;
	}
	
	/**
	 * @return array of all links from given term
	 * @param $db database connection
	 * @param $term_id term id
	 */
	private static function loadAllFromDbForTermIdAndDirection($db, $term_id, $direction) {
		if (!$term_id) return null;
		$q = 'select l.id, l.from_term_id, l.to_term_id, l.relationship, t1.term as from_term, t2.term as to_term '
			.'from links l, terms t1, terms t2 '
			.'where '.$direction.'_term_id = ' . $term_id 
			.'  and t1.id = l.from_term_id and t2.id = l.to_term_id';
		$res = mysql_query($q, $db) or show_error("MySQL error: " . mysql_error());
		$links = array();
		while ($arr = mysql_fetch_array($res, MYSQL_ASSOC)) {
			array_push($links, Link::fromArray($arr));
		}
		mysql_free_result($res);
		return $links;
	}
	
	/**
	 * @return array of all links from given term
	 * @param $db database connection
	 * @param $term_id term id
	 */
	public static function loadAllFromDbFromTermId($db, $term_id) {
		return Link::loadAllFromDbForTermIdAndDirection($db, $term_id, 'from');
	}
	
	/**
	 * @return array of all links to given term
	 * @param $db database connection
	 * @param $term_id term id
	 */
	public static function loadAllFromDbToTermId($db, $term_id) {
		return Link::loadAllFromDbForTermIdAndDirection($db, $term_id, 'to');
	}

	/**
	 * @return a list of links in form
	 * @param $form form (POST)
	 * @param $term term from which links originate
	 */
	public static function getAllFromFormAndTerm($form, $term) {
		$count = $form['linkCount'];
		$links = array();
		for ($i = 0; $i < $count; $i++) {
			if (!$form["linkToTerm_$i"]) continue;
			$link = new Link();
			$link->id = $form["linkId_$i"];
			$link->from_term_id = $term->id;
			$link->from_term = $term->term;
			$link->to_term = $form["linkToTerm_$i"];
			$link->relationship = $form["linkRelationship_$i"];
			// this link is incomplete -- needs to_term_id resolved before it can be saved
			array_push($links, $link);
		}
		return $links;
	}

	/**
	 * @return id of this link
	 * @param $db database connection
	 */
	public function saveToDb($db) {
		// resolve to_term_id if necessary
		if (!$this->to_term_id) 
			$this->to_term_id = Term::getIdFromDbOrCreate($db, $this->to_term);
		if ($this->id) {
			// update	
			$q = "update links set "
				."  from_term_id = $this->from_term_id, "
				."  to_term_id = $this->to_term_id, "
				."  relationship = nullif('$this->relationship', '') "
				."where id = $this->id";
		} else {
			// insert new
			$q = "insert into links "
				."  (from_term_id, to_term_id, relationship) "
				."values "
				."  ($this->from_term_id, $this->to_term_id, nullif('$this->relationship', ''))";
		}
	    mysql_query($q, $db) or show_error("MySQL error: " . mysql_error());
		if (!$this->id)
			$this->id = mysql_insert_id($db);
		return $this->id;
	}
}

?>
