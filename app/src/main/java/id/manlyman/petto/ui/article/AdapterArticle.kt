package id.manlyman.petto.ui.article

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.list_article.view.*

class AdapterArticle (private val context: Context, private val arrayList: ArrayList<Article>) : RecyclerView.Adapter<AdapterArticle.Holder>() {
    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_article,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.judul.text = "Judul : " + arrayList?.get(position)?.judul
        holder.view.penulis.text = "Foto : " + arrayList?.get(position)?.penulis
        holder.view.deskripsi.text = "Deskripsi : " + arrayList?.get(position)?.deskripsi
    }

    override fun getItemCount(): Int = arrayList!!.size
}