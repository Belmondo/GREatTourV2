<?php

/*
 * Following code will list the environments, depending the qrcode
 */

// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';



// connecting to db
$db = new DB_CONNECT();

//url inicial
$initialURL = "http://pesquisa.great.ufc.br/greattourv2/";
// check for required fields
if (isset($_GET['id_environment'])) {
    
    $id_environment = (int) $_GET['id_environment'];
    
   

// get all environments from environments table
$resultado = mysql_query("SELECT * FROM environment WHERE id_environment = $id_environment") or die(mysql_error());
//echo $resultado;

// check for empty result
if (mysql_num_rows($resultado) <> 0) {
    // looping through all results
    // environments node

	while ($row = mysql_fetch_array($resultado)) {
	//taking the result link	
	$imgUrl = $row["image"];

	//concat the string
    $initialURL .= $imgUrl; 
	
    }
   
   //redirect to a link, cause the android just need do the http request.
    //this way is more simple, the processment is server-side
   header("Location:". $initialURL);

} 

}
?>
