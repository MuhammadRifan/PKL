package id.manlyman.petto.ui.facility.shop

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
import kotlinx.android.synthetic.main.fragment_shop.*
import kotlinx.android.synthetic.main.fragment_shop.view.*
import org.json.JSONObject

class ShopFragment : Fragment(), OnItemClickListener {

    var arrayList = ArrayList<Facility>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)
        view.sRecyclerView.setHasFixedSize(true)
        view.sRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        view.addShop.setOnClickListener {
            startActivity(Intent(requireContext(), ActivityAddShop::class.java))
        }

        return view
    }

    override fun onItemClicked(facility: Facility) {
        val intent = Intent(requireContext(), ClickedShop::class.java)
        intent.putExtra("ID", facility.id.toString())
        startActivity(intent)

//        Toast.makeText(requireContext(), "ID : ${facility.id}", Toast.LENGTH_LONG)
//                .show()
//        Log.i("USER_", facility.id.toString())
    }

    private fun loadShop(){
        val loading = ProgressDialog(requireContext())
        loading.setMessage("Loading...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.Read)
                .addBodyParameter("table", "toko")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        if (response?.getString("result").toString() == "Data kosong") {
                            loading.dismiss()

                            sEmpty.visibility = View.VISIBLE
                            sRecyclerView.visibility = View.GONE
                        } else {
                            sEmpty.visibility = View.GONE
                            sRecyclerView.visibility = View.VISIBLE

                            arrayList.clear()
                            val jsonArray = response?.optJSONArray("result")

                            for (i in 0 until jsonArray?.length()!!) {
                                val jsonObject = jsonArray.optJSONObject(i)
                                arrayList.add(Facility(jsonObject.getInt("id"),
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
                                    val adapter = AdapterShop(this@ShopFragment, arrayList)
                                    adapter.notifyDataSetChanged()
                                    sRecyclerView.adapter = adapter
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

    override fun onStart() {
        super.onStart()
        loadShop()
    }
}