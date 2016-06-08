<?php

/*
 * Following code will consult if the user is ok!
 */

// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';



// connecting to db
$db = new DB_CONNECT();

// check for required fields
if (isset($_GET['name']) && isset($_GET['password'])) {
    
    $name = $_GET['name'];
    $password = $_GET['password'];

    //encrypting the password to compare with the password on database

    $encryptPassword = base64_encode($password);
    


// get all visitor from visitors table who has this name and password
$result = mysql_query("SELECT * FROM visitor WHERE name = '$name' and password = '$encryptPassword' ") or die(mysql_error());


// check for empty result
if (mysql_num_rows($result) <> 0) {
    // looping through all results
    // products node
    //$response["usuarios"] = array();

    $row = mysql_fetch_array($result);
    $id_visitor = $row["id_visitor"];
    $data = date("y/m/d");

    //echo $data;
    $insertIdVisitor = mysql_query("INSERT INTO visits (id_visitor, visit_date) VALUES ('$id_visitor', '$data')") or die(mysql_error());


    
   
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no visitors found
    $response["success"] = 0;
 
    // echo no users JSON
    echo json_encode($response);
}

}
?>
