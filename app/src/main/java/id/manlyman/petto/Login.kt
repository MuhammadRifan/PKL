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
            var error = 0

            if (txtEmail.text.toString().isEmpty()) {
                txtEmail.error = "Mohon masukan email"
                error++
            } else if (!txtEmail.text.toString().trim().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+.com+"))) {
                txtEmail.error = "Mohon masukkan email dengan benar"
                error++
            }

            if (txtPass.text.toString().length < 5) {
                txtPass.error = "Mohon masukan password dengan benar"
                error++
            }

            if (error == 0) {
                login(txtEmail.text.toString(), txtPass.text.toString())
            } else {
                Toast.makeText(this, "Form tidak lengkap", Toast.LENGTH_SHORT).show()
            }

        }

        txtRegister.setOnClickListener{
            startActivity(Intent(this, Register::class.java))
        }
    }

    private fun login(email: String, pass: String){
        val loading = ProgressDialog(this)
        loading.setMessage("Logging in...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.Login)
            .addBodyParameter("email", email)
            .addBodyParameter("pass", pass)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()

                    Toast.makeText(applicationContext, response?.getString("message"), Toast.LENGTH_SHORT).show()

                    if (response?.getString("message")?.contains("berhasil")!!) {
                        val Config = FConfig(applicationContext)
                        Config.setCustom("is_login", "1")
                        Config.setCustom("uname", response.getString("username").toString())
                        Config.setCustom("pass", response.getString("pass").toString())
                        Config.setCustom("email", response.getString("email").toString())
                        Config.setCustom("phone", response.getString("phone").toString())
                        Config.setCustom("picture", response.getString("picture").toString())
                        Config.setCustom("srtv", response.getString("srtv").toString())
                        Config.setCustom("level", response.getString("level").toString())

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
        val config = FConfig(this)
        val isLogin = config.getCustom("is_login", "")

        if (isLogin == "1") {
            login(config.getCustom("email", ""), config.getCustom("pass", ""))
        }
    }
}
