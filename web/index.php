<?php
    require_once('config.php');

    date_default_timezone_set("Asia/Jakarta");
    $page = isset($_GET['p']) ? $_GET['p'] : "";

    switch ($page) {
        case 'Read':
            Read();
            break;
        case 'ReadByID':
            ReadByID();
            break;
        case 'ReadCostum':
            ReadCostum();
            break;
        case 'Delete':
            Deletes();
            break;
        case 'Login':
            Login();
            break;
        case 'Register':
            Register();
            break;
        case 'Upload':
            Upload();
            break;
        case 'Update':
            Update();
            break;
        case 'Pengguna2Dokter':
            Pengguna2Dokter();
            break;
        case 'AddArticle':
            AddArticle();
            break;
        case 'UpdateArticle':
            UpdateArticle();
            break;
        case 'CekOldPass':
            CekOldPass();
            break;
        case 'AddShop':
            AddShop();
            break;
        case 'UpdateShop':
            UpdateShop();
            break;
        case 'AddAC':
            AddAC();
            break;
        case 'UpdateAC':
            UpdateAC();
            break;
        case 'AddHealth':
            AddHealth();
            break;
        case 'UpdateHealth':
            UpdateHealth();
            break;
        case 'AddCommunity':
            AddCommunity();
            break;
        case 'UpdateCommunity':
            UpdateCommunity();
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

        if (isset($result)) {
            echo json_encode(array('result' => $result));
        } else {
            echo json_encode(array('result' => 'Data kosong'));
        }

    }

    function ReadByID() {
        $table = $_POST['table'];
        $idname = $_POST['idname'];
        $id = $_POST['id'];

        $result = find_by_id($table, $idname, $id) -> fetch();

        if (!empty($result)) {
            $result['message'] = "Data ada";
            echo json_encode($result);
        }else{
            echo json_encode(array('message' => 'Data tidak ditemukan'));
        }
    }

    function ReadCostum() {
        $table = $_POST['table'];
        $column = $_POST['column'];
        $value = $_POST['value'];

        $query = select_custom($table, "$column = '$value'");
        while($row = $query -> fetch()){
            $result[] = $row;
        }

        if (isset($result)) {
            echo json_encode(array('result' => $result));
        } else {
            echo json_encode(array('result' => 'Data kosong'));
        }
    }

    function Deletes() {
        $table = $_POST['table'];
        $idname = $_POST['idname'];
        $id = $_POST['id'];

        if (delete($table, $idname, $id)) {
            echo json_encode(array('message' => 'Data berhasil dihapus'));
        }else{
            echo json_encode(array('message' => 'Data gagal dihapus'));
        }
    }

    function Login() {
        $email = $_POST['email'];
        $pass = $_POST['pass'];

        if ( !$email || !$pass) {
            echo json_encode(array('message' => 'Form ada yang kosong'));
        } else {
            $result = find_by_id("pengguna", "email", $email) -> fetch();

            if (!empty($result)) {
                if (password_verify($pass, $result['pass']) || $pass == $result['pass']) {
                    if ($result['level'] == 1) {
                        $result['message'] = "Login dokter berhasil";
                    } else {
                        $result['message'] = "Login pengguna berhasil";
                    }

                    echo json_encode($result);
                } else {
                    echo json_encode(array('message' => 'Email atau Password Salah'));
                }
            } else {
                echo json_encode(array('message' => 'Email atau Password Salah'));
            }
        }
    }

    function Register() {
        $nama = $_POST['nama'];
        $email = $_POST['email'];
        $pass = $_POST['pass'];
        $notelp = $_POST['notelp'];
        $foto = $_POST['foto'];

        if(!$nama || !$email || !$pass || !$notelp || !$foto){
            echo json_encode(array('message' => 'Form ada yang kosong'));
        }else{
            $pass = password_hash($_POST['pass'], PASSWORD_BCRYPT);

            if(insertPengguna($nama, $pass, $email, $notelp, $foto, 0)){
                echo json_encode(array('message' => 'Register pengguna berhasil'));
            }else{
                echo json_encode(array('message' => 'Register pengguna gagal'));
            }
        }
    }

    function Upload() {
        if(isset($_FILES['image'])){
            $file = $_FILES['image'];
            $folder = $_POST['folder'];

            // Properties
            $name = $file['name'];
            $tmp = $file['tmp_name'];
            $size = $file['size'];
            $error = $file['error'];

            // Extension
            $ext = explode("." , $name);
            $ext = strtolower(end($ext));

            $allowed = ['png', 'jpg', 'jpeg'];

            if(in_array($ext , $allowed)){
                if($error == 0){
                    if($size <= 2097152 /* 2 MB */){

                        $name_new = uniqid("" , true) . "." . $ext;
                        $destination = "assets/images/" . $folder . "/" . $name_new;

                        if(move_uploaded_file($tmp , $destination)){
                            echo "$folder/$name_new";
                        }else{
                            echo 'Upload Gagal';
                        }

                    }else{
                        echo 'File terlalu besar (max 2 MB)';
                    }
                }else{
                    echo 'Error';
                }
            }else{
                echo 'Upload Gagal';
            }
        }
    }

    function Update() {
        $id = $_POST['id'];
        $uname = $_POST['nama'];
        $email = $_POST['email'];
        $pass = $_POST['pass'];
        $notelp = $_POST['notelp'];
        $foto = $_POST['foto'];
        $strv = $_POST['strv'];
        $level = $_POST['level'];

        if (!$uname || !$email || !$pass || !$notelp || !$foto || !$strv) {
            echo json_encode(array('message' => 'Form ada yang kosong'));
        } else {
            $sql = find_by_id("pengguna", "username", $id) -> fetch();

            if ($sql['pass'] != $pass) {
                $pass = password_hash($_POST['pass'], PASSWORD_BCRYPT);
            }

            if(updatePengguna($id, $uname, $pass, $email, $notelp, $foto, $strv, $level)){
                $result = find_by_id("pengguna", "username", $uname) -> fetch();
                $result['message'] = "Update berhasil";
                echo json_encode($result);
            }else{
                echo json_encode(array('message' => 'Update gagal'));
            }
        }
    }

    function Pengguna2Dokter() {
        $uname = $_POST['uname'];

        if(!$uname){
            echo json_encode(array('message' => 'Gagal, Silahkan Hubungi Admin'));
        }else{
            $result = find_by_id("pengguna", "username", $uname) -> fetch();

            $query = updatePengguna($result['username'],
                                    $result['username'],
                                    $result['pass'],
                                    $result['email'],
                                    $result['phone'],
                                    $result['picture'],
                                    $result['srtv'],
                                    1);
            if($query){
                echo json_encode(array('message'=>'Update berhasil'));
            }else{
                echo json_encode(array('message'=>'Gagal, Silahkan Hubungi Admin'));
            }
        }
    }

    function CekOldPass() {
        $old = $_POST['passOld'];
        $pass = $_POST['pass'];

        if (password_verify($pass, $old)) {
            echo json_encode(array('message' => 'Yes'));
        } else {
            echo json_encode(array('message' => 'No'));
        }
    }

    function AddArticle() {
        $id = $_POST['penulis'];
        $judul = $_POST['judul'];
        $isi = $_POST['isi'];
        $tgl = date("d-m-Y");
        $foto = $_POST['foto'];

        if(!$id || !$judul || !$isi || !$foto){
            echo json_encode(array('message' => 'Form ada yang kosong'));
        }else{
            if(insertArticle($id, $judul, $isi, $tgl, $foto)){
                echo json_encode(array('message' => 'Posting artikel berhasil'));
            }else{
                echo json_encode(array('message' => 'Posting artikel gagal'));
            }
        }
    }

    function UpdateArticle() {
        $id = $_POST['id'];
        $judul = $_POST['judul'];
        $isi = $_POST['isi'];
        $picture = $_POST['picture'];

        if(!$judul || !$isi){
            echo json_encode(array('message' => 'Form ada yang kosong'));
        }else{
            if ($picture == "") {
                $sql = "judul = '$judul', isi = '$isi' WHERE id = $id";
            } else {
                $sql = "judul = '$judul', isi = '$isi', picture = '$picture' WHERE id = $id";
            }

            if(update_custom("artikel", $sql)){
                $result = find_by_id("artikel", "id", $id) -> fetch();
                $result['message'] = "Update artikel berhasil";
                echo json_encode($result);
            }else{
                echo json_encode(array('message' => 'Update artikel gagal'));
            }
        }
    }

    function AddShop() {
        $owner = $_POST['owner'];
        $nama = $_POST['nama'];
        $address = $_POST['address'];
        $city = $_POST['city'];
        $phone = $_POST['phone'];
        $picture = $_POST['picture'];
        $h1 = $_POST['h1'];
        $h2 = $_POST['h2'];
        $h3 = $_POST['h3'];
        $h4 = $_POST['h4'];
        $h5 = $_POST['h5'];
        $h6 = $_POST['h6'];
        $h7 = $_POST['h7'];
        $jb = $_POST['buka'];
        $jt = $_POST['tutup'];

        if(!$owner || !$nama || !$address || !$city || !$phone || !$picture || !$jb || !$jt){
            echo json_encode(array('message' => 'Form ada yang kosong'));
        } else {
            if ($h1 || $h2 || $h3 || $h4 || $h5 || $h6 || $h7) {

                $h1 = ($h1 == "true") ? 1 : 0;
                $h2 = ($h2 == "true") ? 2 : 0;
                $h3 = ($h3 == "true") ? 3 : 0;
                $h4 = ($h4 == "true") ? 4 : 0;
                $h5 = ($h5 == "true") ? 5 : 0;
                $h6 = ($h6 == "true") ? 6 : 0;
                $h7 = ($h7 == "true") ? 7 : 0;

                $sql = "(NULL, '$owner', '$nama', '$address', '$city', '$phone', '$picture', $h1, $h2, $h3, $h4, $h5, $h6, $h7, '$jb', '$jt')";
                if(insertCustom("toko", $sql)){
                    echo json_encode(array('message' => 'Tambah toko berhasil'));
                }else{
                    echo json_encode(array('message' => 'Tambah toko gagal'));
                }
            } else {
                echo json_encode(array('message' => 'Form ada yang kosong'));
            }
        }
    }

    function UpdateShop() {
        $id = $_POST['id'];
        $nama = $_POST['nama'];
        $address = $_POST['address'];
        $city = $_POST['city'];
        $phone = $_POST['phone'];
        $picture = $_POST['picture'];
        $h1 = $_POST['h1'];
        $h2 = $_POST['h2'];
        $h3 = $_POST['h3'];
        $h4 = $_POST['h4'];
        $h5 = $_POST['h5'];
        $h6 = $_POST['h6'];
        $h7 = $_POST['h7'];
        $jb = $_POST['buka'];
        $jt = $_POST['tutup'];

        if(!$nama || !$address || !$city || !$phone || !$jb || !$jt){
            echo json_encode(array('message' => 'Form ada yang kosong1'));
        } else {
            if ($h1 || $h2 || $h3 || $h4 || $h5 || $h6 || $h7) {
                $h1 = ($h1 == "true") ? 1 : 0;
                $h2 = ($h2 == "true") ? 2 : 0;
                $h3 = ($h3 == "true") ? 3 : 0;
                $h4 = ($h4 == "true") ? 4 : 0;
                $h5 = ($h5 == "true") ? 5 : 0;
                $h6 = ($h6 == "true") ? 6 : 0;
                $h7 = ($h7 == "true") ? 7 : 0;

                if ($picture == "") {
                    $sql = "nama = '$nama',
                    address = '$address',
                    city = '$city',
                    phone = '$phone',
                    hari_buka1 = $h1,
                    hari_buka2 = $h2,
                    hari_buka3 = $h3,
                    hari_buka4 = $h4,
                    hari_buka5 = $h5,
                    hari_buka6 = $h6,
                    hari_buka7 = $h7,
                    jam_buka = '$jb',
                    jam_tutup = '$jt' WHERE id = $id";
                } else {
                    $sql = "nama = '$nama',
                    address = '$address',
                    city = '$city',
                    phone = '$phone',
                    picture = '$picture',
                    hari_buka1 = $h1,
                    hari_buka2 = $h2,
                    hari_buka3 = $h3,
                    hari_buka4 = $h4,
                    hari_buka5 = $h5,
                    hari_buka6 = $h6,
                    hari_buka7 = $h7,
                    jam_buka = '$jb',
                    jam_tutup = '$jt' WHERE id = $id";
                }

                if(update_custom("toko", $sql)){
                    $result = find_by_id("toko", "id", $id) -> fetch();
                    $result['message'] = "Update toko berhasil";
                    echo json_encode($result);
                }else{
                    echo json_encode(array('message' => 'Update toko gagal'));
                }
            } else {
                echo json_encode(array('message' => 'Form ada yang kosong2'));
            }
        }
    }

    function AddAC() {
        $owner = $_POST['owner'];
        $nama = $_POST['nama'];
        $address = $_POST['address'];
        $city = $_POST['city'];
        $phone = $_POST['phone'];
        $picture = $_POST['picture'];
        $h1 = $_POST['h1'];
        $h2 = $_POST['h2'];
        $h3 = $_POST['h3'];
        $h4 = $_POST['h4'];
        $h5 = $_POST['h5'];
        $h6 = $_POST['h6'];
        $h7 = $_POST['h7'];
        $jb = $_POST['buka'];
        $jt = $_POST['tutup'];

        if(!$owner || !$nama || !$address || !$city || !$phone || !$picture || !$jb || !$jt){
            echo json_encode(array('message' => 'Form ada yang kosong'));
        } else {
            if ($h1 || $h2 || $h3 || $h4 || $h5 || $h6 || $h7) {

                $h1 = ($h1 == "true") ? 1 : 0;
                $h2 = ($h2 == "true") ? 2 : 0;
                $h3 = ($h3 == "true") ? 3 : 0;
                $h4 = ($h4 == "true") ? 4 : 0;
                $h5 = ($h5 == "true") ? 5 : 0;
                $h6 = ($h6 == "true") ? 6 : 0;
                $h7 = ($h7 == "true") ? 7 : 0;

                $sql = "(NULL, '$owner', '$nama', '$address', '$city', '$phone', '$picture', $h1, $h2, $h3, $h4, $h5, $h6, $h7, '$jb', '$jt')";
                if(insertCustom("animalcare", $sql)){
                    echo json_encode(array('message' => 'Tambah animal care berhasil'));
                }else{
                    echo json_encode(array('message' => 'Tambah animal care gagal'));
                }
            } else {
                echo json_encode(array('message' => 'Form ada yang kosong'));
            }

        }
    }

    function UpdateAC() {
        $id = $_POST['id'];
        $nama = $_POST['nama'];
        $address = $_POST['address'];
        $city = $_POST['city'];
        $phone = $_POST['phone'];
        $picture = $_POST['picture'];
        $h1 = $_POST['h1'];
        $h2 = $_POST['h2'];
        $h3 = $_POST['h3'];
        $h4 = $_POST['h4'];
        $h5 = $_POST['h5'];
        $h6 = $_POST['h6'];
        $h7 = $_POST['h7'];
        $jb = $_POST['buka'];
        $jt = $_POST['tutup'];

        if(!$nama || !$address || !$city || !$phone || !$jb || !$jt){
            echo json_encode(array('message' => 'Form ada yang kosong1'));
        } else {
            if ($h1 || $h2 || $h3 || $h4 || $h5 || $h6 || $h7) {
                $h1 = ($h1 == "true") ? 1 : 0;
                $h2 = ($h2 == "true") ? 2 : 0;
                $h3 = ($h3 == "true") ? 3 : 0;
                $h4 = ($h4 == "true") ? 4 : 0;
                $h5 = ($h5 == "true") ? 5 : 0;
                $h6 = ($h6 == "true") ? 6 : 0;
                $h7 = ($h7 == "true") ? 7 : 0;

                if ($picture == "") {
                    $sql = "nama = '$nama',
                    address = '$address',
                    city = '$city',
                    phone = '$phone',
                    hari_buka1 = $h1,
                    hari_buka2 = $h2,
                    hari_buka3 = $h3,
                    hari_buka4 = $h4,
                    hari_buka5 = $h5,
                    hari_buka6 = $h6,
                    hari_buka7 = $h7,
                    jam_buka = '$jb',
                    jam_tutup = '$jt' WHERE id = $id";
                } else {
                    $sql = "nama = '$nama',
                    address = '$address',
                    city = '$city',
                    phone = '$phone',
                    picture = '$picture',
                    hari_buka1 = $h1,
                    hari_buka2 = $h2,
                    hari_buka3 = $h3,
                    hari_buka4 = $h4,
                    hari_buka5 = $h5,
                    hari_buka6 = $h6,
                    hari_buka7 = $h7,
                    jam_buka = '$jb',
                    jam_tutup = '$jt' WHERE id = $id";
                }

                if(update_custom("animalcare", $sql)){
                    $result = find_by_id("animalcare", "id", $id) -> fetch();
                    $result['message'] = "Update animal care berhasil";
                    echo json_encode($result);
                }else{
                    echo json_encode(array('message' => 'Update animal care gagal'));
                }
            } else {
                echo json_encode(array('message' => 'Form ada yang kosong2'));
            }
        }
    }

    function AddHealth() {
        $sip = $_POST['sip'];
        $owner = $_POST['owner'];
        $nama = $_POST['nama'];
        $deskripsi = $_POST['deskripsi'];
        $address = $_POST['address'];
        $city = $_POST['city'];
        $phone = $_POST['phone'];
        $picture = $_POST['picture'];
        $h1 = $_POST['h1'];
        $h2 = $_POST['h2'];
        $h3 = $_POST['h3'];
        $h4 = $_POST['h4'];
        $h5 = $_POST['h5'];
        $h6 = $_POST['h6'];
        $h7 = $_POST['h7'];
        $jb = $_POST['buka'];
        $jt = $_POST['tutup'];

        if(!$sip || !$owner || !$nama || !$deskripsi || !$address || !$city || !$phone || !$picture || !$jb || !$jt){
            echo json_encode(array('message' => 'Form ada yang kosong'));
        } else {
            if ($h1 || $h2 || $h3 || $h4 || $h5 || $h6 || $h7) {

                $h1 = ($h1 == "true") ? 1 : 0;
                $h2 = ($h2 == "true") ? 2 : 0;
                $h3 = ($h3 == "true") ? 3 : 0;
                $h4 = ($h4 == "true") ? 4 : 0;
                $h5 = ($h5 == "true") ? 5 : 0;
                $h6 = ($h6 == "true") ? 6 : 0;
                $h7 = ($h7 == "true") ? 7 : 0;

                $sql = "($sip, '$owner', '$nama', '$deskripsi', '$address', '$city', '$phone', '$picture', $h1, $h2, $h3, $h4, $h5, $h6, $h7, '$jb', '$jt')";
                if(insertCustom("faskes", $sql)){
                    echo json_encode(array('message' => 'Tambah fasilitas kesehatan berhasil'));
                }else{
                    echo json_encode(array('message' => 'Tambah fasilitas kesehatan gagal'));
                }
            } else {
                echo json_encode(array('message' => 'Form ada yang kosong'));
            }

        }
    }

    function UpdateHealth() {
        $oldsip = $_POST['siplama'];
        $sip = $_POST['sip'];
        $nama = $_POST['nama'];
        $deskripsi = $_POST['deskripsi'];
        $address = $_POST['address'];
        $city = $_POST['city'];
        $phone = $_POST['phone'];
        $picture = $_POST['picture'];
        $h1 = $_POST['h1'];
        $h2 = $_POST['h2'];
        $h3 = $_POST['h3'];
        $h4 = $_POST['h4'];
        $h5 = $_POST['h5'];
        $h6 = $_POST['h6'];
        $h7 = $_POST['h7'];
        $jb = $_POST['buka'];
        $jt = $_POST['tutup'];

        if(!$sip || !$nama || !$deskripsi || !$address || !$city || !$phone || !$jb || !$jt){
            echo json_encode(array('message' => 'Form ada yang kosong1'));
        } else {
            if ($h1 || $h2 || $h3 || $h4 || $h5 || $h6 || $h7) {
                $h1 = ($h1 == "true") ? 1 : 0;
                $h2 = ($h2 == "true") ? 2 : 0;
                $h3 = ($h3 == "true") ? 3 : 0;
                $h4 = ($h4 == "true") ? 4 : 0;
                $h5 = ($h5 == "true") ? 5 : 0;
                $h6 = ($h6 == "true") ? 6 : 0;
                $h7 = ($h7 == "true") ? 7 : 0;

                if ($picture == "") {
                    $sql = "sip = '$sip',
                    nama = '$nama',
                    description = '$deskripsi',
                    address = '$address',
                    city = '$city',
                    phone = '$phone',
                    hari_buka1 = $h1,
                    hari_buka2 = $h2,
                    hari_buka3 = $h3,
                    hari_buka4 = $h4,
                    hari_buka5 = $h5,
                    hari_buka6 = $h6,
                    hari_buka7 = $h7,
                    jam_buka = '$jb',
                    jam_tutup = '$jt' WHERE sip = $oldsip";
                } else {
                    $sql = "sip = '$sip',
                    nama = '$nama',
                    description = '$deskripsi',
                    address = '$address',
                    city = '$city',
                    phone = '$phone',
                    picture = '$picture',
                    hari_buka1 = $h1,
                    hari_buka2 = $h2,
                    hari_buka3 = $h3,
                    hari_buka4 = $h4,
                    hari_buka5 = $h5,
                    hari_buka6 = $h6,
                    hari_buka7 = $h7,
                    jam_buka = '$jb',
                    jam_tutup = '$jt' WHERE sip = $oldsip";
                }

                if(update_custom("faskes", $sql)){
                    $result = find_by_id("faskes", "sip", $sip) -> fetch();
                    $result['message'] = "Update fasilitas kesehatan berhasil";
                    echo json_encode($result);
                }else{
                    echo json_encode(array('message' => 'Update fasilitas kesehatan gagal'));
                }
            } else {
                echo json_encode(array('message' => 'Form ada yang kosong2'));
            }
        }
    }

    function AddCommunity() {
        $owner = $_POST['owner'];
        $nama = $_POST['nama'];
        $des = $_POST['deskripsi'];
        $alamat = $_POST['alamat'];
        $kota = $_POST['kota'];
        $phone = $_POST['phone'];
        $picture = $_POST['picture'];

        if(!$owner || !$nama || !$des || !$alamat || !$kota || !$phone || !$picture){
            echo json_encode(array('message' => 'Form ada yang kosong'));
        }else{
            $sql = "(NULL, '$owner', '$nama', '$des', '$alamat', '$kota', '$phone', '$picture')";
            if(insertCustom("komunitas", $sql)){
                echo json_encode(array('message' => 'Komunitas berhasil dibuat'));
            }else{
                echo json_encode(array('message' => 'Komunitas gagal dibuat'));
            }
        }
    }

    function UpdateCommunity() {
        $id = $_POST['id'];
        $nama = $_POST['nama'];
        $des = $_POST['deskripsi'];
        $alamat = $_POST['alamat'];
        $kota = $_POST['kota'];
        $phone = $_POST['phone'];
        $picture = $_POST['picture'];

        if(!$nama || !$des || !$alamat || !$kota || !$phone){
            echo json_encode(array('message' => 'Form ada yang kosong'));
        }else{
            if ($picture == "") {
                $sql = "nama = '$nama', description = '$des', address = '$alamat', kota = '$kota', phone = '$phone' WHERE id = $id";
            } else {
                $sql = "nama = '$nama', description = '$des', address = '$alamat', kota = '$kota', phone = '$phone', picture = '$picture' WHERE id = $id";
            }

            if(update_custom("komunitas", $sql)){
                $result = find_by_id("komunitas", "id", $id) -> fetch();
                $result['message'] = "Update komunitas berhasil";
                echo json_encode($result);
            }else{
                echo json_encode(array('message' => 'Update komunitas gagal'));
            }
        }
    }
?>
