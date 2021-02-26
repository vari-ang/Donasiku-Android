package com.ubaya.s160717023_donasiku

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.squareup.picasso.Picasso

class CustomDonasiAdapter(val thiscontext: Context,
                          val donasi: ArrayList<Donasi>)
    : ArrayAdapter<Donasi>(thiscontext, R.layout.listview_donations, donasi) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = thiscontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater
        val v = inflater.inflate(R.layout.listview_donations, parent, false)
        val txtName = v.findViewById<TextView>(R.id.textViewDonationName)
        val txtNumber = v.findViewById<TextView>(R.id.textViewDonationNumber)
        val txtUnit = v.findViewById<TextView>(R.id.textViewDonationUnit)

        txtName.text = donasi[position].nama
        txtNumber.text = donasi[position].jumlah.toString()
        txtUnit.text = donasi[position].satuan

        return v
    }

}