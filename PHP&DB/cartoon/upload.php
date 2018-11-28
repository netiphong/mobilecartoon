<?php

include 'connect.php';

// Create connection
 
 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
 $DefaultId = 0;
 
 $ImageData = $_POST['image_data'];
 
 $ImageName = $_POST['image_tag'];

 $Details = $_POST['details'];

 $admin = $_POST['adminname'];

 $ImagePath = "upload/$ImageName.jpg";
 
 $ServerURL = "/$ImagePath";
 
 $InsertSQL = "INSERT INTO cartoon (name,details,Image,adminname) values('$ImageName','$Details','$ServerURL','$admin')";
 
 if(mysqli_query($conn, $InsertSQL)){

 file_put_contents($ImagePath,base64_decode($ImageData));

 echo "Your Image Has Been Uploaded.";
 }
 
 mysqli_close($conn);
 }else{
 echo "Please Try Again";
 }

?>