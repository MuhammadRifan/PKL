package id.manlyman.petto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import kotlinx.android.synthetic.main.activity_areyou.*
import org.json.JSONObject

class Areyou : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_areyou)
        supportActionBar?.hide()

        val emailRegis = intent.getStringExtra("email")

        btnYes.setOnClickListener {
            AndroidNetworking.post(ApiEndPoint.Pengguna2Dokter)
                .addBodyParameter("email", emailRegis)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        if (response?.getString("message")?.contains("berhasil")!!) {
                            startActivity(Intent(applicationContext, Login::class.java))
                        } else {
                            Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("OnError", anError?.errorDetail?.toString()!!)
                        Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                    }

                })
            startActivity(Intent(this, Login::class.java))
        }

        btnNo.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}