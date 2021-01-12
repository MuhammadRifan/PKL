package id.manlyman.petto

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener
import com.androidnetworking.widget.ANImageView
import com.google.android.material.navigation.NavigationView
import com.mikhaellopez.circularimageview.CircularImageView
import id.manlyman.petto.ui.facility.myshop.ActivityMyShop
import id.manlyman.petto.ui.facility.paketproduk.ActivityFormPP
import id.manlyman.petto.ui.logout.LogoutFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_akun, R.id.nav_artikel, R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val config = FConfig(this)

        val menu: Menu = navView.menu
        menu.findItem(R.id.nav_artikel).isVisible = config.getCustom("level", "") == "1"

        menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            val builder = AlertDialog.Builder(this)
            builder.apply {
                setPositiveButton("Ok" ) { dialog, id ->
                    val Config = FConfig(applicationContext)
                    Config.delCustom()

                    Toast.makeText(applicationContext, "Logout berhasil", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, Login::class.java)
                    startActivity(intent)
                }
                setNegativeButton("Cancel" ) { dialog, id -> }
            }
            // Set other dialog properties
            builder.setMessage("Apa anda yakin ?")
                    .setTitle("Keluar akun")

            // Create the AlertDialog
            builder.create().show()

            true
        }

        val header: View = navView.getHeaderView(0)
        val userProfilePic: CircularImageView = header.findViewById(R.id.userProfilePic)
        val userName: TextView = header.findViewById(R.id.userName)
        val userEmail: TextView = header.findViewById(R.id.userEmail)

        picture(config.getCustom("picture", ""), userProfilePic)

        userName.text = config.getCustom("uname", "")
        userEmail.text = config.getCustom("email", "")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//         Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.myToko -> {
                val intent = Intent(this, ActivityMyShop::class.java)
                intent.putExtra("TB", "toko")
                startActivity(intent)
                true
            }
            R.id.myServis -> {
                val intent = Intent(this, ActivityMyShop::class.java)
                intent.putExtra("TB", "animalcare")
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun picture(url: String?, img: ImageView){
        AndroidNetworking.get(ApiEndPoint.Pictures + url)
            .setTag("Foto")
            .setPriority(Priority.MEDIUM)
            .setBitmapConfig(Bitmap.Config.ARGB_8888)
            .build()
            .getAsBitmap(object : BitmapRequestListener {
                override fun onResponse(bitmap: Bitmap) {
                    img.setImageBitmap(bitmap)
                }

                override fun onError(error: ANError) {
                    Log.d("OnError", error.errorDetail.toString())
                }
            })
    }

    override fun onResume() {
        super.onResume()
        val config = FConfig(this)
        val login = config.getCustom("is_login", "")

        if (login != "1") {
            Toast.makeText(this, "Mohon Login Terlebih Dahulu", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Login::class.java))
        }
    }
}