<?php
 
/*
 * Following code will delete a product from table
 * A product is identified by product id (pid)
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['pid']) && isset($_POST['bid'])) {
    
	$pid = $_POST['pid'];
	$bid = $_POST['bid'];
	
    // include db connect class
    require_once 'db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
	
	//connecting to db postgres
	$host = "cc.zebromalz.info"; 
	$user = "adam"; 
	$pass = "makapaka"; 
	$dbname = "adam"; 
	 
	$db_pg = pg_connect("host=$host dbname=$dbname user=$user password=$pass")
		or die ("Could not connect to server\n"); 
				
	$query = "DELETE FROM products WHERE pid = '$pid'";
	
	if($bid == 1)
	{
		// mysql update row with matched pid
		$result = mysql_query($query);
	} else if($bid == 2){
		// postgres update row with matched pid
		$result = pg_query($db_pg, $query) or die("Cannot execute query: $query\n");
	}
    // check if row deleted or not
    if (mysql_affected_rows() > 0 || pg_affected_rows($result) > 0) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Product successfully deleted";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No product found";
 
        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>