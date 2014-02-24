<?php
 
/*
 * Following code will get single product details
 * A product is identified by its parameters in link
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
$query_pg = "SELECT * FROM products WHERE ";

// query start for mysql
$query = "";

if(isset($_GET['title']))
{
	$query .= "Title LIKE '%".$_GET['title']."%' ";
	$query_pg .= "\"Title\" LIKE '%".$_GET['title']."%' ";
	//echo $query;
}
if(isset($_GET['price']))
{
	$query .= "AND Price=".$_GET['price']." ";
	$query_pg .= "AND \"Price\"=".$_GET['price']." ";
	//echo $query;
}
if(isset($_GET['genre']))
{

	$genre_str = $_GET['genre'];
	
	if(!empty($genre_str))
	{
		$query .= "AND Genre IN (";
		$query_pg .= "AND \"Genre\" IN (";
		
		$str_arr = explode(",",$genre_str);
		
		for($i=0; $i< sizeof($str_arr);$i++)
		{
			if($i > 0)
			{
				$query .= ",";
				$query_pg .= ",";
			}
			$query .= "'".$str_arr[$i]."'";
			$query_pg .= "'".$str_arr[$i]."'";
		}
		$query .= ") ";
		$query_pg .= ") ";
		
		//echo $query;
	}
	
}
if(isset($_GET['producer']))
{
	$producer_str = $_GET['producer'];
	if(!empty($producer_str))
	{
		$query .= "AND Producer IN (";
		$query_pg .= "AND \"Producer\" IN (";
		
		$str_arr = explode(",",$producer_str);
		
		for($i=0; $i< sizeof($str_arr);$i++)
		{
			if($i > 0)
			{
				$query .= ",";
				$query_pg .= ",";
			}
			$query .= "'".$str_arr[$i]."'";
			$query_pg .= "'".$str_arr[$i]."'";
		}
		$query .= ") ";
		$query_pg .= ") ";
		//echo $query;
	}
	
}

if(isset($_GET['platform']))
{
	$platform_str = $_GET['platform'];
	if(!empty($platform_str))
	{
		$query .= "AND Platform IN (";
		$query_pg .= "AND \"Platform\" IN (";
		
		$str_arr = explode(",",$platform_str);
		
		for($i=0; $i< sizeof($str_arr);$i++)
		{
			if($i > 0)
			{
				$query .= ",";
				$query_pg .= ",";
			}
			$query .= "'".$str_arr[$i]."'";
			$query_pg .= "'".$str_arr[$i]."'";
		}
		$query .= ") ";
		$query_pg .= ") ";
		//echo $query;
	}
	
}

if(isset($_GET['priceMin']) && isset($_GET['priceMax']))
{
	$query .= "AND Price BETWEEN ";
	$query_pg .= "AND \"Price\" BETWEEN ";
	
	$query .= $_GET['priceMin']. ' AND ' . $_GET['priceMax'];
	$query_pg .= $_GET['priceMin']. ' AND ' . $_GET['priceMax'];
	
	//echo $query;
}
// FOR TEST ECHO QUERY
//echo $query;
//echo $query_pg

// check for post data
if (!empty($query) && !empty($query_pg)) {
    
    // get a product from products table
    $result = mysql_query("SELECT *FROM products WHERE $query");
	$result_pg = pg_query($db_pg, $query_pg) or die("Cannot execute query: $query\n");
 
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
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>