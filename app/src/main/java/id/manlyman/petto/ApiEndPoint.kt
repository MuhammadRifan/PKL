package id.manlyman.petto

class ApiEndPoint {
    companion object {

        private val SERVER = "http://192.168.1.7/PKL/"
        val Login = SERVER + "?p=Login"
        val RegistDokter = SERVER + "?p=registDokter"
        val RegistPengguna = SERVER + "?p=registPengguna"
        val Read = SERVER + "?p=read"
        val Delete = SERVER + "?p=delete"
        val Update = SERVER + "?p=update"
        val Find_ID = SERVER + "?p=findID"

    }
}