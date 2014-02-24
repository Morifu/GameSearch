<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['pid']) && isset($_POST['bid']) && isset($_POST['title']) && isset($_POST['price']) && isset($_POST['genre']) && isset($_POST['platform']) && isset($_POST['producer'])) {

	require_once 'db_connect.php';
	 
	 // connecting to db mysql
	$db = new DB_CONNECT();

	//connecting to db postgres
	$host = "cc.zebromalz.info"; 
	$user = "adam"; 
	$pass = "makapaka"; 
	$dbname = "adam"; 
	 
	$db_pg = pg_connect("host=$host dbname=$dbname user=$user password=$pass")
		or die ("Could not connect to server\n"); 
	
	$pid = $_POST['pid'];
	$title = $_POST['title'];
    $price = $_POST['price'];
	$genre = $_POST['genre'];
	$platform = $_POST['platform'];
	$producer = $_POST['producer'];
		
	if($_POST['bid'] == 1)
	{
		// mysql inserting a new row
		$query_ = "UPDATE products SET Title = '$title', Price = $price, Genre = '$genre', Platform = '$platform', Producer = '$producer' WHERE pid = $pid";
 
		$result = mysql_query($query_);
	} 
	else if($_POST['bid'] == 2)
	{
		// postgres inserting a new row
		$query_ = "UPDATE products SET \"Title\" = '$title', \"Price\" = $price, \"Genre\" = '$genre', \"Platform\" = '$platform', \"Producer\" = '$producer' WHERE \"pid\" = $pid";
	
		$result = pg_query($db_pg, $query_) or die("Cannot execute query: $query\n");
	}
    
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
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