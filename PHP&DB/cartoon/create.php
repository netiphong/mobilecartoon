<?php

include "connect.php";

$name=$_GET['Name'];
$pass=$_GET['Password'];




$records = mysqli_query($conn,"select * from users where username='".$name."'");

$data = array();

while($row = mysqli_fetch_assoc($records))
{
    $data[] = $row; 
}


if(json_encode($data)!= "[]"){
	echo " Name is unavilable";
}
else{
	
		$query = "insert into users (username, password) values ('".$name."','".$pass."')";

	if(mysqli_query($conn, $query))
	{
		echo "success";
		//mysqli_close($conn);
	}
	else
	{	
		echo "failed";
	}
	
}


 


?>