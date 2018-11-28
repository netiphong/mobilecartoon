<?php

include "connect.php";

$name=$_GET['Name'];



$records = mysqli_query($conn,"select * from users where username='".$name."'");

$data = array();

while($row = mysqli_fetch_assoc($records))
{
    $data[] = $row; 
}

echo json_encode($data);

mysqli_close($conn);

?>