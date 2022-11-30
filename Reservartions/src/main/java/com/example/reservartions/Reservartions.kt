package com.example.reservartions

import java.util.*

class Reservartions {

    var reservartions: MutableList<Reservation> = mutableListOf()
    var restaurants: MutableList<Restaurant> = mutableListOf();

    init{
        restaurants.add(Restaurant("1", "Kralj"))
        restaurants.add(Restaurant("2", "Spar Restavracija"))
        restaurants.add(Restaurant("3", "Zlati Lev"))
    }

    fun push(reservation: Reservation) {
        reservartions.add(reservation)
    }

    override fun toString(): String {
        var string = ""
        for(res in reservartions){
            string += reservartions +"\n"
        }
        return string;
    }

    fun checkRestaurant(rest: String): String{
        var id = 0;
        for(res in restaurants){
            if(res.name.toLowerCase() == rest.toLowerCase() ){
                return res.id
            }
            if(id< res.id.toInt()){
                id=res.id.toInt()
            }
        }
        id += 1
        restaurants.add(Restaurant(id.toString(),rest))
        return id.toString()
    }

    fun getReservation(uuid:String):Reservation?{
        for(res in reservartions){
            if(res.uuid== uuid)
                return res;
        }
        return null
    }

    fun updateReservation(uuid:String,reservation: Reservation): Boolean {
        for(res in reservartions){
            if(res.uuid == uuid) {
                res.title = reservation.title
                res.dateTime = reservation.dateTime
                res.restaurantId = reservation.restaurantId
                return true
            }
        }
        return false
    }

    fun getRestaurant(id: String):String{
        for(res in restaurants){
            if(id==res.id)
                return res.name;
        }
        return "null"


    }

}