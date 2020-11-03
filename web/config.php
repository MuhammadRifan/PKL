<?php

    $connect = new PDO("mysql:host=localhost;dbname=petto", "root", "");

    function select($db){
        global $connect;

        $result = $connect -> prepare("SELECT * FROM $db");
        $result -> execute();

        return $result;
    }

    function find_by_id($db, $idname, $id){
        global $connect;

        $result = $connect -> prepare("SELECT * FROM $db where $idname=?");
        $result -> bindParam(1, $id);
        $result -> execute();

        return $result;
    }

    function delete($db, $idname, $id){
        global $connect;

        $result = $connect -> prepare("DELETE FROM $db WHERE $idname=?");
        $result -> bindParam(1, $id);
        $result -> execute();
    }

    function insertPengguna($satu, $dua, $tiga, $empat, $lima, $enam){
        global $connect;

        $result = $connect -> prepare("INSERT INTO pengguna VALUES(NULL,?,?,?,?,?,?,NULL,NULL)");
        $result -> bindParam(1, $satu);
        $result -> bindParam(2, $dua);
        $result -> bindParam(3, $tiga);
        $result -> bindParam(4, $empat);
        $result -> bindParam(5, $lima);
        $result -> bindParam(6, $enam);
        return $result -> execute();
    }

    function updatePengguna($id, $satu, $dua, $tiga, $empat, $lima, $enam, $tujuh, $delapan){
        global $connect;

        $result = $connect -> prepare("UPDATE pengguna SET nama_pengguna=?,
                                        email_pengguna=?,
                                        password_pengguna=?,
                                        alamat=?, no_telp=?,
                                        foto=?,
                                        nip=?,
                                        spesialis=? WHERE id_pengguna=?");
        $result -> bindParam(1, $satu);
        $result -> bindParam(2, $dua);
        $result -> bindParam(3, $tiga);
        $result -> bindParam(4, $empat);
        $result -> bindParam(5, $lima);
        $result -> bindParam(6, $enam);
        $result -> bindParam(7, $tujuh);
        $result -> bindParam(8, $delapan);
        $result -> bindParam(9, $id);
        return $result -> execute();
    }

    function insertDokter($db, $satu, $dua, $tiga){
      global $connect;

      $result = $connect -> prepare("INSERT INTO $db VALUES(NULL,?,?,?,1)");
      $result -> bindParam(1, $satu);
      $result -> bindParam(2, $dua);
      $result -> bindParam(3, $tiga);
      return $result -> execute();
    }

    function updateDokter($db, $satu, $dua, $tiga, $id, $sql){
      global $connect;

      $result = $connect -> prepare("UPDATE $db SET $sql WHERE id=?");
      $result -> bindParam(1, $satu);
      $result -> bindParam(2, $dua);
      $result -> bindParam(3, $tiga);
      $result -> bindParam(4, $id);
      return $result -> execute();
    }

?>
