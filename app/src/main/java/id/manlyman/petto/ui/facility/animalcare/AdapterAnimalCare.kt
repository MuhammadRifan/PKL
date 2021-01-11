package id.manlyman.petto.ui.facility.animalcare

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.R
import id.manlyman.petto.ui.facility.Facility
import kotlinx.android.synthetic.main.list_facility.view.*
import java.util.*
import kotlin.collections.ArrayList

class Holder(view: View) : RecyclerView.ViewHolder(view){
    val foto = view.fotoFC
    val nama = view.namaFC
    val alamat = view.alamatFC
    val btn = view.bukaFC

    val dayNow = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
    val bukaCol = ContextCompat.getColor(btn.context, R.color.Buka)
    val tutupCol = ContextCompat.getColor(btn.context, R.color.Tutup)

    fun bind(facility: Facility, clickListener: OnItemClickListener)
    {
        picture(facility.pict, foto)
        nama.text = facility.name
        alamat.text = facility.city

        var buka = 0

        if (facility.h1 == dayNow) {
            buka = 1
        } else if (facility.h2 == dayNow) {
            buka = 1
        } else if (facility.h3 == dayNow) {
            buka = 1
        } else if (facility.h4 == dayNow) {
            buka = 1
        } else if (facility.h5 == dayNow) {
            buka = 1
        } else if (facility.h6 == dayNow) {
            buka = 1
        } else if (facility.h7 == dayNow) {
            buka = 1
        }

        if (buka == 1) {
            val hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
            val mnt = Calendar.getInstance()[Calendar.MINUTE]
            var jamand = hour.toString()

            if (mnt.toString().length == 1) jamand += "0$mnt"
            else jamand += mnt.toString()

            val jambuka = facility.buka.toString().split(":")
            val bukadb = (jambuka[0] + jambuka[1]).toInt()

            val jamtutup = facility.tutup.toString().split(":")
            val tutupdb = (jamtutup[0] + jamtutup[1]).toInt()

            if (jamand.toInt() in bukadb..tutupdb) {
                btn.setBackgroundColor(bukaCol)
                btn.text = "Buka"
            } else {
                btn.setBackgroundColor(tutupCol)
                btn.text = "Tutup"
            }
        } else {
            btn.setBackgroundColor(tutupCol)
            btn.text = "Tutup"
        }

        btn.setOnClickListener {
            clickListener.onItemClicked(facility)
        }
    }
}

class AdapterAnimalCare (private val itemClickListener: OnItemClickListener, private val arrayList: ArrayList<Facility>) : RecyclerView.Adapter<Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_facility,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = arrayList[position]
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