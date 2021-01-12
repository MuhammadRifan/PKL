package id.manlyman.petto.ui.facility.shop

import android.app.ProgressDialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.R
import id.manlyman.petto.ui.facility.paketproduk.AdapterPaketProduk
import id.manlyman.petto.ui.facility.paketproduk.PaketProduk
import kotlinx.android.synthetic.main.activity_clicked_shop.*
import kotlinx.android.synthetic.main.fragment_shop.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class ClickedShop : AppCompatActivity() {
    var arrayList = ArrayList<PaketProduk>()
    lateinit var sessionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicked_shop)
        csRecyclerView.setHasFixedSize(true)
        csRecyclerView.layoutManager = LinearLayoutManager(this)
        supportActionBar?.title = "Toko"

        sessionId = intent.getStringExtra("ID").toString()

        loadToko(sessionId)
    }

    override fun onStart() {
        super.onStart()
        loadProduk(sessionId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadToko(ID: String){
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
                            if (day == 0) days[x - 1]
                            else ", ${days[x - 1]}"
                            day++
                        }
                    }

                    if (response?.getInt("hari_buka1") == 1) {
                        stringDay +=
                        if (day == 0) days[0]
                        else ", ${days[0]}"
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

    private fun loadProduk(ID: String){
        val loading = ProgressDialog(this)
        loading.setMessage("Loading...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.ReadCustom)
            .addBodyParameter("table", "produk")
            .addBodyParameter("column", "toko")
            .addBodyParameter("value", ID)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("result").toString() == "Data kosong") {
                        loading.dismiss()

                        csEmpty.visibility = View.VISIBLE
                        csRecyclerView.visibility = View.GONE
                    } else {
                        csEmpty.visibility = View.GONE
                        csRecyclerView.visibility = View.VISIBLE

                        arrayList.clear()
                        val jsonArray = response?.optJSONArray("result")

                        for (i in 0 until jsonArray?.length()!!) {
                            val jsonObject = jsonArray.optJSONObject(i)
                            arrayList.add(PaketProduk(jsonObject.getString("name"),
                                jsonObject.getString("harga"),
                                jsonObject.getString("picture")))

                            if (jsonArray.length() - 1 == i) {
                                loading.dismiss()
                                val adapter = AdapterPaketProduk(arrayList)
                                adapter.notifyDataSetChanged()
                                csRecyclerView.adapter = adapter
                            }
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(this@ClickedShop, "Connection Error", Toast.LENGTH_LONG).show()
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