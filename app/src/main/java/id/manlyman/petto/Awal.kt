package id.manlyman.petto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        btnLoginPengguna.setOnClickListener {
            val intent = Intent(this, LogReg::class.java)
            intent.putExtra("Judul", "Login Pengguna")
            startActivity(intent)
        }

        btnRegisterPengguna.setOnClickListener {
            val intent = Intent(this, LogReg::class.java)
            intent.putExtra("Judul", "Register Pengguna")
            startActivity(intent)
        }

        btnLoginDokter.setOnClickListener {
            val intent = Intent(this, LogReg::class.java)
            intent.putExtra("Judul", "Login Dokter")
            startActivity(intent)
        }

        btnRegisterDokter.setOnClickListener {
            val intent = Intent(this, LogReg::class.java)
            intent.putExtra("Judul", "Register Dokter")
            startActivity(intent)
        }
    }
}