package com.karpicki.batterylevelreporter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private var isReceiverRegistered = false

    private val mBatInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        private var lastBatteryLevel : Float = 0F

        override fun onReceive(ctxt: Context?, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPercentage = level * 100 / scale.toFloat()
            val scope = CoroutineScope(Dispatchers.IO)

            Log.i("BroadcastReceiver:level", batteryPercentage.toString())
            println("BroadcastReceiver:level: $batteryPercentage")

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

                println("BroadcastReceiver:status: $status")

                if (status in 200..399) {
                    lastBatteryLevel = batteryPercentage
                }
            }
        }
    }

    private fun start() {
        if (isReceiverRegistered) {
            return
        }

        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_BATTERY_CHANGED)
        filter.addAction(Intent.ACTION_BATTERY_LOW)
        filter.addAction(Intent.ACTION_BATTERY_OKAY)

        this.registerReceiver(
            this.mBatInfoReceiver,
            filter
        );

        isReceiverRegistered = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putBoolean(Constants.semaphoreName, isReceiverRegistered)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            savedInstanceState.getBoolean(Constants.semaphoreName)
                .also { isReceiverRegistered = it }
        }

        //window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        start()
    }

}