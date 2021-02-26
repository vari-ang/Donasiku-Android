package com.ubaya.s160717023_donasiku.ui.add_needs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubaya.s160717023_donasiku.Donasi
import com.ubaya.s160717023_donasiku.Kebutuhan
import com.ubaya.s160717023_donasiku.R
import com.ubaya.s160717023_donasiku.ui.donation.DonationActivity
import kotlinx.android.synthetic.main.fragment_add_needs.*
import org.jetbrains.anko.sdk27.coroutines.onItemSelectedListener
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import org.json.JSONArray
import java.io.Serializable
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt

class AddNeedsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =  inflater?.inflate(R.layout.fragment_add_needs, container, false)

        var kebutuhans = ArrayList<Kebutuhan>()

        // BUNDLE DATA
        var donasis = arguments?.getSerializable("donasis") as? ArrayList<Donasi>

        // Selected Need (Donasi)
        var idDonasi = -1
        var namaDonasi = ""
        var satuanDonasi = ""

        var nilaiDonasi = 0
        var totalDonasi = 0

        // Get all needs (kebutuhan)
        val volley = Volley.newRequestQueue(getActivity()!!.getBaseContext())

        var url = "http://ubaya.prototipe.net/s160717023/donasiku/get_all_needs.php"
        var stringRequest = StringRequest(
            Request.Method.POST, url,
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
                    kebutuhans.add(need)
                }

                if(!kebutuhans.isNullOrEmpty()) {
                    val kebutuhansArr = arrayOfNulls<String>((kebutuhans.size + 1))
                    kebutuhansArr[0] = "-- Pilih Kebutuhan --"

                    for(i in 0 until kebutuhans.size) {
                        kebutuhansArr[i+1] = kebutuhans.get(i).kebutuhan
                    }

                    // Create an ArrayAdapter from the string-array resource with the default look
                    val adapter = ArrayAdapter(getActivity()!!.getBaseContext(), android.R.layout.simple_spinner_item, kebutuhansArr)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    var spinnerNeeds = view.findViewById<Spinner>(R.id.spinnerNeeds)
                    spinnerNeeds.adapter = adapter

                    spinnerNeeds.onItemSelectedListener {
                        onItemSelected { adapter, view, index, id ->
                            if(index != 0) {
                                totalDonasi = kebutuhans[index-1].totalDonasi
                                nilaiDonasi = kebutuhans[index-1].nilai
                                val persentaseDonasi:Double = (totalDonasi * 100.0) / nilaiDonasi
                                editTextNumberOfNeeds.setText("${totalDonasi} / ${nilaiDonasi} (${persentaseDonasi}%)")

                                idDonasi = kebutuhans[index-1].id
                                namaDonasi = kebutuhans[index-1].kebutuhan
                                satuanDonasi = kebutuhans[index-1].satuan
                            }
                            else {
                                idDonasi = -1
                                namaDonasi = ""
                                satuanDonasi = ""
                            }
                        }
                    }
                }
            },
            Response.ErrorListener {
                toast(it.toString())
            })

        volley.add(stringRequest)

        val buttonSubmit:Button = view.findViewById(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            if(idDonasi != -1) {
                var jumlahDonasi = 0
                var isNumeric = true
                if(!editTextNumberOfDonations.text.isNullOrBlank()) {
                    try {
                        jumlahDonasi = parseInt(editTextNumberOfDonations.text.toString())
                    } catch (e: NumberFormatException) {
                        isNumeric = false
                    }

                    if(isNumeric) {
                        if (jumlahDonasi > 0) {
                            if(jumlahDonasi + totalDonasi <= nilaiDonasi) {
                                var donasi = Donasi(idDonasi, namaDonasi, jumlahDonasi, satuanDonasi)
                                donasis?.add(donasi)

                                startActivity<DonationActivity>(
                                    "donasis" to donasis as Serializable
                                )
                            }
                            else {
                                alert(
                                    "Jumlah Donasi Anda Melebihi Yang Dibutuhkan (Jumlah tersisa yang dibutuhkan: ${nilaiDonasi - totalDonasi})",
                                    "Gagal Mendonasikan Kebutuhan"
                                ) {
                                    yesButton {}
                                }.show()
                            }
                        }
                        else {
                            alert(
                                "Mohon Isikan Jumlah Donasi Dan Setidaknya Berjumlah 1 Item",
                                "Gagal Mendonasikan Kebutuhan"
                            ) {
                                yesButton {}
                            }.show()
                        }
                    }
                    else {
                        alert("Jumlah Donasi Harus Berupa Angka Saja", "Gagal Mendonasikan Kebutuhan") {
                            yesButton {}
                        }.show()
                    }
                }
                else {
                    alert("Mohon Isikan Jumlah Donasi", "Gagal Mendonasikan Kebutuhan") {
                        yesButton {}
                    }.show()
                }
            }
            else {
                alert("Mohon Pilih Kebutuhan Terlebih Dahulu Sebelum Melakukan Donasi", "Gagal Mendonasikan Kebutuhan") {
                    yesButton {}
                }.show()
            }
        }

        return view
    }
}
