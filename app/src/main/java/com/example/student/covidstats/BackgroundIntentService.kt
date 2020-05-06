package com.example.student.covidstats

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat


// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_FETCH_ALL = "com.example.student.covidstats.action.FETCH_ALL"
private const val ACTION_FETCH_DETAILS = "com.example.student.covidstats.action.FETCH_DETAILS"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "com.example.student.covidstats.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.example.student.covidstats.extra.PARAM2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class BackgroundIntentService : IntentService("BackgroundIntentService") {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("asd","startcommand")
        val notBuilder: NotificationCompat.Builder
        val mNotificationIntent = Intent(this, MainActivity::class.java)
        val mNotificationPendingIntent = PendingIntent.getActivity(this, 0,
            mNotificationIntent,
            0)
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            } else {
                ""
            }

        notBuilder = NotificationCompat.Builder(
            this, channelId)
            .setContentTitle("Data synchronization")
            .setContentText("Fetching new data")
            .setSmallIcon(R.mipmap.ic_notification)
            .setColor(Color.WHITE)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_notification))
            .setAutoCancel(false)
            .setContentIntent(mNotificationPendingIntent)

        startForeground(MainActivity.MY_NOTIFICATION_ID,
            notBuilder.build())
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onHandleIntent(intent: Intent?) {
                Log.d("asd", "Create request for API :D")
                when (intent?.action) {
                    ACTION_FETCH_ALL -> {
                        handleActionFetchAll()
                    }
                    ACTION_FETCH_DETAILS -> {
//                val param1 = intent.getStringExtra(EXTRA_PARAM1)
//                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                        handleActionFetchDetails()
                    }
                }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {

        val channelId = "PlayerServiceChannel"
        val channelName = "Player Service Channel"
        val channel = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_LOW)
        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        return channelId
    }

    /**
     * Handle action FetchAll in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFetchAll(param1: String="default", param2: String="default") {
        Log.d("asd","executing service fetchALL")
        try {
            Log.d("asd","BEFORE")
            ApiResolver.apiFetchAll(applicationContext)
//            Thread.sleep((10 * 1000).toLong())
        } catch (ex: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    /**
     * Handle action FetchDetails in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFetchDetails(country: String="poland", param2: String="whatever") {
        Log.d("asd","executing service fetchDetails")
        try {
            Log.d("asd","BEFORE")
            ApiResolver.apiFetchDetails(country)
        } catch (ex: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    companion object {
        /**
         * Starts this service to perform action FetchAll with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startActionFetchAll(context: Context) {
            val intent = Intent(context, BackgroundIntentService::class.java).apply {
                action = ACTION_FETCH_ALL
//                putExtra(EXTRA_PARAM1, param1)
//                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }

        /**
         * Starts this service to perform action FetchDetails with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startActionFetchDetails(context: Context) {
            val intent = Intent(context, BackgroundIntentService::class.java).apply {
                action = ACTION_FETCH_DETAILS
//                putExtra(EXTRA_PARAM1, param1)
//                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }
    }
}
