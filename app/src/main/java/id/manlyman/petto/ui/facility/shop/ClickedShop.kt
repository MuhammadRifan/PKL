package id.manlyman.petto.ui.facility.shop

import android.app.ProgressDialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.activity_clicked_shop.*
import org.json.JSONObject

class ClickedShop : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicked_shop)
        val sessionId = intent.getStringExtra("ID")

        Load(sessionId.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun Load(ID: String){
        val loading = ProgressDialog(this)
        loading.setMessage("Loading data...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.ReadByID)
            .addBodyParameter("table", "toko")
            .addBodyParameter("id", ID)
            .addBodyParameter("idname", "id")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()

                    namaToko.text = response?.getString("nama").toString()
                    picture(response?.getString("picture").toString(), fotoToko)
                    alamatToko.text = response?.getString("address").toString()

                    val days = arrayOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jum\'at", "Sabtu")
                    var day = 0
                    var stringDay = ""

                    for (x in 2..7) {
                        if (response?.getInt("hari_buka${x}") == x) {
                            stringDay +=
                            if (day == 0) {
                                days[x-1]
                            } else {
                                ", ${days[x-1]}"
                            }
                            day++
                        }
                    }

                    if (response?.getInt("hari_buka1") == 1) {
                        stringDay +=
                        if (day == 0) {
                            days[0]
                        } else {
                            ", ${days[0]}"
                        }
                    }

                    hariBuka.text = stringDay
                    jamToko.text = response?.getString("jam_buka").toString() + " - " + response?.getString("jam_tutup").toString()
                    kontakToko.text = response?.getString("phone").toString()
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