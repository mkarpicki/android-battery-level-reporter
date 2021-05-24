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
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class MainActivity : AppCompatActivity() {

    private var isBackgroundServiceRunning = false
    private var lastBatteryLevel : Float = 0F

    private val mBatInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context?, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPercentage = level * 100 / scale.toFloat()
            val scope = CoroutineScope(Dispatchers.IO)

            Log.i("BroadcastReceiver:level", batteryPercentage.toString())
            println("BroadcastReceiver:level:" + batteryPercentage.toString())


            if (lastBatteryLevel == batteryPercentage) {
                return
            }

            scope.launch {
                var status: Int

                //option 1
//              if (batteryPercentage != null) {
//                  if (batteryPercentage < 5) {
//                      status = IFTTTClient.send("bt_server_battery_low", batteryPercentage.toString())
//                  } else if (batteryPercentage > 95) {
//                      status = IFTTTClient.send("bt_server_battery_full", batteryPercentage.toString())
//                  }
//              }

                // option 2
                status = IFTTTClient.send("bt_server_battery_percentage", batteryPercentage.toString())

                // option 3
                //status = ThingSpeakClient.send(batteryPercentage.toString(), "field1")

                println("BroadcastReceiver:status:" + status.toString())

                if (status >= 200 && status < 400) {
                    lastBatteryLevel = batteryPercentage
                }
            }


        }
    }

    private fun start() {
        if (isBackgroundServiceRunning) {
            return
        }

        this.registerReceiver(
            this.mBatInfoReceiver,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        );

        //val reportIntent = Intent(applicationContext, ReporterBackgroundService::class.java)
        //startService(reportIntent)

        isBackgroundServiceRunning = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putBoolean(Constants.semaphoreName, isBackgroundServiceRunning)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            savedInstanceState.getBoolean(Constants.semaphoreName)
                .also { isBackgroundServiceRunning = it }
        }

        //window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        start()
    }

}