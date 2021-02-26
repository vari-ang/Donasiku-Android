package com.ubaya.s160717023_donasiku.ui.main

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.ubaya.s160717023_donasiku.Kebutuhan
import com.ubaya.s160717023_donasiku.R
import com.ubaya.s160717023_donasiku.ui.add_needs.AddNeedsActivity
import com.ubaya.s160717023_donasiku.ui.donation.DonationActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONArray
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    val needs = ArrayList<Kebutuhan>()

    override fun onBackPressed() {
        // Disable back button
        // super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
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

        // Get all needs (kebutuhan)
        val volley = Volley.newRequestQueue(this)

        var url = "http://ubaya.prototipe.net/s160717023/donasiku/get_all_needs.php"
        var stringRequest = StringRequest(Request.Method.POST, url,
            Response.Listener<String> {
                var arr = JSONArray(it)
                for(i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    val need = Kebutuhan(
                        obj.getInt("id"),
                        obj.getString("kebutuhan"),
                        obj.getString("deskripsi"),
                        obj.getString("satuan"),
                        obj.getInt("nilai"),
                        obj.getInt("total_donasi")
                    )
                    needs.add(need)
                }

                // Pass the data to fragment
                var bundle = Bundle()
                bundle.putSerializable("needs", needs as Serializable)
                navController.navigate(R.id.nav_main, bundle)
            },
            Response.ErrorListener {
                toast(it.toString())
            })

        volley.add(stringRequest)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.nav_main) {
//                startActivity<MainActivity>()
            }
            else if(destination.id == R.id.nav_donation) {
                startActivity<DonationActivity>()
            }
            else if(destination.id == R.id.nav_add_needs) {
                startActivity<AddNeedsActivity>()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Hides The Three Dots Settings
//        menuInflater.inflate(R.menu.main, menu)
        return true
    }
//
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
