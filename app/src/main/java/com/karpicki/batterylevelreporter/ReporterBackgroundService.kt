package com.karpicki.batterylevelreporter

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule


class ReporterBackgroundService(): Service() {

    private val nextRunInMilliseconds : Long = 10 * Constants.minute.toLong()
    //private val nextRunInMilliseconds : Long = 10 * Constants.second.toLong()
    private var lastBatteryLevel : Float? = 0F

    private fun send() {

        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { filter ->
            applicationContext.registerReceiver(null, filter)
        }

        val batteryPercentage: Float? = batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }

        if (lastBatteryLevel == batteryPercentage) {
            return
        }

        lastBatteryLevel = batteryPercentage

        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            //var status: Int = 0

            // option 2
            IFTTTClient.send("bt_server_battery_percentage", batteryPercentage.toString())

            // option 3
            //ThingSpeakClient.send(batteryPercentage.toString(), "field1")
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i("ReporterService", "start")

        send()

        onTaskRemoved(intent)
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)

        Timer("Next ReporterService", false).schedule(nextRunInMilliseconds) {
            startService(restartServiceIntent)
            //startService(rootIntent)
        }
        super.onTaskRemoved(rootIntent)
    }

}