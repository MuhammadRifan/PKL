package id.manlyman.petto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_log_reg.*

class LogReg : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_reg)

        val message = intent.getStringExtra("Judul")

        titleAct.apply {
            text = message
        }

    }
}