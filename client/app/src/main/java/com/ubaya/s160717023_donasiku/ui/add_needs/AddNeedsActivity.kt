package com.ubaya.s160717023_donasiku.ui.add_needs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.ubaya.s160717023_donasiku.Donasi
import com.ubaya.s160717023_donasiku.R
import com.ubaya.s160717023_donasiku.ui.donation.DonationActivity
import com.ubaya.s160717023_donasiku.ui.main.MainActivity
import org.jetbrains.anko.startActivity
import java.io.Serializable

class AddNeedsActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // INTENT DATA
        val donasis = this.intent?.getSerializableExtra("donasis") as? ArrayList<Donasi>

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = this.findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_main,
                R.id.nav_donation,
                R.id.nav_add_needs
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Pass the data to fragment
        var bundle = Bundle()
        bundle.putSerializable("donasis", donasis?: ArrayList<Donasi>() as Serializable)
        navController.navigate(R.id.nav_add_needs, bundle)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.nav_main) {
                startActivity<MainActivity>()
            }
            else if(destination.id == R.id.nav_donation) {
                startActivity<DonationActivity>()
            }
            else if(destination.id == R.id.nav_add_needs) {
//                startActivity<AddNeedsActivity>()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Hides The Three Dots Settings
//        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
