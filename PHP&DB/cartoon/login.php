<?php

include "connect.php";

$name=$_GET['Name'];
$pass=$_GET['Password'];


$records = mysqli_query($conn,"select * from users where username='".$name."'");
$records1 = mysqli_query($conn,"select password from users where username='".$name."'");

$data = array();
$data = array();






while($row = mysqli_fetch_assoc($records))
{
    $data[] = $row; 
}
while($row1 = mysqli_fetch_assoc($records1))
{
    $data1[] = $row1; 
}


if(json_encode($data)!= "[]"){
	
	if(json_encode($data1[0]) ==  "{".'"'."password".'":'.'"'.$pass.'"}'){
		echo "Correct";
		
	}else echo "Please check your password";
	
}



else{
	echo "Please check your username";
}


