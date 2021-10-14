package com.kt.myproject.repository.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kt.myproject.R
import com.kt.myproject.ex.string
import com.kt.myproject.ex.toJsonObject
import com.kt.myproject.repository.store.PreferencesStore
import com.kt.myproject.ui.activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/10/13
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class MessageService : FirebaseMessagingService() {

    companion object {
        var NOTIFICATION_ID = 0
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        CoroutineScope(Dispatchers.IO).launch {
            PreferencesStore.saveToken(p0)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isNotEmpty()) {
            parseRemoteMessage(remoteMessage.data)
        }
        remoteMessage.notification?.body?.let {
            parseRemoteMessageNotification(it)
        }
    }

    private fun parseRemoteMessageNotification(messageBody: String) {
        val json = messageBody.toJsonObject()
        if (json != null) {
            val title = json["title"].toString()
            val body = json["body"].toString()
            val image = json["image"].toString()
            handleFcmNotification(title, body, image)
        }
    }

    private fun parseRemoteMessage(messageBody: Map<String, String>) {
        val title = messageBody["title"].toString()
        val message = messageBody["message"].toString()
        val image = messageBody["image"].toString()
        handleFcmNotification(title, message, image)
    }

    private fun handleFcmNotification(title: String, message: String, image: String) {
        val requestID = System.currentTimeMillis().toInt()
        val notifyIntent = Intent(this, MainActivity::class.java)
        notifyIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestID,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = string(R.string.notification_channel_id)
        val channelName = string(R.string.app_name)

        var myBitmap: Bitmap? = null
        if (image != "") myBitmap = bitmapFromURL(image)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(message)
            .setBigContentTitle(title)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setSmallIcon(R.mipmap.ic_fb)
            .setStyle(bigTextStyle)
            .setSound(defaultSoundUri)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        if (myBitmap != null) notificationBuilder.setLargeIcon(myBitmap)

        val notificationManager = ContextCompat.getSystemService(
            this, NotificationManager::class.java
        ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        NOTIFICATION_ID++
    }

    private fun bitmapFromURL(strURL: String?): Bitmap? {
        return try {
            val url = URL(strURL)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}