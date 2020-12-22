package id.manlyman.petto.ui.facility.health

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.manlyman.petto.R
import id.manlyman.petto.ui.facility.Facility
import kotlinx.android.synthetic.main.list_health.view.*

class Holder(view: View) : RecyclerView.ViewHolder(view){
    val nama = view.namaPaket
    val btn = view.bukaSC

    fun bind(facility: Facility, clickListener: OnItemClickListener)
    {
        nama.text = facility.nama

        btn.setOnClickListener {
            clickListener.onItemClicked(facility)
        }
    }
}

class AdapterHealth(private val itemClickListener: OnItemClickListener, private val arrayList: ArrayList<Facility>) : RecyclerView.Adapter<Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_health,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = arrayList.get(position)
        holder.bind(user, itemClickListener)
    }

    override fun getItemCount(): Int = arrayList.size
}

interface OnItemClickListener{
    fun onItemClicked(facility: Facility)
}