<?php header("Access-Control-Allow-Origin: *"); ?>
<?php 
    include "./connection.php";

    $mysqli = new mysqli($server, $id, $pw, $db);

    if ($mysqli->connect_errno) {
        $result = array('status' => 'error', 'message' => "Gagal terhubung ke database MySQL: " . $mysqli->connect_error);
    }

    // DATA
    $email = addslashes(htmlentities($_POST['email']));
    $date = addslashes(htmlentities($_POST['date']));
    $donations = json_decode($_POST['donations'], true);
    $result = "";

    if(empty($email) || empty($date) || empty($donations)) {
        $result = array('status' => 'error', 'message' => "Ada Data Yang Kosong");
    }
    else {
        // First, add email & date data to `donasi` table
        $stmt = $mysqli->prepare("INSERT INTO donasi(tanggal, email_user) VALUES (?,?)");
        $stmt->bind_param('ss', $date, $email);
        if($stmt->execute()) {
            // Get the last ID from the INSERT statement above
            $donation_id = $stmt->insert_id;
            
            foreach($donations as $donation) {
                // Donation's data
                $id = $donation['id'];
                $name = $donation['nama'];
                $quantity = $donation['jumlah'];
                $unit = $donation['satuan'];

                $stmt2 = $mysqli->prepare("INSERT INTO detail_donasi(id_donasi, id_kebutuhan, nilai) VALUES (?,?,?)");
                $stmt2->bind_param('iii', $donation_id, $id, $quantity);
                if(!$stmt2->execute()) {
                    $result = array('status' => 'success', 'message' => $mysqli->error);
                }
            }

            $stmt->close();
            $stmt2->close();

            $stmt3 = $mysqli->prepare("SELECT nomor_hp FROM user WHERE email = ?");
            $stmt3->bind_param('s', $email);
            $stmt3->execute();
            $res = $stmt3->get_result();
            
            $row = $res->fetch_assoc();
            if($row) {
                $result = array('status' => 'success', 'message' => $row['nomor_hp']);
            }
            else {
                $result = array('status' => 'error', 'message' => "Error mendapatkan Nomor HP");
            }
            $stmt3->close();
        }
        else {
            $result = array('status' => 'success', 'message' => $mysqli->error);
        }
    }

    echo json_encode($result);

    $mysqli->close();
?>
