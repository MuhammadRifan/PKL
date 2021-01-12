package id.manlyman.petto.ui.facility.animalcare

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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
import id.manlyman.petto.ui.facility.Facility
import kotlinx.android.synthetic.main.fragment_animal_care.*
import kotlinx.android.synthetic.main.fragment_animal_care.view.*
import org.json.JSONObject


class AnimalCareFragment : Fragment(), OnItemClickListener {
    var arrayList = ArrayList<Facility>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_animal_care, container, false)
        view.acRecyclerView.setHasFixedSize(true)
        view.acRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val config = FConfig(requireContext())
        val uname = config.getCustom("uname", "")

        cekAC(uname, view.addAC)

        view.searchVW.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                view.searchVW.clearFocus()
                loadSearch(query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadSearch(newText.toString())
                return false
            }
        })

        return view
    }

    override fun onItemClicked(facility: Facility) {
        val intent = Intent(requireContext(), ClickedAnimalCare::class.java)
        intent.putExtra("ID", facility.id.toString())
        startActivity(intent)
    }

    private fun loadAnimalCare(){
        val loading = ProgressDialog(requireContext())
        loading.setMessage("Loading...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.Read)
            .addBodyParameter("table", "animalcare")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("result").toString() == "Data kosong") {
                        loading.dismiss()

                        acEmpty.visibility = View.VISIBLE
                        acRecyclerView.visibility = View.GONE
                    } else {
                        acEmpty.visibility = View.GONE
                        acRecyclerView.visibility = View.VISIBLE

                        arrayList.clear()
                        val jsonArray = response?.optJSONArray("result")

                        for (i in 0 until jsonArray?.length()!!) {
                            val jsonObject = jsonArray?.optJSONObject(i)
                            arrayList.add(Facility(jsonObject.getString("id"),
                                jsonObject.getString("nama"),
                                jsonObject.getString("city"),
                                jsonObject.getString("picture"),
                                jsonObject.getInt("hari_buka1"),
                                jsonObject.getInt("hari_buka2"),
                                jsonObject.getInt("hari_buka3"),
                                jsonObject.getInt("hari_buka4"),
                                jsonObject.getInt("hari_buka5"),
                                jsonObject.getInt("hari_buka6"),
                                jsonObject.getInt("hari_buka7"),
                                jsonObject.getString("jam_buka"),
                                jsonObject.getString("jam_tutup")))

                            if (jsonArray?.length() - 1 == i) {
                                loading.dismiss()
                                val adapter = AdapterAnimalCare(this@AnimalCareFragment, arrayList)
                                adapter.notifyDataSetChanged()
                                acRecyclerView.adapter = adapter
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

    private fun loadSearch(value: String){
        AndroidNetworking.post(ApiEndPoint.ReadSearch)
            .addBodyParameter("table", "animalcare")
            .addBodyParameter("column", "nama")
            .addBodyParameter("value", value)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("result").toString() == "Data kosong") {
                        acEmpty.visibility = View.VISIBLE
                        acRecyclerView.visibility = View.GONE
                    } else {
                        acEmpty.visibility = View.GONE
                        acRecyclerView.visibility = View.VISIBLE
                        arrayList.clear()
                        val jsonArray = response?.optJSONArray("result")

                        for (i in 0 until jsonArray?.length()!!) {
                            val jsonObject = jsonArray?.optJSONObject(i)
                            arrayList.add(Facility(jsonObject.getString("id"),
                                jsonObject.getString("nama"),
                                jsonObject.getString("city"),
                                jsonObject.getString("picture"),
                                jsonObject.getInt("hari_buka1"),
                                jsonObject.getInt("hari_buka2"),
                                jsonObject.getInt("hari_buka3"),
                                jsonObject.getInt("hari_buka4"),
                                jsonObject.getInt("hari_buka5"),
                                jsonObject.getInt("hari_buka6"),
                                jsonObject.getInt("hari_buka7"),
                                jsonObject.getString("jam_buka"),
                                jsonObject.getString("jam_tutup")))

                            if (jsonArray?.length() - 1 == i) {
                                val adapter = AdapterAnimalCare(this@AnimalCareFragment, arrayList)
                                adapter.notifyDataSetChanged()
                                acRecyclerView.adapter = adapter
                            }
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(requireContext(), "Connection Error", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun cekAC(ID: String, Btn: FloatingActionButton){
        val intent = Intent(requireContext(), ActivityAddAC::class.java)

        AndroidNetworking.post(ApiEndPoint.ReadByID)
            .addBodyParameter("table", "animalcare")
            .addBodyParameter("id", ID)
            .addBodyParameter("idname", "owner")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("message")?.contains("ada")!!) {
                        Btn.setImageResource(android.R.drawable.ic_menu_edit)
                        intent.putExtra("ID", response.getString("id"))
                    } else {
                        Btn.setImageResource(android.R.drawable.ic_input_add)
                    }

                    Btn.setOnClickListener {
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
        loadAnimalCare()
    }
}