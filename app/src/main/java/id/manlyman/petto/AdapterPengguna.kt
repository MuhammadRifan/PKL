package id.manlyman.petto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.test_list.view.*

class AdapterPengguna (private val context: Context, private val arrayList: ArrayList<Pengguna>) : RecyclerView.Adapter<AdapterPengguna.Holder>() {
    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.test_list,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.judul.text = "Judul : " + arrayList?.get(position)?.judul
        holder.view.deskripsi.text = "Deskripsi : " + arrayList?.get(position)?.deskripsi
        holder.view.penulis.text = "Penulis : " + arrayList?.get(position)?.penulis
    }

    override fun getItemCount(): Int = arrayList!!.size
}