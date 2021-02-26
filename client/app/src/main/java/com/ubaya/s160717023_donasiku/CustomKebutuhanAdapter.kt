package com.ubaya.s160717023_donasiku

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class CustomKebutuhanAdapter(val thiscontext: Context,
                             val kebutuhan: ArrayList<Kebutuhan>)
    : ArrayAdapter<Kebutuhan>(thiscontext, R.layout.listview_card_needs, kebutuhan) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = thiscontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater
        val v = inflater.inflate(R.layout.listview_card_needs, parent, false)
        val logo = v.findViewById<ImageView>(R.id.imageViewCardNeed)
        val txtName = v.findViewById<TextView>(R.id.textViewCardName)
        val txtDescription = v.findViewById<TextView>(R.id.textViewCardDescription)
        val txtTotalNeeds = v.findViewById<TextView>(R.id.textViewTotalNeeds)
        val progressBarNeed = v.findViewById<ProgressBar>(R.id.progressBarNeed)
        val txtDonated = v.findViewById<TextView>(R.id.textViewDonated)

        // Set image (logo) to imageViewCard
        Picasso.get()
            .load("http://ubaya.prototipe.net/s160717023/donasiku/kebutuhan_images/${kebutuhan[position].id}.jpeg")
            .into(logo)

        txtName.text = kebutuhan[position].kebutuhan
        txtDescription.text = kebutuhan[position].deskripsi
        txtTotalNeeds.text = "Kebutuhan: ${kebutuhan[position].nilai} ${kebutuhan[position].satuan}"
        progressBarNeed.max = kebutuhan[position].nilai
        progressBarNeed.progress = kebutuhan[position].totalDonasi
        txtDonated.text = "${kebutuhan[position].totalDonasi}/${kebutuhan[position].nilai}"

        return v
    }
}