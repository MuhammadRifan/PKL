package id.manlyman.petto

class ApiEndPoint {
    companion object {

        private val SERVER = "http://192.168.1.6/PKL/"
//        private val SERVER = "http://10.0.3.2:80/PKL/"
        val Register = SERVER + "?p=Register"
        val Login = SERVER + "?p=Login"
        val Read = SERVER + "?p=Read"
        val Delete = SERVER + "?p=delete"
        val Update = SERVER + "?p=update"
        val Find_ID = SERVER + "?p=findID"

    }
}