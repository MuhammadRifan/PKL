package id.manlyman.petto.ui.facility.health

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
import id.manlyman.petto.ui.facility.Facility
import kotlinx.android.synthetic.main.list_facility.view.*

class Holder(view: View) : RecyclerView.ViewHolder(view){
    val foto = view.fotoFC
    val nama = view.namaFC
    val alamat = view.alamatFC
    val btn = view.bukaFC

    fun bind(facility: Facility, clickListener: OnItemClickListener)
    {
        picture(facility.pict, foto)
        nama.text = facility.name
        alamat.text = facility.city

        btn.setOnClickListener {
            clickListener.onItemClicked(facility)
        }
    }
}

class AdapterHealth(private val itemClickListener: OnItemClickListener, private val arrayList: ArrayList<Facility>) : RecyclerView.Adapter<Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_facility,parent,false))
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