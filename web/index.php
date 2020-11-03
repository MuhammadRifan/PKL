<?php

    require_once('config.php');

    $page = isset($_GET['p']) ? $_GET['p'] : "";

    switch ($page) {
        case 'Read':
            Read();
            break;
        case 'Login':
            Login();
            break;
        case 'Register':
            Register();
            break;
        case 'Pengguna2Dokter':
            Pengguna2Dokter();
            break;
        default:
            echo json_encode(array('message'=>'Connection Not Found!'));
            break;
    }

    function Read() {
        $table = $_POST['table'];
        $query = select($table);
        while($row = $query -> fetch()){
            $result[] = $row;
        }

        echo json_encode(array('result'=>$result));
    }

    function Login() {
        $email = $_POST['email'];
        $pass = $_POST['pass'];

        if ( !$email || !$pass) {
            echo json_encode(array('message' => 'Form ada yang kosong'));
        } else {
            $query = select("pengguna");
            $id = null;
            while ($row = $query -> fetch()) {
                if ($row['email_pengguna'] == $email && $row['password_pengguna'] == $pass) {
                    $id = $row['id_pengguna'];
                }
            }

            if ($id != null) {
                $result = find_by_id("pengguna", "id_pengguna", $id) -> fetch();
                if ($result['nip'] != NULL) {
                    $result['message'] = "Login dokter berhasil";
                } else {
                    $result['message'] = "Login pengguna berhasil";
                }

                echo json_encode($result);
            } else {
                echo json_encode(array('message' => 'Email atau Password Salah'));
            }
        }
    }

    function Register() {
        $nama = $_POST['nama'];
        $email = $_POST['email'];
        $pass = $_POST['pass'];
        $alamat = $_POST['alamat'];
        $notelp = $_POST['notelp'];
        $foto = $_POST['foto'];

        if(!$email || !$pass || !$nama || !$alamat || !$notelp || !$foto){
            echo json_encode(array('message' => 'Form ada yang kosong'));
        }else{
            if(insertPengguna($nama, $email, $pass, $alamat, $notelp, $foto)){
                echo json_encode(array('message' => 'Register pengguna berhasil'));
            }else{
                echo json_encode(array('message' => 'Register pengguna gagal'));
            }
        }
    }

    function Pengguna2Dokter() {
        $id = $_POST['id'];
        $nip = $_POST['nip'];
        $spesialis = $_POST['spesialis'];

        if(!$id || !$nip || !$spesialis){
            echo json_encode(array('message' => 'Form ada yang kosong'));
        }else{
            $result = find_by_id("pengguna", "id_pengguna", $id) -> fetch();

            $query = updatePengguna($id, $result['nama_pengguna'], $result['email_pengguna'], $result['password_pengguna'], $result['alamat'], $result['no_telp'], $result['foto'], $nip, $spesialis);
            if($query){
                echo json_encode(array('message'=>'Update berhasil'));
            }else{
                echo json_encode(array('message'=>'Update gagal'));
            }
        }
    }

    if ($page == "update") {
        $email = $_POST['email'];
        $pass = $_POST['pass'];
        $id = $_POST['id'];

        if(!$email || !$pass || !$id){
            echo json_encode(array('message'=>'required field is empty.'));
        }else{
            $query = updatePengguna("test", $email, $pass, $id, "email=?, pass=?");
            if($query){
                echo json_encode(array('message'=>'update berhasil.'));
            }else{
                echo json_encode(array('message'=>'update gagal.'));
            }
        }
    } else if ($page == "delete") {
        $id = $_POST['id'];

        if( !$id ){
            echo json_encode(array('message'=>'required field is empty.'));
        }else{
            $query = find_by_id("test", "id", $id);
            $result = $query -> rowCount();

            if($result == 1){
                delete("test", $id);
                echo json_encode(array('message'=>'delete berhasil.'));
            }else{
                echo json_encode(array('message'=>'delete gagal.'));
            }
        }
    } else if ($page == "registDokter") {
        $email = $_POST['email'];
        $pass = $_POST['pass'];
        $nip = $_POST['nip'];

        if(!$email || !$pass || !$nip){
            echo json_encode(array('message'=>'required field is empty.'));
        }else{
            if(insertDokter("test", $email, $pass, $nip)){
                echo json_encode(array('message'=>'Register dokter berhasil.'));
            }else{
                echo json_encode(array('message'=>'Register dokter gagal.'));
            }
        }
    } else if ($page == "findID") {
        $id = $_POST['id'];

        if( !$id ){
            echo json_encode(array('message'=>'required field is empty.'));
        }else{
            $query = find_by_id("test", "id", $id);
            $result = $query -> rowCount();

            if($result == 1){
                echo json_encode($query -> fetch());
            }else{
                echo json_encode(array('message'=>'ID tidak ditemukan'));
            }
        }
    } else {
        // echo json_encode(array('message'=>'Hello'));
    }



?>
