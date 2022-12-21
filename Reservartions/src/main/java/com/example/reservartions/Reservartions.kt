package com.example.reservartions

import java.util.*

class Reservartions {

    var reservartions: MutableList<Reservation> = mutableListOf()
    var restaurants: MutableList<Restaurant> = mutableListOf();

    init{
        restaurants.add(Restaurant("0", "Pri Florjanu",46.5600997,15.6484543  ))
        restaurants.add(Restaurant("1", "Okrepčevalnica Baščaršija", 46.5583493, 15.6448828  ))
        restaurants.add(Restaurant("2", "Kitajska restavracija Peking", 46.5609763, 15.645319 ))
        restaurants.add(Restaurant("3", "Vivaldi", 46.5373025, 15.6240099 ))
        restaurants.add(Restaurant("4", "Cesarska hiša", 46.5599364, 15.62102636801524 ))
        restaurants.add(Restaurant("5", "Ngon", 46.5576522, 15.6462879 ))
        restaurants.add(Restaurant("6", "La Cantina", 46.55935, 15.6481455 ))
        restaurants.add(Restaurant("7", "Cantante Tabor", 46.54858965, 15.643102645467971))
        restaurants.add(Restaurant("8", "Gostilna Koblarjev zaliv", 46.56544985, 15.619146440114575 ))
        restaurants.add(Restaurant("9", "mlada lipa", 46.5405598, 15.6073827))
        restaurants.add(Restaurant("10", "Takos", 46.5537993, 15.652079 ))
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

    /*fun checkRestaurant(rest: String): String{
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
    }*/

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

    fun deleteReservation(uuid:String): Boolean {
        for(res in reservartions){
            if(res.uuid == uuid) {
                reservartions.remove(res)
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

    fun rastaurantNames(): Array<String>{
        var array: Array<String> = arrayOf();
        for(rest in restaurants){
            array += rest.name
        }
        return array
    }

    fun checkReservationResta(id:String): Boolean{
        for(res in reservartions){
            if(res.restaurantId == id){
                return true
            }
        }
        return false
    }

}