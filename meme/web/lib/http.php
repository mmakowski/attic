<?php
function redirect_to($page) {
  header("Location: http://" . $_SERVER['HTTP_HOST']
	 . dirname($_SERVER['PHP_SELF'])
	 . "/$page");      
  exit;
}    
?>
