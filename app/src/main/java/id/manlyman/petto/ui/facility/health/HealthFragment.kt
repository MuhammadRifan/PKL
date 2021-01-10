package id.manlyman.petto.ui.facility.health

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.FConfig
import id.manlyman.petto.R
import id.manlyman.petto.ui.facility.Facility
import kotlinx.android.synthetic.main.fragment_health.*
import kotlinx.android.synthetic.main.fragment_health.view.*
import org.json.JSONObject

class HealthFragment : Fragment(), OnItemClickListener {

    var arrayList = ArrayList<Facility>()
    var piew: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_health, container, false)
        view.hRecyclerView.setHasFixedSize(true)
        view.hRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        piew = view
        return view
    }

    override fun onItemClicked(facility: Facility) {
        val intent = Intent(requireContext(), ClickedHealth::class.java)
        intent.putExtra("ID", facility.id)
        startActivity(intent)
    }

    private fun loadHealth(){
        val loading = ProgressDialog(requireContext())
        loading.setMessage("Loading...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.Read)
            .addBodyParameter("table", "faskes")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("result").toString() == "Data kosong") {
                        loading.dismiss()

                        hEmpty.visibility = View.VISIBLE
                        hRecyclerView.visibility = View.GONE
                    } else {
                        hEmpty.visibility = View.GONE
                        hRecyclerView.visibility = View.VISIBLE

                        arrayList.clear()
                        val jsonArray = response?.optJSONArray("result")

                        for (i in 0 until jsonArray!!.length()) {
                            val jsonObject = jsonArray.optJSONObject(i)
                            arrayList.add(Facility(jsonObject.getString("sip"),
                                jsonObject.getString("nama"),
                                jsonObject.getString("city"),
                                jsonObject.getString("picture"),
                                jsonObject.getInt("hari_buka1"),
                                jsonObject.getInt("hari_buka2"),
                                jsonObject.getInt("hari_buka3"),
                                jsonObject.getInt("hari_buka4"),
                                jsonObject.getInt("hari_buka5"),
                                jsonObject.getInt("hari_buka6"),
                                jsonObject.getInt("hari_buka7")))

                            if (jsonArray.length() - 1 == i) {
                                loading.dismiss()
                                val adapter = AdapterHealth(this@HealthFragment, arrayList)
                                adapter.notifyDataSetChanged()
                                hRecyclerView.adapter = adapter
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

    private fun cekArtc(ID: String){
        val intent = Intent(requireContext(), ActivityAddHealth::class.java)

        AndroidNetworking.post(ApiEndPoint.ReadByID)
            .addBodyParameter("table", "faskes")
            .addBodyParameter("id", ID)
            .addBodyParameter("idname", "owner")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("message")?.contains("ada")!!) {
                        piew?.addHealth?.setImageResource(android.R.drawable.ic_menu_edit)
                        intent.putExtra("ID", response.getString("sip"))
                        Toast.makeText(requireContext(), response.getString("sip").toString(), Toast.LENGTH_SHORT).show()
                    } else {
                        piew?.addHealth?.setImageResource(android.R.drawable.ic_input_add)
                    }

                    piew?.addHealth?.setOnClickListener {
                        startActivity(intent)
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        loadHealth()
        val config = FConfig(requireContext())
        val uname = config.getCustom("uname", "")

        if (config.getCustom("level", "") == "1") {
            piew?.addHealth?.visibility = View.VISIBLE

            if (config.getCustom("srtv", "") == "null" || config.getCustom("srtv", "") == "0") {
                piew?.addHealth?.setOnClickListener {
                    Toast.makeText(requireContext(), "Mohon isi srtv pada halaman profil", Toast.LENGTH_LONG).show()
                }
            } else {
                cekArtc(uname)
            }
        } else {
            piew?.addHealth?.visibility = View.GONE
        }
    }
}