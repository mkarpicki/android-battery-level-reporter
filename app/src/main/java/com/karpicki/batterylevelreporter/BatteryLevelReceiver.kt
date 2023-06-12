package com.karpicki.batterylevelreporter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BatteryLevelReceiver : BroadcastReceiver() {

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
//            val status: Int

            //option 1
//              if (batteryPercentage != null) {
//                  if (batteryPercentage < 5) {
//                      status = IFTTTClient.send("bt_server_battery_low", batteryPercentage.toString())
//                  } else if (batteryPercentage > 95) {
//                      status = IFTTTClient.send("bt_server_battery_full", batteryPercentage.toString())
//                  }
//              }

            // option 2
            //val status = IFTTTClient.send("bt_server_battery_percentage", batteryPercentage.toString())

            // option 3
            val status = ThingSpeakClient.send(batteryPercentage.toString(), BuildConfig.THING_SPEAK_FIELD)

            println("BroadcastReceiver:status: $status")

            if (status in 200..399) {
                lastBatteryLevel = batteryPercentage
            }
        }
    }

}