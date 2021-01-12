package id.manlyman.petto

class ApiEndPoint {
    companion object {

//        private val SERVER = "http://192.168.1.5/PKL/"
        private val SERVER = "http://pklpetto.000webhostapp.com/"
//        private val SERVER = "http://localhost/Petto/web"
//        private val SERVER = "http://10.0.2.2"
        val Register = SERVER + "?p=Register"
        val Login = SERVER + "?p=Login"
        val Read = SERVER + "?p=Read"
        val ReadByID = SERVER + "?p=ReadByID"
        val ReadCustom = SERVER + "?p=ReadCustom"
        val ReadSearch = SERVER + "?p=ReadSearch"
        val ReadArtiRand = SERVER + "?p=ReadArtiRand"
        val Delete = SERVER + "?p=Delete"
        val Pictures = SERVER + "assets/images/"
        val Upload = SERVER + "?p=Upload"
        val Update = SERVER + "?p=Update"
        val Pengguna2Dokter = SERVER + "?p=Pengguna2Dokter"
        val AddArticle = SERVER + "?p=AddArticle"
        val UpdateArticle = SERVER + "?p=UpdateArticle"
        val AddShop = SERVER + "?p=AddShop"
        val UpdateShop = SERVER + "?p=UpdateShop"
        val AddAC = SERVER + "?p=AddAC"
        val UpdateAC = SERVER + "?p=UpdateAC"
        val AddHealth = SERVER + "?p=AddHealth"
        val UpdateHealth = SERVER + "?p=UpdateHealth"
        val AddCommunity = SERVER + "?p=AddCommunity"
        val UpdateCommunity = SERVER + "?p=UpdateCommunity"
        val CekPP = SERVER + "?p=CekPP"
        val ReadPP = SERVER + "?p=ReadPP"
        val AddPP = SERVER + "?p=AddPP"
        val UpdatePP = SERVER + "?p=UpdatePP"
        val Find_ID = SERVER + "?p=findID"
    }
}
