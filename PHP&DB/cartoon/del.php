<?php

include "connect.php";

$uid=$_GET['UID'];

$query = "delete from cartoon where cid='".$uid."'";

if(mysqli_query($conn, $query))
{
     echo "success";
}
else
{
    echo mysqli_error($conn);
}

mysqli_close($conn);

?>