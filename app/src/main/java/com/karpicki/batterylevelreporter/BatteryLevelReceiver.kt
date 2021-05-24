package com.karpicki.batterylevelreporter

import android.content.Context
import android.content.Intent
import androidx.legacy.content.WakefulBroadcastReceiver

// https://developer.android.com/training/scheduling/wakelock#wakeful
// https://stackoverflow.com/questions/45392037/broadcast-receiver-in-kotlin
// https://android--code.blogspot.com/2020/08/android-kotlin-get-battery-percentage.html

class BatteryLevelReceiver : WakefulBroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        // Start the service, keeping the device awake while the service is
        // launching. This is the Intent to deliver to the service.
        Intent(context, ReporterBackgroundService::class.java).also { service ->
            WakefulBroadcastReceiver.startWakefulService(context, service)
        }
    }
}