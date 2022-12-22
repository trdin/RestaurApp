package com.example.restaurapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.reservartions.Reservation
import com.example.restaurapp.databinding.ActivityMainBinding
import com.example.restaurapp.notification.MyNotificationReceiver
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber
import java.util.*

// TODO fix bundle sending and reservation
// TODO fix ntoification sending id in shared prefferences
// TODO fix the notification options


class MainActivity : AppCompatActivity() {

    lateinit var app: MyApplication
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration


    var bundle: Bundle = Bundle();

    companion object {
        val CHANNEL_ID = "com.example.restaurapp.notification" //my channel id
        val TIME_ID = "TIME_ID"
        val MY_ACTION_FILTER = "com.example.restaurapp.open"
        val KEY = "com.example.restaurapp.update"
        val UUID_KEY = "com.example.restaurapp.uuid"
        val ANSW_OPEN = "OPEN"
        val ANSW_DELETE = "DELETE"
        private var notificationId = 0
        fun getNotificationUniqueID(): Int {
            return notificationId++;
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = (this.application as MyApplication)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree()) //Init report type
        }
        createNotificationChannel()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView


        /*navController = findNavController(R.id.nav_host_fragment_activity_main2)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.reservationsFragment, R.id.addResFragment, R.id.mapsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/

        navView.setOnItemSelectedListener { item ->
            Timber.d("selected ")
           // var fragment: Fragment? = null
            when (item.itemId) {
                R.id.addResFragment -> {
                    loadFragment(AddResFragment())
                    //active = addResFragment
                    true
                }
                R.id.mapsFragment -> {
                    loadFragment(MapsFragment())
                    true
                }
                R.id.reservationsFragment -> {
                    /*reservationsFragment.arguments = bundle
                    supportFragmentManager.beginTransaction().hide(active).show(reservationsFragment).commit();
                    active = reservationsFragment*/
                    loadFragment(ReservationsFragment())
                     true
                }
                else -> {false}
            }
        }

        navView.selectedItemId =
            R.id.addResFragment

        /*fm.beginTransaction().add(R.id.nav_host_fragment_activity_main2, addResFragment, "4").hide(addResFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment_activity_main2, mapsFragment , "3").hide(mapsFragment).commit();

        fm.beginTransaction().add(R.id.nav_host_fragment_activity_main2,homeFragment, "1").hide(homeFragment).commit();
        ///navView.selectedItemId = R.id.homeFragment
        fm.beginTransaction().add(R.id.nav_host_fragment_activity_main2, reservationsFragment , "2").hide(reservationsFragment).commit();
        fm.beginTransaction().show(homeFragment).commit()*/


    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container1,fragment)
        transaction.commit()
    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MyTestChannel"
            val descriptionText = "Testing notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


    }

    /*@RequiresApi(Build.VERSION_CODES.O)
    fun createNotication() {

        var currentDateTime =LocalDateTime.now()
        //val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        var time = currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        createNotifyWithIntent(
            "My title Notification with Intent",
            "Something is working on",
            time.toString(),
        R.drawable.ic_not_stat
        )
    }*/

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotication(uuid: String, cal: Calendar, name: String, title: String): Int {
        var id = app.getSavedNotId();
        cal.add(Calendar.HOUR, -1);
        createNotifyWithIntent(
            id,
            uuid,
            name,
            title,
            cal
        )
        return id;
    }

    fun createNotifyWithIntent(id: Int,uuid:String, name: String, title: String, cal: Calendar) {

        val intent = Intent(this, MyNotificationReceiver::class.java).apply {
            //action = MY_ACTION_FILTER
            putExtra("name", name)
            putExtra("title", title)
            putExtra("uuid", uuid)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)

        //TODO deleting updated messages
        /*val intent2 = Intent(this, MyNotificationReceiver::class.java)
        val pendingIntent2 = PendingIntent.getBroadcast(this, 0, intent2, 0)

        val alarmManager2 = getSystemService(ALARM_SERVICE) as AlarmManager

        alarmManager2.cancel(pendingIntent2);*/

    }

    fun deleteNotification(id:Int){
        val intent2 = Intent(this, MyNotificationReceiver::class.java)
        val pendingIntent2 = PendingIntent.getBroadcast(this, id, intent2, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager2 = getSystemService(ALARM_SERVICE) as AlarmManager

        alarmManager2.cancel(pendingIntent2);
    }


    val br: BroadcastReceiver = MyNotificationReceiver()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.apply {
            if (extras?.getString(KEY) == ANSW_OPEN) {
                val bundle = Bundle()
                bundle.putString("uuid", extras?.getString(UUID_KEY))
                this@MainActivity.bundle = bundle
                (this@MainActivity.findViewById<View>(R.id.nav_view) as BottomNavigationView).selectedItemId =
                    R.id.addResFragment

            } else
                if (extras?.getString(KEY) == ANSW_DELETE && extras?.getString(UUID_KEY) != null) {
                    this@MainActivity.app
                    var reservation: Reservation? = app.data.getReservation(extras?.getString(UUID_KEY)!!);
                    this@MainActivity.deleteNotification( reservation!!.alarmId)
                    app.data.deleteReservation(reservation.uuid);
                    //adapter.notifyDataSetChanged()
                    app.saveDatabase()

                    this@MainActivity.bundle = Bundle()
                    (this@MainActivity.findViewById<View>(R.id.nav_view) as BottomNavigationView).selectedItemId =
                        R.id.reservationsFragment
                }
            cancelAllNotification()
        }
    }

    private fun cancelAllNotification() {
        val ns = NOTIFICATION_SERVICE
        val nMgr = applicationContext.getSystemService(ns) as NotificationManager
        nMgr.cancelAll()
    }


}