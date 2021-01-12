package id.manlyman.petto.ui.article.clickedartikel

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
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.R
import id.manlyman.petto.ui.article.Article
import kotlinx.android.synthetic.main.list_produk.view.*

class Holder(view: View) : RecyclerView.ViewHolder(view){
    val fotoUser = view.fotoProduk
    val judul = view.namaProduk
    val isi = view.hargaProduk
    val cv = view.CVProduk

    fun bind(article: Article, clickListener: OnItemClickListener)
    {
        judul.text = article.judul
        isi.text = article.isi
        picture(article.pict, fotoUser)

        if (article.isi!!.length > 50) {
            isi.text = article.isi!!.substring(0, 25) + "..."
        } else {
            isi.text = article.isi
        }

        cv.setOnClickListener {
            clickListener.onItemClicked(article)
        }
    }
}

class AdapterClickedArticle (private val itemClickListener: OnItemClickListener, private val arrayList: ArrayList<Article>) : RecyclerView.Adapter<Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_produk,parent,false))
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