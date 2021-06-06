package com.karpicki.batterylevelreporter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import java.util.*

class BootUpReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {

        if (Objects.equals(intent.action, Intent.ACTION_BOOT_COMPLETED)) {

            Toast.makeText(
                context, "This is a BootUpReceiver",
                Toast.LENGTH_SHORT
            ).show()

            val i = Intent(context, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(i)
        }
    }
}