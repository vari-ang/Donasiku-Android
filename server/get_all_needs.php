<?php header("Access-Control-Allow-Origin: *"); ?>
<?php 
    include "./connection.php";

    $mysqli = new mysqli($server, $id, $pw, $db);

    if ($mysqli->connect_errno) {
        echo array('status' => 'error', 'message' => "Gagal terhubung ke database MySQL: " . $mysqli->connect_error);
        die();
    }
    
    $stmt = $mysqli->prepare("SELECT * FROM kebutuhan");
    $stmt->bind_param();
    $stmt->execute();

    $res = $stmt->get_result();
    if($res->num_rows > 0) {
        $needs = array();
        $i = 0;

        while($row = $res->fetch_assoc()) {
            $id_kebutuhan = addslashes(htmlentities($row['id']));
            $needs[$i]['id'] = $id_kebutuhan;
            $needs[$i]['kebutuhan'] = addslashes(htmlentities($row['kebutuhan']));
            $needs[$i]['deskripsi'] = addslashes(htmlentities($row['deskripsi']));
            $needs[$i]['satuan'] = addslashes(htmlentities($row['satuan']));
            $needs[$i]['nilai'] = addslashes(htmlentities($row['nilai']));
            
            $stmt2 = $mysqli->prepare("SELECT IFNULL(SUM(nilai), 0) as total_donasi FROM detail_donasi WHERE id_kebutuhan = ?");
            $stmt2->bind_param('i', $id_kebutuhan);
            $stmt2->execute();

            $res2 = $stmt2->get_result();
            $row2 = $res2->fetch_assoc();

            $needs[$i]['total_donasi'] = $row2['total_donasi'];
            
            $i++;
        }
        echo json_encode($needs);
    }

    $stmt->close();

    $mysqli->close();
?>
