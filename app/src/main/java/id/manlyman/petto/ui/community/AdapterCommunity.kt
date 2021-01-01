package id.manlyman.petto.ui.community

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
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.list_community.view.*
import org.json.JSONObject

class Holder(val view: View) : RecyclerView.ViewHolder(view){
    val fotoUser = view.fotoProfilPengguna
    val nama = view.namaCommu
    val kota = view.kotaCommu
    val fotoKomu = view.fotoCommu
    val desk = view.deskCommu
    val more = view.readMore6

    fun bind(community: Community, clickListener: OnItemClickListener)
    {
        user(community.owner, fotoUser)
        nama.text = community.name
        kota.text = community.city
        picture(community.picture, fotoKomu)

        if (community.description!!.length > 50) {
            desk.text = community.description.substring(0, 50) + "..."
        } else {
            desk.text = community.description
        }

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

private fun user(id: String?, img: ImageView){
    AndroidNetworking.post(ApiEndPoint.ReadByID)
            .addBodyParameter("id", id)
            .addBodyParameter("table", "pengguna")
            .addBodyParameter("idname", "username")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("message")?.contains("ada")!!) {
                        picture(response.getString("picture").toString(), img)
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                }
            })
}