package com.ubaya.s160717023_donasiku

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubaya.s160717023_donasiku.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val volley = Volley.newRequestQueue(this)

        buttonSIgnIn.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if(email.isEmpty() || password.isEmpty()) {
                alert("", "Tolong isi semua data yang diminta") {
                    yesButton {}
                }.show()
            }
            else {
                var url = "http://ubaya.prototipe.net/s160717023/donasiku/login.php"
                var stringRequest = object : StringRequest(Request.Method.POST, url,
                    Response.Listener<String> { response ->
                        val obj = JSONObject(response)
                        if (obj["status"] == "success") {
                            toast("Berhasil Login")

                            // Set user's email to userEmail global variable
                            (this.application as MyApplication).userEmail = email

                            startActivity<MainActivity>()
                        } else if (obj["status"] == "error") {
                            alert("", "${obj["message"]}") {
                                yesButton {}
                            }.show()
                        }
                    },
                    Response.ErrorListener {
                        alert("Error", it.toString()) {
                            yesButton {}
                        }.show()
                    }) {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params.put("email", email)
                        params.put("password", password)

                        return params
                    }
                }

                volley.add(stringRequest)
            }
        }
    }
}
