
<?php
    //ini_set('default_charset','UTF-8');
    //mb_internal_encoding("UTF-8");
    //mb_http_output( "UTF-8" ); 
    


    $objConnect = mysql_connect("localhost","great_greattour","great@2014");
    $objDB = mysql_select_db("great_greattour");

            

    if (isset($_GET['id_environment'])) {
    
    $id_environment = (int) $_GET['id_environment'];

    $strSQL = "SELECT * FROM person WHERE id_environment = $id_environment";

    
    $objQuery = mysql_query($strSQL);
    $intNumField = mysql_num_fields($objQuery);
    $resultArray = array();
    while($obResult = mysql_fetch_array($objQuery))
    {
        $arrCol = array();
        for($i=0;$i<$intNumField;$i++)
        {
            $arrCol[mysql_field_name($objQuery,$i)] = $obResult[$i];
        }
        array_push($resultArray,$arrCol);
    }
    
    mysql_close($objConnect);
    

    echo json_encode($resultArray);

}
?>