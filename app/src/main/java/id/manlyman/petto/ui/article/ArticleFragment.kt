package id.manlyman.petto.ui.article

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.FConfig
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_article.view.*
import org.json.JSONObject

class ArticleFragment : Fragment(), OnItemClickListener {

    var arrayList = ArrayList<Article>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_article, container, false)
        root.aRecyclerView.setHasFixedSize(true)
        root.aRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val config = FConfig(requireContext())

        if (config.getCustom("level", "") == "1") {
            root.addArticle.visibility = View.VISIBLE

            root.addArticle.setOnClickListener {
                if (config.getCustom("srtv", "") == "null" || config.getCustom("srtv", "") == "0") {
                    Toast.makeText(requireContext(), "Mohon isi srtv pada halaman profil", Toast.LENGTH_LONG).show()
                } else {
                    startActivity(Intent(requireContext(), ActivityAddArticle::class.java))
                }
            }
        } else {
            root.addArticle.visibility = View.GONE
        }

        return root
    }

    override fun onItemClicked(article: Article) {
        val intent = Intent(requireContext(), ClickedArticle::class.java)
        intent.putExtra("ID", article.id.toString())
        startActivity(intent)

//        Toast.makeText(requireContext(), "ID : ${article.id}", Toast.LENGTH_LONG).show()
//        Log.i("USER_", article.judul.toString())
    }

    private fun loadArticle(){
        val loading = ProgressDialog(requireContext())
        loading.setMessage("Loading...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.Read)
            .addBodyParameter("table", "artikel")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("result").toString() == "Data kosong") {
                        loading.dismiss()

                        aEmpty.visibility = View.VISIBLE
                        aRecyclerView.visibility = View.GONE
                    } else {
                        aEmpty.visibility = View.GONE
                        aRecyclerView.visibility = View.VISIBLE

                        arrayList.clear()
                        val jsonArray = response?.optJSONArray("result")

                        for (i in 0 until jsonArray?.length()!!) {
                            val jsonObject = jsonArray.optJSONObject(i)
                            arrayList.add(Article(jsonObject.getInt("id"),
                                jsonObject.getString("penulis"),
                                jsonObject.getString("judul"),
                                jsonObject.getString("isi"),
                                jsonObject.getString("tanggal"),
                                jsonObject.getString("picture")))

                            if (jsonArray.length() - 1 == i) {
                                loading.dismiss()
                                val adapter = AdapterArticle(this@ArticleFragment, arrayList)
                                adapter.notifyDataSetChanged()
                                aRecyclerView.adapter = adapter
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
        loadArticle()
    }
}