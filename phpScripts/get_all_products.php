<?php
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response = array();
 
// include db connect class
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
$query_pg = "SELECT * FROM products";
// get all products from products table
$result = mysql_query("SELECT *FROM products");
$result_pg = pg_query($db_pg, $query_pg) or die("Cannot execute query: $query\n");
 
 
// check for empty result
if (!empty($result) && !empty($result_pg)) {
	
		// set up response array
		if(mysql_num_rows($result) > 0 || pg_num_rows($result_pg) > 0)
			$response["products"] = array();
		
        // check for empty result
        if (mysql_num_rows($result) > 0) {
			
			while ($row = mysql_fetch_array($result)) {
 
				$product = array();
				$product["bid"] = "1";
				$product["pid"] = $row["pid"];
				$product["title"] = $row["Title"];
				$product["genre"] = $row["Genre"];
				$product["price"] = $row["Price"];
				$product["producer"] = $row["Producer"];
				$product["platform"] = $row["Platform"];
				
				array_push($response["products"], $product);
			}
        } 
		
		if (pg_num_rows($result_pg) > 0) {
			
			while ($row = pg_fetch_array($result_pg)) {
 
				$product = array();
				$product["bid"] = "2";
				$product["pid"] = $row["pid"];
				$product["title"] = $row["Title"];
				$product["genre"] = $row["Genre"];
				$product["price"] = $row["Price"];
				$product["producer"] = $row["Producer"];
				$product["platform"] = $row["Platform"];
				
				array_push($response["products"], $product);
 
			}
          
        } 
		
		if(count($response) != 0)
		{
			$response["success"] = 1;
	 
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
        // no product found
        $response["success"] = 0;
        $response["message"] = "No product found";
 
        // echo no users JSON
        echo json_encode($response);
    }
?>