package com.example.restaurapp.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.restaurapp.MainActivity
import com.example.restaurapp.R
import timber.log.Timber

class MyNotificationReceiver : BroadcastReceiver() {
    /*override fun onReceive(context: Context, intent: Intent?) {
        Timber.d(" WELCOME in MyNotificationReceiver")
        val intent1 = Intent(context, MyNewIntentService::class.java)
        context.startService(intent1)
    }*/


    override fun onReceive(context: Context, intent: Intent) {

        var name = intent.getStringExtra("name")
        var title = intent.getStringExtra("title")
        var uuid = intent.getStringExtra("uuid")

        val myTimeIntent = Intent(context, MyNotificationReceiver::class.java).apply {
            action = MainActivity.MY_ACTION_FILTER
        }


        val inOpen = Intent(context, MainActivity::class.java)
        inOpen.putExtra(MainActivity.KEY, MainActivity.ANSW_OPEN)
        inOpen.putExtra(MainActivity.UUID_KEY, uuid)
        inOpen.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntentOpen =
            PendingIntent.getActivity(
                context,
                MainActivity.getNotificationUniqueID(),
                inOpen,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val inDelete = Intent(context, MainActivity::class.java)
        inDelete.putExtra(MainActivity.KEY, MainActivity.ANSW_DELETE)
        inDelete.putExtra(MainActivity.UUID_KEY, uuid)
        inDelete.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntentDelete =
            PendingIntent.getActivity(
                context,
                MainActivity.getNotificationUniqueID(),
                inDelete,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        var builder = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
            .setSmallIcon(R.drawable.pizza)
            .setContentTitle("Reservation for $name")
            .setContentText("Title: $title, reservation is in 30 min")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            //  .setContentIntent(myPendingIntent)
            .addAction(
                R.drawable.ic_action_yes, "Open",
                pendingIntentOpen
            )
            .addAction(
                R.drawable.ic_action_no, "Delete Reservation",
                pendingIntentDelete
            )


        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)

        notificationManager.notify(MainActivity.getNotificationUniqueID(), builder.build())
       Timber.d(" WELCOME in MyNotificationReceiver")

    }
}
