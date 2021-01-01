package id.manlyman.petto.ui.article

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.list_article.view.*
import org.json.JSONObject

class Holder(view: View) : RecyclerView.ViewHolder(view){
    val fotoUser = view.fotoProfilPengguna
    val judul = view.judulArtikel
    val tgl = view.tanggalArtikel
    val fotoArti = view.fotoArtikel
    val isi = view.isiArtikel
    val more = view.readMore

    fun bind(article: Article, clickListener: OnItemClickListener)
    {
        user(article.penulis, fotoUser)
        judul.text = article.judul
        tgl.text = article.tgl
        picture(article.pict, fotoArti)

        if (article.isi!!.length > 50) {
            isi.text = article.isi!!.substring(0, 50) + "..."
        } else {
            isi.text = article.isi
        }

        more.setOnClickListener {
            clickListener.onItemClicked(article)
        }
    }
}

class AdapterArticle (private val itemClickListener: OnItemClickListener, private val arrayList: ArrayList<Article>) : RecyclerView.Adapter<Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_article,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = arrayList[position]
        holder.bind(user, itemClickListener)
    }

    override fun getItemCount(): Int = arrayList.size
}

interface OnItemClickListener{
    fun onItemClicked(article: Article)
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

private fun user(id: String?, img: ImageView){
    AndroidNetworking.post(ApiEndPoint.ReadByID)
        .addBodyParameter("id", id)
        .addBodyParameter("table", "pengguna")
        .addBodyParameter("idname", "username")
        .setPriority(Priority.MEDIUM)
        .build()
        .getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                if (response?.getString("message")?.contains("ada")!!) {
                    picture(response.getString("picture").toString(), img)
                }
            }

            override fun onError(anError: ANError?) {
                Log.d("OnError", anError?.errorDetail?.toString()!!)
            }
        })
}