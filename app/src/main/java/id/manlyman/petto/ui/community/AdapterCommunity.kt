package id.manlyman.petto.ui.community

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.list_community.view.*

class AdapterCommunity (private val context: Context, private val arrayList: ArrayList<Community>) : RecyclerView.Adapter<AdapterCommunity.Holder>() {
    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_community,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.nama.text = "Judul : " + arrayList?.get(position)?.nama_komunitas
        holder.view.foto.text = "Foto : " + arrayList?.get(position)?.foto_komunitas
        holder.view.deskripsi.text = "Deskripsi : " + arrayList?.get(position)?.deskripsi_komunitas
        holder.view.kontak.text = "Kontak : " + arrayList?.get(position)?.kontak
    }

    override fun getItemCount(): Int = arrayList!!.size
}