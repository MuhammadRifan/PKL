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
import id.manlyman.petto.R
import id.manlyman.petto.ui.facility.Facility
import kotlinx.android.synthetic.main.fragment_health.*
import kotlinx.android.synthetic.main.fragment_health.view.*
import org.json.JSONObject

class HealthFragment : Fragment(), OnItemClickListener {

    var arrayList = ArrayList<Facility>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_health, container, false)
        view.hRecyclerView.setHasFixedSize(true)
        view.hRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        return view
    }

    override fun onItemClicked(facility: Facility) {
        val intent = Intent(requireContext(), ClickedHealth::class.java)
        intent.putExtra("ID", facility.id.toString())
        startActivity(intent)

//        Toast.makeText(requireContext(), "ID : ${facility.id}", Toast.LENGTH_LONG)
//                .show()
//        Log.i("USER_", facility.id.toString())
    }

    private fun loadHealth(){
        val loading = ProgressDialog(requireContext())
        loading.setMessage("Loading...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.ReadFacility)
                .addBodyParameter("table", "fasilitas")
                .addBodyParameter("facility", "Health")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        arrayList.clear()
                        val jsonArray = response?.optJSONArray("result")

                        if (jsonArray?.length() == 0) {
                            loading.dismiss()
                            Toast.makeText(requireContext(), "Data Kosong", Toast.LENGTH_LONG).show()
                        }

                        for (i in 0 until jsonArray?.length()!!) {
                            val jsonObject = jsonArray?.optJSONObject(i)
                            arrayList.add(Facility(jsonObject.getInt("id_fasilitas"),
                                    jsonObject.getString("nama_fasilitas"),
                                    jsonObject.getString("alamat_fasilitas"),
                                    jsonObject.getString("deskripsi_fasilitas")))

                            if (jsonArray?.length() - 1 == i) {
                                loading.dismiss()
                                val adapter = AdapterHealth(this@HealthFragment, arrayList)
                                adapter.notifyDataSetChanged()
                                hRecyclerView.adapter = adapter
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

    override fun onStart() {
        super.onStart()
        loadHealth()
    }
}