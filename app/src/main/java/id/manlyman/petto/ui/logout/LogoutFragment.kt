package id.manlyman.petto.ui.logout

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.manlyman.petto.FConfig
import id.manlyman.petto.HomeActivity
import id.manlyman.petto.Login
import id.manlyman.petto.R

class LogoutFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_logout, container, false)
        Logout()
        return root
    }

    private fun Logout(){
        val loading = ProgressDialog(requireContext())
        loading.setMessage("Logout ...")
        loading.show()

        val Config = FConfig(requireContext())
        Config.delCustom()

        loading.dismiss()

        Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_LONG).show()
        val intent = Intent(requireContext(), Login::class.java)
        startActivity(intent)
    }
}