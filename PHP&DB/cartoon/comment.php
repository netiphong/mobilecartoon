<?php

include "connect.php";

$name=$_GET['Name'];
$m=$_GET['Message'];
$cid=$_GET['CID'];
$intcid =(int)$cid;

	

	$query = "insert into comment (username, message, cid) values ('".$name."','".$m."',".$intcid.")";

	if(mysqli_query($conn, $query))
	{
		echo "success";
		//mysqli_close($conn);
	}
	else
	{	
		echo "failed";
	}
	



 


?>