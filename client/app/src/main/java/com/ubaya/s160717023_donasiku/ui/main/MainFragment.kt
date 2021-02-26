package com.ubaya.s160717023_donasiku.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.ListFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ubaya.s160717023_donasiku.CustomKebutuhanAdapter
import com.ubaya.s160717023_donasiku.Kebutuhan
import com.ubaya.s160717023_donasiku.R
import com.ubaya.s160717023_donasiku.ui.donation.DonationActivity
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.support.v4.startActivity

class MainFragment() : ListFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater?.inflate(R.layout.fragment_main, container, false)

        val fab: FloatingActionButton = view.findViewById(R.id.fab_donate)
        fab.setOnClickListener { view ->
            startActivity<DonationActivity>()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var kebutuhans = arguments?.getSerializable("needs") as? ArrayList<Kebutuhan>

        if(!kebutuhans.isNullOrEmpty()) {
            val arrayAdapter = CustomKebutuhanAdapter(this.getActivity()!!.getApplicationContext(), kebutuhans.toCollection(ArrayList()))
            list?.adapter = arrayAdapter
        }
    }
}
