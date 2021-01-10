<?php

    $connect = new PDO("mysql:host=localhost;dbname=petto", "root", "");

    function select($db){
        global $connect;

        if ($db == "artikel") {
            $result = $connect -> prepare("SELECT * FROM $db ORDER BY tanggal ASC, id DESC");
        } else {
            $result = $connect -> prepare("SELECT * FROM $db ORDER BY nama ASC");
        }

        $result -> execute();

        return $result;
    }

    function select_custom($db, $sql){
        global $connect;

        if ($db == "artikel") {
            $sql .= " ORDER BY judul ASC";
        }

        $result = $connect -> prepare("SELECT * FROM $db WHERE $sql");
        $result -> execute();

        return $result;
    }

    function update_custom($db, $sql){
        global $connect;

        $result = $connect -> prepare("UPDATE $db SET $sql");
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
        return $result -> execute();
    }

    function insertPengguna($satu, $dua, $tiga, $empat, $lima, $enam){
        global $connect;

        $result = $connect -> prepare("INSERT INTO pengguna VALUES(?,?,?,?,?,null,?)");
        $result -> bindParam(1, $satu);
        $result -> bindParam(2, $dua);
        $result -> bindParam(3, $tiga);
        $result -> bindParam(4, $empat);
        $result -> bindParam(5, $lima);
        $result -> bindParam(6, $enam);
        return $result -> execute();
    }

    function updatePengguna($id, $satu, $dua, $tiga, $empat, $lima, $enam, $tujuh){
        global $connect;

        $result = $connect -> prepare("UPDATE pengguna SET username=?,
                                        pass=?,
                                        email=?,
                                        phone=?,
                                        picture=?,
                                        srtv=?,
                                        level=? WHERE username=?");
        $result -> bindParam(1, $satu);
        $result -> bindParam(2, $dua);
        $result -> bindParam(3, $tiga);
        $result -> bindParam(4, $empat);
        $result -> bindParam(5, $lima);
        $result -> bindParam(6, $enam);
        $result -> bindParam(7, $tujuh);
        $result -> bindParam(8, $id);
        return $result -> execute();
    }

    function insertArticle($satu, $dua, $tiga, $empat, $lima){
        global $connect;

        $result = $connect -> prepare("INSERT INTO artikel VALUES(NULL,?,?,?,?,?)");
        $result -> bindParam(1, $satu);
        $result -> bindParam(2, $dua);
        $result -> bindParam(3, $tiga);
        $result -> bindParam(4, $empat);
        $result -> bindParam(5, $lima);
        return $result -> execute();
    }

    function insertCustom($table, $sql){
        global $connect;

        $result = $connect -> prepare("INSERT INTO $table VALUES $sql");
        return $result -> execute();
    }

    // function insertDokter($db, $satu, $dua, $tiga){
    //   global $connect;
    //
    //   $result = $connect -> prepare("INSERT INTO $db VALUES(NULL,?,?,?,1)");
    //   $result -> bindParam(1, $satu);
    //   $result -> bindParam(2, $dua);
    //   $result -> bindParam(3, $tiga);
    //   return $result -> execute();
    // }

    // function updateDokter($db, $satu, $dua, $tiga, $id, $sql){
    //   global $connect;
    //
    //   $result = $connect -> prepare("UPDATE $db SET $sql WHERE id=?");
    //   $result -> bindParam(1, $satu);
    //   $result -> bindParam(2, $dua);
    //   $result -> bindParam(3, $tiga);
    //   $result -> bindParam(4, $id);
    //   return $result -> execute();
    // }

?>
