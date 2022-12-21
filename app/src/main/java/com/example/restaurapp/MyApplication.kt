package com.example.restaurapp;

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.reservartions.Reservartions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.io.FileUtils
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.*


const val MY_FILE_NAME = "mydata.json"
const val MY_SP_FILE_NAME = "myshared.data"  //pred razredom

public class MyApplication : Application(){
    lateinit var data: Reservartions
    lateinit var userUuid: String;

    private lateinit var gson: Gson
    private lateinit var file: File

    lateinit var sharedPref: SharedPreferences;

    lateinit var firestore: FirebaseFirestore;



    @SuppressLint("BinaryOperationInTimber")
    override fun onCreate() {
        super<Application>.onCreate()

        firestore = FirebaseFirestore.getInstance();

        data = Reservartions()
        var document = firestore.collection("reservations").document("reservations")
        document.get().addOnSuccessListener { documentSnapshot ->
            // Log.d("sometag",documentSnapshot.data.toString() )
            data = documentSnapshot.toObject<Reservartions>()!!
        }

        gson = GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
        file = File(filesDir, MY_FILE_NAME)
        sharedPref = getSharedPreferences(MY_SP_FILE_NAME, Context.MODE_PRIVATE);


        //initData()

        if (!sharedPref.contains("ID")) {
            saveID(UUID.randomUUID().toString().replace("-", ""));
        }
        if(!sharedPref.contains("NotID")){
            saveNotificationID("0")
        }

        userUuid = getID()!!;


        /*firestore.collection("reservations").document("reservations").set(data).addOnSuccessListener {
            Toast.makeText(
                this,
                "succes",
                Toast.LENGTH_LONG
            ).show()
        }.addOnFailureListener{
            Toast.makeText(
                this,
                "Fail",
                Toast.LENGTH_LONG
            ).show()
        }*/


    }

    /*@OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        appBackground++
        saveAppBackground()
    }*/

    fun loadData(adapter: ReservationsAdapter){
        var document = firestore.collection("reservations").document("reservations")
        document.get().addOnSuccessListener { documentSnapshot ->
            // Log.d("sometag",documentSnapshot.data.toString() )
            data = documentSnapshot.toObject<Reservartions>()!!
            adapter.notifyDataSetChanged()
        }
    }


    fun saveDatabase() {
        /*try {
            //for FileUtils import org.apache.commons.io.FileUtils
            //in gradle implementation 'org.apache.commons:commons-io:1.3.2'
            FileUtils.writeStringToFile(file, gson.toJson(data))
            Timber.d("Save to file.")
        } catch (e: IOException) {
            Timber.d("Can't save %s", file.path)
        }*/

        firestore.collection("reservations").document("reservations").set(data)
    }


    fun initData() {
        data = try { //www
            //Timber.d("My file data:${FileUtils.readFileToString(file)}")
            gson.fromJson(FileUtils.readFileToString(file), Reservartions::class.java)
        } catch (e: IOException) {
            Timber.d("No file init data.")
            Reservartions()
        }
    }

    fun saveID(id: String) {
        with(sharedPref.edit()) {
            putString("ID", id)
            apply()
        }
    }

    fun getID(): String? {
        return sharedPref.getString("ID", "DefaultNoData")
    }

    private fun saveNotificationID(id: String) {
        with(sharedPref.edit()) {
            putString("NotID", id)
            apply()
        }
    }

    private fun getNotificationID(): String? {
        return sharedPref.getString("NotID", "100")
    }


    fun getSavedNotId(): Int {
        var notId = getNotificationID()!!.toInt()
        notId += 1
        saveNotificationID(notId.toString())
        return notId
    }


   /*fun saveSortData() {
        if (getOrder() == "ASC") {
            data.sortByPriority()
        } else {
            data.sortByPriorityDescendig()
        }
        saveToFile();
    }


    fun deleteByID(id: String): Boolean {
        return data.deleteByID(id)
    }

    fun modifyTask(id: String, taskNew: Task): Boolean {
        return data.modifyTask(id, taskNew)
    }

    fun findById(id: String): Task? {
        return data.findByID(id)
    }*/



}