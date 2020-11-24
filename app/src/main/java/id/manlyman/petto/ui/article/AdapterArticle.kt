package id.manlyman.petto.ui.article

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.list_article.view.*

class Holder(view: View) : RecyclerView.ViewHolder(view){
    val judul = view.judul
    val penulis = view.penulis
    val deskripsi = view.deskripsi


    fun bind(article: Article,clickListener: OnItemClickListener)
    {
        judul.text = "Judul : " + article.judul
        penulis.text = "Foto : " + article.penulis
        deskripsi.text = "Deskripsi : " + article.deskripsi

        itemView.setOnClickListener {
            clickListener.onItemClicked(article)
        }
    }
}

class AdapterArticle (private val itemClickListener: OnItemClickListener, private val arrayList: ArrayList<Article>) : RecyclerView.Adapter<Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_article,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = arrayList.get(position)
        holder.bind(user, itemClickListener)
    }

    override fun getItemCount(): Int = arrayList.size
}

interface OnItemClickListener{
    fun onItemClicked(article: Article)
}