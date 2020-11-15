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

//        val message = intent.getStringExtra("Judul")
//
//        titleAct.apply {
//            text = message
//        }

        btnLogin.setOnClickListener {
            Login()
        }

        Bawah.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
        val Config = FConfig(this)
        val login = Config.getCustom("is_login", "")

        if (login.equals("1")) {
            Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
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
                    val Config = FConfig(applicationContext)
                    Config.setCustom("is_login", "1")

                    if (response?.getString("message")?.contains("berhasil")!!) {
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

}
