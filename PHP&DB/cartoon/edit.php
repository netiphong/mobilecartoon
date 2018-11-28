<?php

include 'connect.php';

// Create connection
 
 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
 $DefaultId = 0;
 
$uid=$_POST['UID'];
$intid=(int)$uid;

$ImageData = $_POST['image_data'];
 
 $ImageName = $_POST['image_tag'];

 $Details = $_POST['details'];

$addminname = $_POST['adminname'];

 $ImagePath = "upload/$ImageName.jpg";
 
 $ServerURL = "/$ImagePath";
 
 $InsertSQL = "UPDATE cartoon SET image='".$ServerURL."',name='".$ImageName."',details='".$Details."',adminname='".$addminname."'"." WHERE cid=".$intid."";
 
 
 if(mysqli_query($conn, $InsertSQL)){

 file_put_contents($ImagePath,base64_decode($ImageData));

 echo "Your Image Has Been Uploaded.";
 }
 
 mysqli_close($conn);
 }else{
 echo "Please Try Again";
 }

?>