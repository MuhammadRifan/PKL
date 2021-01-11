package id.manlyman.petto.ui.facility.myshop

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
import kotlinx.android.synthetic.main.list_my_shop.view.*
import java.text.DecimalFormat

class Holder(view: View) : RecyclerView.ViewHolder(view){
    val fotoProduk = view.fotoProduk
    val nama = view.namaProduk
    val harga = view.hargaProduk
    val edit = view.btnEdit
    val hps = view.btnHapus

    fun bind(produk: MyProduk, clickListener: OnItemClickListener)
    {
        nama.text = produk.name
        harga.text = "Rp " + DecimalFormat("#,###").format(produk.harga?.toInt()).toString()
        picture(produk.picture, fotoProduk)

        edit.setOnClickListener {
            clickListener.onItemClicked(produk, 1)
        }

        hps.setOnClickListener {
            clickListener.onItemClicked(produk, 2)
        }
    }
}

class AdapterMyShop (private val itemClickListener: OnItemClickListener, private val arrayList: ArrayList<MyProduk>) : RecyclerView.Adapter<Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_my_shop,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = arrayList[position]
        holder.bind(user, itemClickListener)
    }

    override fun getItemCount(): Int = arrayList.size
}

interface OnItemClickListener{
    fun onItemClicked(produk: MyProduk, sec: Int)
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