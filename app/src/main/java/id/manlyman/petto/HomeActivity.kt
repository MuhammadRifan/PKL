package id.manlyman.petto

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.androidnetworking.widget.ANImageView
import com.google.android.material.navigation.NavigationView

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
                R.id.nav_home, R.id.nav_akun, R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val config = FConfig(this)

        val header: View = navView.getHeaderView(0)
        val userProfilePic: ANImageView = header.findViewById(R.id.userProfilePic)
        val userName: TextView = header.findViewById(R.id.userName)
        val userEmail: TextView = header.findViewById(R.id.userEmail)

        userProfilePic.setDefaultImageResId(R.mipmap.ic_launcher)
        userProfilePic.setErrorImageResId(R.mipmap.ic_launcher)
        userProfilePic.setImageUrl(ApiEndPoint.Pictures + config.getCustom("picture", ""))

        userName.text = config.getCustom("uname", "")
        userEmail.text = config.getCustom("email", "")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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