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

        val uname = intent.getStringExtra("uname")

        btnYes.setOnClickListener {
            AndroidNetworking.post(ApiEndPoint.Pengguna2Dokter)
                .addBodyParameter("uname", uname)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        if (!response?.getString("message")?.contains("berhasil")!!) {
                            Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_LONG).show()
                        }
                        startActivity(Intent(applicationContext, Login::class.java))
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("OnError", anError?.errorDetail?.toString()!!)
                        Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                    }
                })
        }

        btnNo.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }
}