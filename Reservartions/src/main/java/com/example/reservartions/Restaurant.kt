package com.example.reservartions

class Restaurant(
    var id: String = "",
    var name: String = "",
    var lat : Double = 0.0,
    var lon : Double = 0.0
) {
//will add data when the need arrives

    override fun toString(): String {
        return name
    }

}