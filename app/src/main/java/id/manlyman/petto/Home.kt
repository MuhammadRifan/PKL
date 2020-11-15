package id.manlyman.petto

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject

class Home : AppCompatActivity() {

    var arrayList = ArrayList<Pengguna>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.title = "Artikel"

//        hRecyclerView.setHasFixedSize(true)
//        hRecyclerView.layoutManager = LinearLayoutManager(this)
//
//        btnLogout.setOnClickListener {
//            Logout()
//        }
    }

    private fun loadPengguna(){
        val loading = ProgressDialog(this)
            loading.setMessage("Loading...")
            loading.show()

        AndroidNetworking.post(ApiEndPoint.Read)
            .addBodyParameter("table", "artikel")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    arrayList.clear()
                    val jsonArray = response?.optJSONArray("result")

                    if (jsonArray?.length() == 0) {
                        loading.dismiss()
                        Toast.makeText(applicationContext, "Data Kosong", Toast.LENGTH_LONG).show()
                    }

                    for (i in 0 until jsonArray?.length()!!) {
                        val jsonObject = jsonArray?.optJSONObject(i)
                        arrayList.add(Pengguna(jsonObject.getString("judul"),
                            jsonObject.getString("deskripsi"),
                            jsonObject.getString("penulis")))

                        if (jsonArray?.length() - 1 == i) {
                            loading.dismiss()
                            val adapter = AdapterPengguna(applicationContext, arrayList)
                            adapter.notifyDataSetChanged()
//                            hRecyclerView.adapter = adapter
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun Logout(){
        val loading = ProgressDialog(this)
        loading.setMessage("Logout ...")
        loading.show()

        val Config = FConfig(this)
        Config.delCustom()

        loading.dismiss()

        Toast.makeText(this, "Logout berhasil", Toast.LENGTH_LONG).show()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        loadPengguna()
    }
}