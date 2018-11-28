<?php

include "connect.php";


$records = mysqli_query($conn,"select * from cartoon");

$data = array();

while($row = mysqli_fetch_assoc($records))
                {
                    array_push($data, array("cid"=>$row['cid'],"name"=>$row['name'],"details"=>$row['details'],"image_url"=>$row['image'],"adminname"=>$row['adminname']));
                }
                print(json_encode(array_reverse($data)));


mysqli_close($conn);

?>