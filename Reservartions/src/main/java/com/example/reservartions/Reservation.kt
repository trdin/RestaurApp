package com.example.reservartions

import java.util.*

class Reservation(
    var dateTime: Date,
    var title : String,
    var restaurantId : String
) {
    var uuid = UUID.randomUUID().toString().replace("-", "");


    override fun toString(): String {
        return "Reservation: $title; date: $dateTime; $restaurantId"
    }
}