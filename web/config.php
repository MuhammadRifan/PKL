<?php

    $connect = new PDO("mysql:host=localhost;dbname=petto", "root", "");

    function select($db){
      global $connect;

      $result = $connect -> prepare("SELECT * FROM $db");
      $result -> execute();

      return $result;
    }

    function find_by_id($db, $id){
      global $connect;

      $result = $connect -> prepare("SELECT * FROM $db where id=?");
      $result -> bindParam(1, $id);
      $result -> execute();

      return $result;
    }

    function insertPengguna($db, $satu, $dua){
      global $connect;

      $result = $connect -> prepare("INSERT INTO $db VALUES(NULL,?,?)");
      $result -> bindParam(1, $satu);
      $result -> bindParam(2, $dua);
      return $result -> execute();
    }

    function updatePengguna($db, $satu, $dua, $id, $sql){
      global $connect;

      $result = $connect -> prepare("UPDATE $db SET $sql WHERE id=?");
      $result -> bindParam(1, $satu);
      $result -> bindParam(2, $dua);
      $result -> bindParam(3, $id);
      return $result -> execute();
    }

    function delete($db, $id){
      global $connect;

      $result = $connect -> prepare("DELETE FROM $db WHERE id=?");
      $result -> bindParam(1, $id);
      $result -> execute();
    }

?>
