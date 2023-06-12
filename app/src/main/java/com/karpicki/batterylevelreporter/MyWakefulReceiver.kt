package com.karpicki.batterylevelreporter

import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import androidx.legacy.content.WakefulBroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MyWakefulReceiver : WakefulBroadcastReceiver() {

    private var lastBatteryLevel : Float = 0F

    override fun onReceive(context: Context, intent: Intent) {

        // Start the service, keeping the device awake while the service is
        // launching. This is the Intent to deliver to the service.

//        Intent(context, BatteryLevelService::class.java).also { service ->
//            WakefulBroadcastReceiver.startWakefulService(context, service)
//        }

        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPercentage = level * 100 / scale.toFloat()
        val scope = CoroutineScope(Dispatchers.IO)

        Log.i("MyWakefulReceiver:level", batteryPercentage.toString())
        println("MyWakefulReceiver:level: $batteryPercentage")

        if (lastBatteryLevel == batteryPercentage) {
            return
        }

    }
}