package id.manlyman.petto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import id.manlyman.petto.databinding.ActivityAwalBinding
import kotlinx.android.synthetic.main.activity_awal.*

class Awal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAwalBinding = DataBindingUtil.setContentView(this, R.layout.activity_awal)

//        binding.btnPengguna.setOnClickListener {
//            Toast.makeText(this, "Pindah Activity", Toast.LENGTH_SHORT).show()
//        }
//
//        btnDokter.setOnClickListener {
//            Toast.makeText(this, "Activity Dokter", Toast.LENGTH_SHORT).show()
//        }

        btnPengguna.setOnClickListener {
            val intent = Intent(this, LogReg::class.java)
            intent.putExtra("Judul", "Pengguna")
            startActivity(intent)
        }

        btnDokter.setOnClickListener {
            val intent = Intent(this, LogReg::class.java)
            intent.putExtra("Judul", "Dokter")
            startActivity(intent)
        }
    }
}