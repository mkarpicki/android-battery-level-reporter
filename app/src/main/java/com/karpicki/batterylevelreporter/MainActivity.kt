package com.karpicki.batterylevelreporter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class MainActivity : AppCompatActivity() {

    private var isBackgroundServiceRunning = false
    private var lastBatteryLevel : Float = 0F

//    private val mBatInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(ctxt: Context?, intent: Intent) {
//            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
//            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
//            val batteryLevel = level * 100 / scale.toFloat()
//
//            if (lastBatteryLevel != batteryLevel) {
//                send(batteryLevel)
//            }
//        }
//    }

//    private fun send(value: Float) {
//
//        var responseCode: Int
//
//        //val apiKey = BuildConfig.THING_SPEAK_API_KEY
//        //val field = field ?: "field1"
//
//        val apiKey = "R8W1JR9JV6JOS0FT"
//        val field = "field1"
//
//
//        try {
//            val client = OkHttpClient();
//
//            // @todo - move api_key to app config
//            val request: Request = Request.Builder()
//                .url("https://api.thingspeak.com/update?api_key=$apiKey&$field=$value")
//                .get()
//                .build()
//
//            val response: Response = client.newCall(request).execute()
//
//            Log.d("TAG", "response.code():" + response.code())
//            //response.body()?.string()
//            responseCode = response.code()
//
//        } catch (e: Exception) {
//            responseCode = 500
//        }
//    }

    private fun start() {
        if (isBackgroundServiceRunning) {
            return
        }

//        val handler: Handler // Handler for the separate Thread
//
//
//        val handlerThread = HandlerThread("NewThread")
//        handlerThread.start()
//        // Now get the Looper from the HandlerThread so that we can create a Handler that is attached to
//        //  the HandlerThread
//        // NOTE: This call will block until the HandlerThread gets control and initializes its Looper
//        // Now get the Looper from the HandlerThread so that we can create a Handler that is attached to
//        //  the HandlerThread
//        // NOTE: This call will block until the HandlerThread gets control and initializes its Looper
//        val looper = handlerThread.looper
//        // Create a handler for the service
//        handler = Handler(looper)
//
//        this.registerReceiver(
//            this.mBatInfoReceiver,
//            IntentFilter(Intent.ACTION_BATTERY_CHANGED),
//            null,
//            handler);

        val reportIntent = Intent(applicationContext, ReporterBackgroundService::class.java)
        startService(reportIntent)

        isBackgroundServiceRunning = true
    }

//    private fun stop() {
//        this.unregisterReceiver(this.mBatInfoReceiver);
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putBoolean(Constants.semaphoreName, isBackgroundServiceRunning)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) { // Here You have to restore count value
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getBoolean(Constants.semaphoreName)
            .also { isBackgroundServiceRunning = it }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start()
    }

//    override fun onDestroy() {
//        stop()
//        super.onDestroy()
//    }
//
//    override fun onPause() {
//        stop()
//        super.onPause()
//    }
}