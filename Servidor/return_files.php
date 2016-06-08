<?php

//ini_set('default_charset','UTF-8');

$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

$db = new DB_CONNECT();

mysql_query("SET NAMES 'utf8'");
mysql_query('SET character_set_connection=utf8');
mysql_query('SET character_set_client=utf8');
mysql_query('SET character_set_results=utf8');


if (isset($_GET['id_environment']) AND isset($_GET['id_type'])) {

    $id_environment = (int) $_GET['id_environment'];
    $id_type = (int) $_GET['id_type'];



    $resultado = mysql_query("SELECT f.description_en,f.description_pt,f.url 
        FROM file f 
        INNER JOIN file_environment fe ON f.id_file = fe.id_file 
        WHERE id_environment = $id_environment AND id_type = $id_type") or die(mysql_error());

    //echo "passou da consulta";

    if (mysql_num_rows($resultado) <> 0) {

        $response["Files"] = array();


          while($row = mysql_fetch_array($resultado)){
          
            $Files = array();
            //$Files["description_en"] = $row["description_en"];
            $Files["description_pt"] = $row["description_pt"];  
            $Files["url"] = $row["url"];    

            array_push($response["Files"], $Files);    

        }

         $response["success"] = 1;
         echo json_encode($response);


        

    }else {
        // no files found
        $response["success"] = 0;
        $response["message"] = "No files found";

         // echo no users JSON
        echo json_encode($response);
}

}


?>