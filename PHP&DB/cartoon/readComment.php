<?php

include "connect.php";
/*
$cid=$_GET['CID'];
$intcid=(int)$cid;*/

//$records = mysqli_query($conn,"select * from comment where cid=".$intcid."");
$records = mysqli_query($conn,"select * from comment");

$data = array();

while($row = mysqli_fetch_assoc($records))
                {
                    array_push($data, array("comid"=>$row['comid'],"username"=>$row['username'],"message"=>$row['message'],"cid"=>$row['cid']));
                }
                print(json_encode(array_reverse($data)));


mysqli_close($conn);

?>