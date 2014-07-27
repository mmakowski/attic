DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id int(10) unsigned NOT NULL auto_increment,
  email varchar(128) NOT NULL,
  name varchar(128) NOT NULL,
  password_hash varchar(64) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_users_email (email)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS terms;
CREATE TABLE terms (
  id int(10) unsigned NOT NULL auto_increment,
  user_id int(10) NOT NULL,
  term varchar(256) NOT NULL,
  normalised_term varchar(256) NOT NULL,
  description text,
  PRIMARY KEY (id),
  UNIQUE KEY uq_terms_user_term (user_id, normalised_term),
  KEY fk_terms_user_id (user_id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS links;
CREATE TABLE links (
  id int(10) unsigned NOT NULL auto_increment,
  from_term_id int(10) NOT NULL,
  to_term_id int(10) NOT NULL,
  relationship varchar(64),
  PRIMARY KEY (id),
  KEY fk_links_from_term (from_term_id),
  KEY fk_links_to_term (to_term_id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

