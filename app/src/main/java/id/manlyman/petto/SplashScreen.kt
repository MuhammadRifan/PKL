package id.manlyman.petto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        val Config = FConfig(this)
        val pesan: Toast = Toast.makeText(this, "", Toast.LENGTH_LONG)
        val splash: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    val login = Config.getCustom("is_login", "")

                    if (login.equals("1")) {
                        pesan.setText("Login Berhasil")
                        pesan.show()
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                    } else {
                        startActivity(Intent(applicationContext, Login::class.java))
                    }
                    finish()
                }
            }
        }
        splash.start()
    }
}