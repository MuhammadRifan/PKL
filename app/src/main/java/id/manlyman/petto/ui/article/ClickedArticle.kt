package id.manlyman.petto.ui.article

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.activity_clicked_article.*
import org.json.JSONObject

class ClickedArticle : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicked_article)
        val sessionId = intent.getStringExtra("ID")
        Toast.makeText(this, sessionId, Toast.LENGTH_SHORT).show()

        Load(sessionId.toString())
    }

    private fun Load(ID: String){
        val loading = ProgressDialog(this)
        loading.setMessage("Loading data...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.ReadID)
            .addBodyParameter("table", "artikel")
            .addBodyParameter("id", ID)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()

                    txtJudul.text = response?.getString("judul").toString()
                    txtDeskripsi.text = response?.getString("deskripsi").toString()
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                }

            })
    }
}