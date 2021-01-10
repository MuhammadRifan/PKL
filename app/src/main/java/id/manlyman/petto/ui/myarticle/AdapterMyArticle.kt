package id.manlyman.petto.ui.myarticle

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
import kotlinx.android.synthetic.main.list_my_article.view.*

class Holder(view: View) : RecyclerView.ViewHolder(view){
    val fotoArti = view.fotoArtikel
    val judul = view.judulArtikel
    val edit = view.btnEdit
    val hps = view.btnHapus

    fun bind(myartcl: myartcl, clickListener: OnItemClickListener)
    {
        judul.text = myartcl.judul
        picture(myartcl.pict, fotoArti)

        edit.setOnClickListener {
            clickListener.onItemClicked(myartcl, 1)
        }

        hps.setOnClickListener {
            clickListener.onItemClicked(myartcl, 2)
        }
    }
}

class AdapterMyArticle (private val itemClickListener: OnItemClickListener, private val arrayList: ArrayList<myartcl>) : RecyclerView.Adapter<Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_my_article,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = arrayList[position]
        holder.bind(user, itemClickListener)
    }

    override fun getItemCount(): Int = arrayList.size
}

interface OnItemClickListener{
    fun onItemClicked(myartcl: myartcl, sec: Int)
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