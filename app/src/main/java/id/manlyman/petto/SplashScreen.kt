package id.manlyman.petto

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        val config = FConfig(this)
//        val pesan: Toast = Toast.makeText(this, "", Toast.LENGTH_LONG)
        val splash: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    val login = config.getCustom("is_login", "")

                    if (login == "1") {
//                        pesan.setText("Login Berhasil")
//                        pesan.show()
                        login(config.getCustom("email", ""), config.getCustom("pass", ""))
                    } else {
                        startActivity(Intent(applicationContext, Login::class.java))
                    }
                    finish()
                }
            }
        }
        splash.start()
    }

    private fun login(email: String, pass: String){
        val Config = FConfig(applicationContext)

        AndroidNetworking.post(ApiEndPoint.Login)
                .addBodyParameter("email", email)
                .addBodyParameter("pass", pass)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        if (response?.getString("message")?.contains("berhasil")!!) {
                            Config.setCustom("is_login", "1")
                            Config.setCustom("uname", response.getString("username").toString())
                            Config.setCustom("pass", response.getString("pass").toString())
                            Config.setCustom("email", response.getString("email").toString())
                            Config.setCustom("phone", response.getString("phone").toString())
                            Config.setCustom("picture", response.getString("picture").toString())
                            Config.setCustom("srtv", response.getString("srtv").toString())
                            Config.setCustom("level", response.getString("level").toString())

                            Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, HomeActivity::class.java))
                        } else {
                            Toast.makeText(applicationContext, "Login gagal silahkan login kembali", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, Login::class.java))
                            Config.delCustom()
                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("OnError", anError?.errorDetail?.toString()!!)
                        Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                    }

                })
    }
}