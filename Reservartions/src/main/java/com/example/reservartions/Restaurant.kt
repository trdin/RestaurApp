package com.example.reservartions

class Restaurant(
    var id: String,
    var name: String,
    var lat : Double,
    var lon : Double
) {
//will add data when the need arrives

    override fun toString(): String {
        return name
    }

}