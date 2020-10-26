<?php

    require_once('config.php');

    $page = isset($_GET['p']) ? $_GET['p'] : "";

    if ($page == "read") {
            $query = select("test");
            while($row = $query -> fetch()){
                $result[] = $row;
            }

            echo json_encode(array('result'=>$result));
    } else if ($page == "update") {
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
            $query = find_by_id("test", $id);
            $result = $query -> rowCount();

            if($result == 1){
                delete("test", $id);
                echo json_encode(array('message'=>'delete berhasil.'));
            }else{
                echo json_encode(array('message'=>'delete gagal.'));
            }
        }
    } else if ($page == "Login") {
        $email = $_POST['email'];
        $pass = $_POST['pass'];

        if( !$email || !$pass ){
            echo json_encode(array('message'=>'required field is empty.'));
        }else{
            $query = select("test");
            $id = null;
            while($row = $query -> fetch()){
                if ($row['email'] == $email && $row['pass'] == $pass) {
                    $id = $row['id'];
                }
            }

            if($id != null){
                $result = find_by_id("test", $id) -> fetch();
                if ($result['nip'] != NULL) {
                    $result['message'] = "Login dokter berhasil";
                } else {
                    $result['message'] = "Login pengguna berhasil";
                }

                echo json_encode($result);
            }else{
                echo json_encode(array('message'=>'Email atau Password salah'));
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
    } else if ($page == "registPengguna") {
        $email = $_POST['email'];
        $pass = $_POST['pass'];

        if(!$email || !$pass){
            echo json_encode(array('message'=>'required field is empty.'));
        }else{
            if(insertPengguna("test", $email, $pass)){
                echo json_encode(array('message'=>'Register pengguna berhasil.'));
            }else{
                echo json_encode(array('message'=>'Register pengguna gagal.'));
            }
        }
    } else if ($page == "findID") {
        $id = $_POST['id'];

        if( !$id ){
            echo json_encode(array('message'=>'required field is empty.'));
        }else{
            $query = find_by_id("test", $id);
            $result = $query -> rowCount();

            if($result == 1){
                echo json_encode($query -> fetch());
            }else{
                echo json_encode(array('message'=>'ID tidak ditemukan'));
            }
        }
    } else {
        echo json_encode(array('message'=>'Hello'));
    }



?>
