<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['title']) && isset($_POST['price']) && isset($_POST['genre']) && isset($_POST['platform']) && isset($_POST['producer'])) {

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
		
	// query start for postgres
	$query = "SELECT * FROM products";
	// get all products from products table
	$result_msql = mysql_query($query);
	$result_pg = pg_query($db_pg, $query) or die("Cannot execute query: $query\n");
	
	$title = $_POST['title'];
    $price = $_POST['price'];
	$genre = $_POST['genre'];
	$platform = $_POST['platform'];
	$producer = $_POST['producer'];
		
	if(mysql_num_rows($result_msql) < pg_num_rows($result_pg))
	{
		// mysql inserting a new row
		$query_ = "INSERT INTO products(pid, Title, Platform, Price, Genre, Producer) VALUES(DEFAULT, '$title', '$platform', $price, '$genre', '$producer')";
	
		$result = mysql_query($query_);
	} 
	else
	{
		// postgres inserting a new row
		$query_ = "INSERT INTO products(pid, \"Title\", \"Platform\", \"Price\", \"Genre\", \"Producer\") VALUES(DEFAULT, '$title', '$platform', $price, '$genre', '$producer')";
	
		$result = pg_query($db_pg, $query_) or die("Cannot execute query: $query\n");
	}
    
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";
 
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