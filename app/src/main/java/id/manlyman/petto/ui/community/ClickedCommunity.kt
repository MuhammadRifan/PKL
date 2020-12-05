package id.manlyman.petto.ui.community

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
import kotlinx.android.synthetic.main.activity_clicked_community.*
import org.json.JSONObject

class ClickedCommunity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicked_community)

        val sessionId = intent.getStringExtra("ID")
        Toast.makeText(this, sessionId, Toast.LENGTH_SHORT).show()

        Load(sessionId.toString())
    }

    private fun Load(ID: String){
        val loading = ProgressDialog(this)
        loading.setMessage("Loading data...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.ReadID)
            .addBodyParameter("table", "komunitas")
            .addBodyParameter("id", ID)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()

                    txtNamaKomu.text = response?.getString("nama_komunitas").toString()
                    txtDeskripsiKomu.text = response?.getString("deskripsi_komunitas").toString()
                    txtNoTelp.text = response?.getString("kontak").toString()
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                }

            })
    }
}