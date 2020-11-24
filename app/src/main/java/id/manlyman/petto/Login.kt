package id.manlyman.petto

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        btnLogin.setOnClickListener {
            Login()
        }

        txtRegister.setOnClickListener{
            startActivity(Intent(this, Register::class.java))
        }

        luPass.setOnClickListener {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show()
        }
    }

    private fun Login(){
        val loading = ProgressDialog(this)
        loading.setMessage("Logging in...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.Login)
            .addBodyParameter("email", txtEmail.text.toString())
            .addBodyParameter("pass", txtPass.text.toString())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()

                    Toast.makeText(applicationContext, response?.getString("message"), Toast.LENGTH_LONG).show()

                    if (response?.getString("message")?.contains("berhasil")!!) {
                        val Config = FConfig(applicationContext)
                        Config.setCustom("is_login", "1")
                        Config.setCustom("id", response?.getString("id_pengguna").toString())
                        Config.setCustom("nama", response?.getString("nama_pengguna").toString())
                        Config.setCustom("email", response?.getString("email_pengguna").toString())
                        Config.setCustom("foto", response?.getString("foto").toString())

                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                }

            })
    }

    override fun onResume() {
        super.onResume()
        val Config = FConfig(this)
        val login = Config.getCustom("is_login", "")

        if (login.equals("1")) {
            Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}
