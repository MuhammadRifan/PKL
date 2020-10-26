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
        holder.view.email.text = "Nama : " + arrayList?.get(position)?.email
        holder.view.pass.text = "Password : " + arrayList?.get(position)?.pass
    }

    override fun getItemCount(): Int = arrayList!!.size
}