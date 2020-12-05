package id.manlyman.petto.ui.article

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.list_article.view.*

class Holder(view: View) : RecyclerView.ViewHolder(view){
    val nama = view.namaKomu
    val deskripsi = view.isiSekilas
    val more = view.readMore

    fun bind(article: Article,clickListener: OnItemClickListener)
    {
        nama.text = article.judul
        deskripsi.text = article.deskripsi

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
        val user = arrayList.get(position)
        holder.bind(user, itemClickListener)
    }

    override fun getItemCount(): Int = arrayList.size
}

interface OnItemClickListener{
    fun onItemClicked(article: Article)
}