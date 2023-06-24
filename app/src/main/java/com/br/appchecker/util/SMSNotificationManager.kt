package com.br.appchecker.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import androidx.core.content.ContextCompat

class SMSNotificationManager {
    fun sendSMS(context: Context, phoneNumber: String, message: String) {
        val smsManager: SmsManager =
            context.getSystemService(SmsManager::class.java)

        val permission = Manifest.permission.SEND_SMS
        val granted = PackageManager.PERMISSION_GRANTED

        if (ContextCompat.checkSelfPermission(context, permission) == granted) {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        }
    }
}