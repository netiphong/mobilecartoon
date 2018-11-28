<?php

include "connect.php";
/*
$name=$_GET['Name'];
$pass=$_GET['Password'];*/
$uid=$_GET['UID'];
$intuid=(int)$uid;



$records = mysqli_query($conn,"select * from cartoon where cid=".$intuid."");

$data = array();

while($row = mysqli_fetch_assoc($records))
{
    $data[] = $row; 
}

echo json_encode($data);

mysqli_close($conn);

?>