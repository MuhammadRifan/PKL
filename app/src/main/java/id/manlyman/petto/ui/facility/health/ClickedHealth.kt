package id.manlyman.petto.ui.facility.health

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.activity_clicked_health.*
import org.json.JSONObject

class ClickedHealth : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicked_health)
        val sessionId = intent.getStringExtra("ID")
        Toast.makeText(this, sessionId, Toast.LENGTH_SHORT).show()

        Load(sessionId.toString())
    }

    private fun Load(ID: String){
        val loading = ProgressDialog(this)
        loading.setMessage("Loading data...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.ReadID)
            .addBodyParameter("table", "fasilitas")
            .addBodyParameter("id", ID)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()

                    namaKomunitas.text = response?.getString("nama_fasilitas").toString()
                    deskripsiFaskes.text = response?.getString("deskripsi_fasilitas").toString()
                    alamatAC.text = response?.getString("alamat_fasilitas").toString()
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                }

            })
    }
}