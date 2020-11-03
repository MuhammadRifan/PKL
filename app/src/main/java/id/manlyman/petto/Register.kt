package id.manlyman.petto

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
//import id.manlyman.petto.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
//        val binding: ActivityRegisterBinding = DataBindingUtil.

//        binding.btnPengguna.setOnClickListener {
//            Toast.makeText(this, "Pindah Activity", Toast.LENGTH_SHORT).show()
//        }
//
//        btnDokter.setOnClickListener {
//            Toast.makeText(this, "Activity Dokter", Toast.LENGTH_SHORT).show()
//        }

        btnRegisterPengguna.setOnClickListener {
            Register()
        }

        txtBawah.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }

    private fun Register() {
        val loading = ProgressDialog(this)
        loading.setMessage("Registering...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.Register)
                .addBodyParameter("nama", txtNama.text.toString())
                .addBodyParameter("email", txtRegistEmail.text.toString())
                .addBodyParameter("pass", txtRegistPass.text.toString())
                .addBodyParameter("alamat", txtAlamat.text.toString())
                .addBodyParameter("notelp", txtNoTelp.text.toString())
                .addBodyParameter("foto", txtFoto.text.toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        loading.dismiss()
                        Toast.makeText(applicationContext, response?.getString("message"), Toast.LENGTH_LONG).show()
                        if (response?.getString("message")?.contains("berhasil")!!) {
                            startActivity(Intent(applicationContext, Home::class.java))
                        }
                    }

                    override fun onError(anError: ANError?) {
                        loading.dismiss()
                        Log.d("OnError", anError?.errorDetail?.toString()!!)
                        Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                    }

                })
    }
}