package id.manlyman.petto.ui.facility.paketproduk

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
import kotlinx.android.synthetic.main.list_produk.view.*
import java.text.DecimalFormat

class AdapterPaketProduk (private val arrayList: ArrayList<PaketProduk>) : RecyclerView.Adapter<AdapterPaketProduk.Holder>() {
    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_produk,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.namaProduk.text = arrayList[position].name
        holder.view.hargaProduk.text = "Rp " + DecimalFormat("#,###.##").format(arrayList[position].harga?.toInt()).toString()
        picture(arrayList[position].picture, holder.view.fotoProduk)
    }

    override fun getItemCount(): Int = arrayList.size
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