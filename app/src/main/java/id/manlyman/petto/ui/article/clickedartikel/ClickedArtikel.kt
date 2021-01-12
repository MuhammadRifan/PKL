package id.manlyman.petto.ui.article.clickedartikel

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.FConfig
import id.manlyman.petto.R
import id.manlyman.petto.ui.article.Article
import kotlinx.android.synthetic.main.activity_clicked_article.*
import org.json.JSONObject

class ClickedArticle : AppCompatActivity(), OnItemClickListener {
    var arrayList = ArrayList<Article>()
    lateinit var sessionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicked_article)
        caRecyclerView.setHasFixedSize(true)
        caRecyclerView.layoutManager = LinearLayoutManager(this)
        sessionId = intent.getStringExtra("ID").toString()
        supportActionBar?.title = "Artikel"

        loadArtikel(sessionId)
    }

    override fun onStart() {
        super.onStart()
        loadAlso(sessionId)
    }

    override fun onItemClicked(article: Article) {
        val intent = Intent(this, ClickedArticle::class.java)
        intent.putExtra("ID", article.id.toString())
        startActivity(intent)
    }

    private fun loadArtikel(ID: String){
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

    private fun loadAlso(ID: String){
        val config = FConfig(this)
        val loading = ProgressDialog(this)
        loading.setMessage("Loading...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.ReadArtiRand)
            .addBodyParameter("table", "artikel")
            .addBodyParameter("column", "id")
            .addBodyParameter("value", ID)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("result").toString() == "Data kosong") {
                        loading.dismiss()

                        textView3.visibility = View.VISIBLE
                        caRecyclerView.visibility = View.GONE
                    } else {
                        textView3.visibility = View.GONE
                        caRecyclerView.visibility = View.VISIBLE

                        arrayList.clear()
                        val jsonArray = response?.optJSONArray("result")

                        for (i in 0 until jsonArray?.length()!!) {
                            val jsonObject = jsonArray.optJSONObject(i)
                            arrayList.add(Article(jsonObject.getInt("id"),
                                    jsonObject.getString("penulis"),
                                    jsonObject.getString("judul"),
                                    jsonObject.getString("isi"),
                                    jsonObject.getString("tanggal"),
                                    jsonObject.getString("picture")))

                            if (jsonArray.length() - 1 == i) {
                                loading.dismiss()
                                val adapter = AdapterClickedArticle(this@ClickedArticle, arrayList)
                                adapter.notifyDataSetChanged()
                                caRecyclerView.adapter = adapter
                            }
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