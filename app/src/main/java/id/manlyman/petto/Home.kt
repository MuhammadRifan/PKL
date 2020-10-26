package id.manlyman.petto

import android.app.ProgressDialog
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

        supportActionBar?.title = "Data Pengguna"

        hRecyclerView.setHasFixedSize(true)
        hRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadPengguna(){
        val loading = ProgressDialog(this)
            loading.setMessage("Loading...")
            loading.show()

        AndroidNetworking.get(ApiEndPoint.Read)
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
                        arrayList.add(Pengguna(jsonObject.getString("email"),
                            jsonObject.getString("pass")))

                        if (jsonArray?.length() - 1 == i) {
                            loading.dismiss()
                            val adapter = AdapterPengguna(applicationContext, arrayList)
                            adapter.notifyDataSetChanged()
                            hRecyclerView.adapter = adapter
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

    override fun onResume() {
        super.onResume()
        loadPengguna()
    }
}