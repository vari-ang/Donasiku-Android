package com.ubaya.s160717023_donasiku

import java.io.Serializable

class Kebutuhan(id: Int, kebutuhan: String, deskripsi: String, satuan: String,  nilai: Int, totalDonasi: Int) : Serializable {
    var id = id
    var kebutuhan = kebutuhan
    var deskripsi = deskripsi
    var satuan = satuan
    var nilai = nilai
    var totalDonasi = totalDonasi
}