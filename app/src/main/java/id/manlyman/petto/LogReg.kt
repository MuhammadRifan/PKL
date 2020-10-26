package id.manlyman.petto

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import kotlinx.android.synthetic.main.activity_log_reg.*
import org.json.JSONObject

class LogReg : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_reg)

        val message = intent.getStringExtra("Judul")

        titleAct.apply {
            text = message
        }

        if (message.toString().contains("Login") ) {
            btnLogReg.apply {
                text = "Login"
            }
            btnLogReg.setOnClickListener {
                Login()
            }
        } else {
            btnLogReg.apply {
                text = "Register"
            }
            if (message.toString().contains("Dokter")) {
                txtNIP.visibility = View.VISIBLE
                btnLogReg.setOnClickListener {
                    RegistDokter()
                }
            } else {
                btnLogReg.setOnClickListener {
                    RegistPengguna()
                }
            }
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

    private fun RegistDokter() {
        val loading = ProgressDialog(this)
        loading.setMessage("Registering...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.RegistDokter)
                .addBodyParameter("email", txtEmail.text.toString())
                .addBodyParameter("pass", txtPass.text.toString())
                .addBodyParameter("nip", txtNIP.text.toString())
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

    private fun RegistPengguna() {
        val loading = ProgressDialog(this)
        loading.setMessage("Registering...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.RegistPengguna)
                .addBodyParameter("email", txtEmail.text.toString())
                .addBodyParameter("pass", txtPass.text.toString())
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
