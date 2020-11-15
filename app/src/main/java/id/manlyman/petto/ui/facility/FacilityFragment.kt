package id.manlyman.petto.ui.facility

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.fragment_facilty.*

class FacilityFragment : Fragment() {

//    private lateinit var FacilitiesAdapter: FacilitiesAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_facilty, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewFacilities.adapter = FacilitiesAdapter(childFragmentManager)
    }
}