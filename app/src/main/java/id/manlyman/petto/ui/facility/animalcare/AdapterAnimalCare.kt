package id.manlyman.petto.ui.facility.animalcare

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.manlyman.petto.R
import id.manlyman.petto.ui.facility.ShopCare
import kotlinx.android.synthetic.main.list_shop_care.view.*

class AdapterAnimalCare (private val context: Context, private val arrayList: ArrayList<ShopCare>) : RecyclerView.Adapter<AdapterAnimalCare.Holder>() {
    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_shop_care,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.nama.text = "Nama Perawatan : " + arrayList?.get(position)?.nama
        holder.view.alamat.text = "Alamat Perawatan : " + arrayList?.get(position)?.alamat
        holder.view.deskripsi.text = "Deskripsi Perawatan : " + arrayList?.get(position)?.deskripsi
    }

    override fun getItemCount(): Int = arrayList!!.size
}