package id.manlyman.petto.ui.myarticle

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import id.manlyman.petto.ui.article.ActivityAddArticle
import kotlinx.android.synthetic.main.fragment_my_article.*
import kotlinx.android.synthetic.main.fragment_my_article.view.*
import org.json.JSONObject

class MyArticleFragment : Fragment(), OnItemClickListener {

    var arrayList = ArrayList<MyArticle>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_my_article, container, false)
        root.maRecyclerView.setHasFixedSize(true)
        root.maRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val config = FConfig(requireContext())

        root.addMArticle.setOnClickListener {
            if (config.getCustom("srtv", "") == "null" || config.getCustom("srtv", "") == "0") {
                Toast.makeText(requireContext(), "Mohon isi srtv pada halaman profil", Toast.LENGTH_LONG).show()
            } else {
                startActivity(Intent(requireContext(), ActivityAddArticle::class.java))
            }
        }

        return root
    }

    override fun onItemClicked(myArticle: MyArticle, sec: Int) {
        if (sec == 1) {
            val intent = Intent(requireContext(), ActivityAddArticle::class.java)
            intent.putExtra("ID", myArticle.id.toString())
            startActivity(intent)
        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder.apply {
                setPositiveButton("Ok" ) { dialog, id ->
                    delete(myArticle.id.toString())
                }
                setNegativeButton("Cancel" ) { dialog, id -> }
            }
            // Set other dialog properties
            builder.setMessage("Apa anda yakin ?")
                    .setTitle("Hapus Artikel")

            // Create the AlertDialog
            builder.create().show()
        }
    }


    private fun delete(ID: String){
        val config = FConfig(requireContext())
        val loading = ProgressDialog(requireContext())
        loading.setTitle("Deleting ...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.Delete)
            .addBodyParameter("id", ID)
            .addBodyParameter("idname", "id")
            .addBodyParameter("table", "artikel")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("message")?.contains("berhasil")!!) {
                        loadArticle(config.getCustom("uname", ""))
                    }
                    loading.dismiss()
                    Toast.makeText(requireContext(), response.getString("message"), Toast.LENGTH_LONG).show()
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(requireContext(), "Connection Error", Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun loadArticle(uname: String){
        val loading = ProgressDialog(requireContext())
        loading.setMessage("Loading...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.ReadCustom)
            .addBodyParameter("table", "artikel")
            .addBodyParameter("column", "penulis")
            .addBodyParameter("value", uname)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("result").toString() == "Data kosong") {
                        loading.dismiss()

                        maEmpty.visibility = View.VISIBLE
                        maRecyclerView.visibility = View.GONE
                    } else {
                        maEmpty.visibility = View.GONE
                        maRecyclerView.visibility = View.VISIBLE

                        arrayList.clear()
                        val jsonArray = response?.optJSONArray("result")

                        for (i in 0 until jsonArray?.length()!!) {
                            val jsonObject = jsonArray.optJSONObject(i)
                            arrayList.add(MyArticle(jsonObject.getInt("id"),
                                    jsonObject.getString("judul"),
                                    jsonObject.getString("picture")))

                            if (jsonArray.length() - 1 == i) {
                                loading.dismiss()
                                val adapter = AdapterMyArticle(this@MyArticleFragment, arrayList)
                                adapter.notifyDataSetChanged()
                                maRecyclerView.adapter = adapter
                            }
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(requireContext(), "Connection Error", Toast.LENGTH_LONG).show()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        val config = FConfig(requireContext())

        loadArticle(config.getCustom("uname", ""))
    }

}