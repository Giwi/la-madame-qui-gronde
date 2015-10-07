<?php

// ** Réglages MySQL - Votre hébergeur doit vous fournir ces informations. ** //
/** Le nom de la base de données de WordPress. */
define('DB_NAME', 'theblackout');

/** Utilisateur de la base de données MySQL. */
define('DB_USER', 'theblackout');

/** Mot de passe de la base de données MySQL. */
define('DB_PASSWORD', 'xxxxxx');

/** Adresse de l'hébergement MySQL. */
define('DB_HOST', 'mysql51-45.perso');
$dbh=mysql_connect (DB_HOST,DB_USER, DB_PASSWORD) or die ('Cannot connect to the database') or
die("Impossible de se connecter : " . mysql_error());
mysql_select_db (DB_NAME,$dbh);
function initDB() {
	$sql = "CREATE TABLE IF NOT EXISTS dqg_children (`id` int(11) NOT NULL AUTO_INCREMENT, `name` text NOT NULL, PRIMARY KEY (`id`)) ENGINE=MyISAM DEFAULT CHARSET=latin1";
	$result=@mysql_query($sql);
	if(!$result) {
		die(mysql_error());
	}$
	$sql = "CREATE TABLE IF NOT EXISTS dqg_sentence (`id` int(11) NOT NULL AUTO_INCREMENT, `sentence` text NOT NULL, PRIMARY KEY (`id`)) ENGINE=MyISAM DEFAULT CHARSET=latin1";
	$result=@mysql_query($sql);
	if(!$result) {
		die(mysql_error());
	}
}
initDB();
print_r($_POST);
 $json =  str_replace("\\\"", "\"", $_POST["jsonpost"]);
    echo "JSON: \n";
    echo "--------------\n<br />";
    var_dump($json);
    echo "\n\n<br /><br />";
    $data = json_decode(safeJSON_chars($json));
    echo "Array: \n<br />";
    echo "--------------\n<br />";
    var_dump($data);
    echo "\n\n<br /><br />";
    $name = $data[0]->{'childName'};
    if($name != null) {
		$sql = "INSERT INTO dqg_children set name=\"".$name."\"";
	    $result=@mysql_query($sql);
	    if(!$result) {
	        die(mysql_error());
    	}
	}
    $name = $data[0]->{'sentences'};
    if($name != null) {
		$sql = "INSERT INTO dqg_sentence set sentence=\"".$name."\"";
	    $result=@mysql_query($sql);
	    if(!$result) {
	        die(mysql_error());
    	}
	}
    echo "Result: \n<br />";
    echo "--------------\n<br />";


    function safeJSON_chars($data) {
    $aux = str_split($data);
    foreach($aux as $a) {
        $a1 = urlencode($a);
        $aa = explode("%", $a1);
        foreach($aa as $v) {
            if($v!="") {
                if(hexdec($v)>127) {
                $data = str_replace($a,"&#".hexdec($v).";",$data);
                }
            }
        }
    }
    return $data;
}
?>
