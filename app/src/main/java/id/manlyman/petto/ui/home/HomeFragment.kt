package id.manlyman.petto.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.manlyman.petto.R

class HomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navBottomView: BottomNavigationView = view.findViewById(R.id.nav_bot_view)
        val fragmentContainer = view.findViewById<View>(R.id.nav_host_bottom_fragment)
        val navBotController = Navigation.findNavController(fragmentContainer)
        navBottomView.setupWithNavController(navBotController)
    }
}