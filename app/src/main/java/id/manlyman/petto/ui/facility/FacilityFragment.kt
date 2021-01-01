package id.manlyman.petto.ui.facility

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.fragment_facilty.*

class FacilityFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_facilty, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewFacilities.adapter = FacilitiesAdapter(childFragmentManager)
    }
}