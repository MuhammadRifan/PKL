package id.manlyman.petto.ui.community

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.FConfig
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.activity_clicked_article.*
import kotlinx.android.synthetic.main.fragment_community.*
import kotlinx.android.synthetic.main.fragment_community.view.*
import org.json.JSONObject

class CommunityFragment : Fragment(), OnItemClickListener {

    var arrayList = ArrayList<Community>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_community, container, false)
        root.cRecyclerView.setHasFixedSize(true)
        root.cRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val config = FConfig(requireContext())
        val uname = config.getCustom("uname", "")

        cekKomu(uname, root.addCommunity)

        return root
    }

    override fun onItemClicked(community: Community) {
        val intent = Intent(requireContext(), ClickedCommunity::class.java)
        intent.putExtra("ID", community.id.toString())
        startActivity(intent)

//        Toast.makeText(requireContext(), "ID : ${community.id}", Toast.LENGTH_LONG)
//                .show()
//        Log.i("USER_", community.id.toString())
    }

    private fun loadCommunity(){
        val loading = ProgressDialog(requireContext())
        loading.setMessage("Loading...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.Read)
                .addBodyParameter("table", "komunitas")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        if (response?.getString("result").toString() == "Data kosong") {
                            loading.dismiss()

                            cEmpty.visibility = View.VISIBLE
                            cRecyclerView.visibility = View.GONE
                        } else {
                            cEmpty.visibility = View.GONE
                            cRecyclerView.visibility = View.VISIBLE

                            arrayList.clear()
                            val jsonArray = response?.optJSONArray("result")

                            for (i in 0 until jsonArray?.length()!!) {
                                val jsonObject = jsonArray?.optJSONObject(i)
                                arrayList.add(Community(jsonObject.getInt("id"),
                                        jsonObject.getString("owner"),
                                        jsonObject.getString("nama"),
                                        jsonObject.getString("description"),
                                        jsonObject.getString("kota"),
                                        jsonObject.getString("picture")))

                                if (jsonArray?.length() - 1 == i) {
                                    loading.dismiss()
                                    val adapter = AdapterCommunity(this@CommunityFragment, arrayList)
                                    adapter.notifyDataSetChanged()
                                    cRecyclerView.adapter = adapter
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

    private fun cekKomu(ID: String, Btn: FloatingActionButton){
        AndroidNetworking.post(ApiEndPoint.ReadByID)
            .addBodyParameter("table", "komunitas")
            .addBodyParameter("id", ID)
            .addBodyParameter("idname", "owner")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("message")?.contains("ada")!!) {
                        val intent = Intent(requireContext(), ActivityAddCommunity::class.java)
                        intent.putExtra("ID", response.getString("id"))

                        Btn.setImageResource(android.R.drawable.ic_menu_edit)
                        Btn.setOnClickListener {
                            startActivity(intent)
                        }
                    } else {
                        Btn.setImageResource(android.R.drawable.ic_input_add)
                        Btn.setOnClickListener {
                            startActivity(Intent(requireContext(), ActivityAddCommunity::class.java))
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        loadCommunity()
    }
}