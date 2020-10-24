<?php

    require_once('config.php');

    $page = isset($_GET['p']) ? $_GET['p'] : "";

    if ($page == "insert") {
        $email = $_POST['email'];
        $pass = $_POST['pass'];

        if(!$email || !$pass){
            echo json_encode(array('message'=>'required field is empty.'));
        }else{
            if(insertPengguna("test", $email, $pass)){
                echo json_encode(array('message'=>'insert berhasil.'));
            }else{
                echo json_encode(array('message'=>'insert gagal.'));
            }
        }
    } else if ($page == "read") {
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
