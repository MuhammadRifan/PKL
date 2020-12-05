package id.manlyman.petto.ui.community

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.list_community.view.*

class Holder(val view: View) : RecyclerView.ViewHolder(view){
    val nama = view.namaKomu
    val deskripsi = view.isiSekilas
    val more = view.readMore

    fun bind(community: Community, clickListener: OnItemClickListener)
    {
        nama.text = community.nama_komunitas
        deskripsi.text = community.deskripsi_komunitas

        more.setOnClickListener {
            clickListener.onItemClicked(community)
        }
    }
}

class AdapterCommunity (private val itemClickListener: OnItemClickListener, private val arrayList: ArrayList<Community>) : RecyclerView.Adapter<Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_community,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = arrayList.get(position)
        holder.bind(user, itemClickListener)
    }

    override fun getItemCount(): Int = arrayList!!.size
}

interface OnItemClickListener{
    fun onItemClicked(community: Community)
}