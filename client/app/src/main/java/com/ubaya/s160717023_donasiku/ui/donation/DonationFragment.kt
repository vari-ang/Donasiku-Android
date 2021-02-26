package com.ubaya.s160717023_donasiku.ui.donation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.ubaya.s160717023_donasiku.CustomDonasiAdapter
import com.ubaya.s160717023_donasiku.Donasi
import com.ubaya.s160717023_donasiku.MyApplication
import com.ubaya.s160717023_donasiku.R
import com.ubaya.s160717023_donasiku.ui.add_needs.AddNeedsActivity
import com.ubaya.s160717023_donasiku.ui.main.MainActivity
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.yesButton
import org.json.JSONObject
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DonationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater?.inflate(R.layout.fragment_donation, container, false)

        // BUNDLE DATA
        var donasis = arguments?.getSerializable("donasis") as? ArrayList<Donasi>

        val fab: FloatingActionButton = view.findViewById(R.id.fab_donation)
        fab.setOnClickListener { view ->
            startActivity<AddNeedsActivity>(
                "donasis" to donasis as Serializable
            )
        }

        val arrayAdapter = CustomDonasiAdapter(this.getActivity()!!.getApplicationContext(), donasis ?: ArrayList<Donasi>())
        var listDonation:ListView = view.findViewById(R.id.listDonation)
        listDonation.adapter = arrayAdapter

        var textViewNumOfDonations:TextView = view.findViewById(R.id.textViewNumOfDonations)
        var textViewNoNeeds:TextView = view.findViewById(R.id.textViewNoNeeds)
        var containerNeeds:LinearLayout = view.findViewById(R.id.containerNeeds)

        if(donasis?.size ?: 0 == 0) {
            textViewNoNeeds.isVisible = true
            containerNeeds.isVisible = false
        }
        else {
            // Show donations from AddNeedsActivity
            textViewNumOfDonations.setText("${donasis?.size ?: 0} item pada daftar donasi Anda")
            textViewNoNeeds.isVisible = false
            containerNeeds.isVisible = true

            // Get user's email to userEmail global variable
            var userEmail: String? = (this.getActivity()!!.application as MyApplication).userEmail ?: ""

            var checkBoxTermsCondiions:CheckBox = containerNeeds.findViewById(R.id.checkBoxTermsConditions)
            var buttonConfirmDonation:Button = containerNeeds.findViewById(R.id.buttonConfirmDonation)

            buttonConfirmDonation.setOnClickListener {
                if(checkBoxTermsCondiions.isChecked) {
                    // Get current date
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val currentDate = sdf.format(Date())

                    // Add data to database
                    val volley = Volley.newRequestQueue(this.getActivity()!!.getApplicationContext())
                    var url = "http://ubaya.prototipe.net/s160717023/donasiku/add_donations.php"
                    var stringRequest = object : StringRequest(
                        Request.Method.POST, url,
                        Response.Listener<String> { response ->
                            val obj = JSONObject(response)
                            if (obj["status"] == "success") {
                                alert("Terima kasih atas donasi Anda. Sebentar lagi kami akan menghubungi Anda di nomer HP ${obj["message"]} untuk konfirmasi") {
                                    yesButton {
                                        donasis?.clear()

                                        textViewNoNeeds.isVisible = true
                                        containerNeeds.isVisible = false

                                        // Move the user to MainActivity
                                        startActivity<MainActivity>()
                                    }
                                }.show()
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
                            params.put("email", userEmail.toString())
                            params.put("date", currentDate)

                            // Donation ArrayList
                            val donasisJson = Gson().toJson(donasis)
                            params.put("donations", donasisJson)

                            return params
                        }
                    }

                    volley.add(stringRequest)
                }
                else {
                    alert("Mohon menyetujui syarat dan ketentuan yang berlaku terlebih dahulu", "Gagal Menambahkan Donasi") {
                        yesButton {}
                    }.show()
                }
            }
        }

        return view
    }
}
