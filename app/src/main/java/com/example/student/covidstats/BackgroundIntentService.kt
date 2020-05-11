package com.example.student.covidstats

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat



private const val ACTION_FETCH_ALL = "com.example.student.covidstats.action.FETCH_ALL"
private const val ACTION_FETCH_DETAILS = "com.example.student.covidstats.action.FETCH_DETAILS"
private const val EXTRA_COUNTRY_NAME = "com.example.student.covidstats.extra.EXTRA_COUNTRY_NAME"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class BackgroundIntentService : IntentService("BackgroundIntentService") {
    private lateinit var mRepository: Repository

    override fun onCreate() {
        super.onCreate()
        mRepository = Repository(CountryEntityRoomDatabase.getDatabase(application).countryEntityDao())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notBuilder: NotificationCompat.Builder
        val mNotificationIntent = Intent(this, MainActivity::class.java)
        val mNotificationPendingIntent = PendingIntent.getActivity(this, 0,
            mNotificationIntent,0)
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
        if(!isNetworkAvailable()) {
            Log.d("DEBUG", "NO CONNECTION")
            return
        }
        Log.d("DEBUG", "WOLOLO " + mRepository.getAll().toString())
        when (intent?.action) {
            ACTION_FETCH_ALL -> {
                handleActionFetchAll()
            }
            ACTION_FETCH_DETAILS -> {
        val countryName = intent.getStringExtra(EXTRA_COUNTRY_NAME)
                handleActionFetchDetails(countryName)
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
        try {
            mRepository.apiFetchAll()
        } catch (ex: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    /**
     * Handle action FetchDetails in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFetchDetails(countryName: String) {
        try {
            mRepository.apiFetchDetails(countryName)
//            ApiResolver.apiFetchDetails(applicationContext, countryName)
        } catch (ex: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =  getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
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
        fun startActionFetchDetails(context: Context, param1: String) {
            val intent = Intent(context, BackgroundIntentService::class.java).apply {
                action = ACTION_FETCH_DETAILS
                putExtra(EXTRA_COUNTRY_NAME, param1)
           }
            context.startService(intent)
        }
    }
}
