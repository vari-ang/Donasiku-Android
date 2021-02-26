<?php header("Access-Control-Allow-Origin: *"); ?>
<?php 
    include "./connection.php";

    $mysqli = new mysqli($server, $id, $pw, $db);

    if ($mysqli->connect_errno) {
        $result = array('status' => 'error', 'message' => "Gagal terhubung ke database MySQL: " . $mysqli->connect_error);
    }

    // DATA
    $email = addslashes(htmlentities($_POST['email']));
    $password = addslashes(htmlentities($_POST['password']));

    if(empty($email) || empty($password)) {
        $result = array('status' => 'error', 'message' => "Tolong isi semua data yang diminta");
    }
    else {
        $stmt = $mysqli->prepare("SELECT * FROM user WHERE email = ? AND password = ?");
        $stmt->bind_param('ss', $email, $password);
        $stmt->execute();
        $res = $stmt->get_result();
        
        $row = $res->fetch_assoc();
        if($row) {
            $result = array('status' => 'success', 'message' => '');
        }
        else {
            $result = array('status' => 'error', 'message' => "Email atau Password salah");
        }
        $stmt->close();
    }

    echo json_encode($result);

    $mysqli->close();
?>
