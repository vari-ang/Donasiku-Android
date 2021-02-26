package com.ubaya.s160717023_donasiku

import java.io.Serializable

class Donasi(id:Int, nama:String, jumlah: Int, satuan:String) : Serializable {
    var id = id
    var nama = nama
    var jumlah = jumlah
    var satuan = satuan
}