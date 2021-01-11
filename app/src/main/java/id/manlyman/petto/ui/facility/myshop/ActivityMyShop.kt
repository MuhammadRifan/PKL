package id.manlyman.petto.ui.facility.myshop

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.FConfig
import id.manlyman.petto.R
import id.manlyman.petto.ui.facility.paketproduk.ActivityFormPP
import kotlinx.android.synthetic.main.activity_my_shop.*
import org.json.JSONObject

class ActivityMyShop : AppCompatActivity(), OnItemClickListener {
    var arrayList = ArrayList<MyProduk>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_shop)
        msRecyclerView.setHasFixedSize(true)
        msRecyclerView.layoutManager = LinearLayoutManager(this)

        val TB = intent.getStringExtra("TB").toString()

        if (TB == "toko") supportActionBar?.title = "My Shop"
        else supportActionBar?.title = "My Service"

        addProduk.setOnClickListener {
            val intent = Intent(this, ActivityFormPP::class.java)
            if (TB == "toko") intent.putExtra("TEMPAT", "produk")
            else intent.putExtra("TEMPAT", "paket")
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(produk: MyProduk, sec: Int) {
        val TB = intent.getStringExtra("TB").toString()

        val tbl: String =
            if (TB == "toko") "produk"
            else "paket"

        if (sec == 1) {
            val intent = Intent(this, ActivityFormPP::class.java)
            intent.putExtra("ID", produk.id.toString())
            intent.putExtra("TEMPAT", tbl)
            startActivity(intent)
        } else {
            val builder = AlertDialog.Builder(this)
            builder.apply {
                setPositiveButton("Ok" ) { dialog, id ->
                    delete(produk.id.toString(), TB)
                }
                setNegativeButton("Cancel" ) { dialog, id -> }
            }
            // Set other dialog properties
            builder.setMessage("Apa anda yakin ?")
                .setTitle("Hapus $tbl")

            // Create the AlertDialog
            builder.create().show()
//            Toast.makeText(requireContext(), "Hapus ${myartcl.id}/$sec", Toast.LENGTH_LONG).show()
        }
    }

    private fun delete(ID: String, TB: String){
        val loading = ProgressDialog(this)
        loading.setTitle("Deleting ...")
        loading.show()

        val tbl: String =
        if (TB == "toko") "produk"
        else "paket"

        AndroidNetworking.post(ApiEndPoint.Delete)
            .addBodyParameter("id", ID)
            .addBodyParameter("idname", "id")
            .addBodyParameter("table", tbl)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("message")?.contains("berhasil")!!) {
                        loadPP(TB)
                    }
                    loading.dismiss()
                    Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_LONG).show()
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun loadPP(TB: String){
        val config = FConfig(this)
        val loading = ProgressDialog(this)
        loading.setMessage("Loading...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.ReadPP)
            .addBodyParameter("column", TB)
            .addBodyParameter("uname", config.getCustom("uname", ""))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("result").toString() == "Data kosong") {
                        loading.dismiss()

                        msEmpty.visibility = View.VISIBLE
                        msRecyclerView.visibility = View.GONE
                    } else {
                        msEmpty.visibility = View.GONE
                        msRecyclerView.visibility = View.VISIBLE

                        arrayList.clear()
                        val jsonArray = response?.optJSONArray("result")

                        for (i in 0 until jsonArray?.length()!!) {
                            val jsonObject = jsonArray.optJSONObject(i)
                            arrayList.add(MyProduk(jsonObject.getString("id"),
                                jsonObject.getString(TB),
                                jsonObject.getString("name"),
                                jsonObject.getString("harga"),
                                jsonObject.getString("picture")))

                            if (jsonArray.length() - 1 == i) {
                                loading.dismiss()
                                val adapter = AdapterMyShop(this@ActivityMyShop, arrayList)
                                adapter.notifyDataSetChanged()
                                msRecyclerView.adapter = adapter
                            }
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        val TB = intent.getStringExtra("TB").toString()

        loadPP(TB)
    }
}