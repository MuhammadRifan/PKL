package id.manlyman.petto.ui.article

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener
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
//        Toast.makeText(this, sessionId, Toast.LENGTH_SHORT).show()

        Load(sessionId.toString())
    }

    private fun Load(ID: String){
        val loading = ProgressDialog(this)
        loading.setMessage("Loading data...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.ReadByID)
            .addBodyParameter("table", "artikel")
            .addBodyParameter("id", ID)
            .addBodyParameter("idname", "id")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()

                    judulArtikel.text = response?.getString("judul").toString()
                    penulisArtikel.text = response?.getString("penulis").toString()
                    tanggalArtikel.text = response?.getString("tanggal").toString()
                    picture(response?.getString("picture").toString(), fotoArtikel)
                    isiArtikel.text = response?.getString("isi").toString()
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun picture(url: String?, img: ImageView){
        AndroidNetworking.get(ApiEndPoint.Pictures + url)
                .setTag("Foto")
                .setPriority(Priority.MEDIUM)
                .setBitmapConfig(Bitmap.Config.ARGB_8888)
                .build()
                .getAsBitmap(object : BitmapRequestListener {
                    override fun onResponse(bitmap: Bitmap) {
                        img.setImageBitmap(bitmap)
                    }

                    override fun onError(error: ANError) {
                        Log.d("OnError", error.errorDetail.toString())
                    }
                })
    }
}