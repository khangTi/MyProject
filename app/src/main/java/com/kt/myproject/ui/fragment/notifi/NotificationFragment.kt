package com.kt.myproject.ui.fragment.notifi

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.kt.myproject.R
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.NotificationBinding


class NotificationFragment : BaseFragment<NotificationBinding>(NotificationBinding::inflate) {

    private val channelId = "Notification"
    private var idNotification = 0

    override fun onViewCreated() {
        binding.notifiSmallIcon.actionClickListener(this::setupSmallIcon)
        binding.notifiLargeIcon.actionClickListener(this::setupLargeIcon)
        binding.notifiWithAction.actionClickListener(this::setupWithAction)
        binding.notifiWithSound.actionClickListener(this::setupWithSound)
        binding.notifiBigContent.actionClickListener(this::setupBigContent)
        binding.notifiBigImage.actionClickListener(this::setupBigImage)
    }

    override fun onLiveDataObserve() {
    }

    private fun setupSmallIcon() {
        val mBuilder = NotificationCompat.Builder(requireContext(), "setupSmallIcon")
        mBuilder.setSmallIcon(R.mipmap.ic_fb)
        mBuilder.setContentTitle("Notification Test")
        mBuilder.setContentText("you have a message of ahihi")
        val mNotificationManager: NotificationManager =
            activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "notification", NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }
        mNotificationManager.notify(idNotification++, mBuilder.build())
    }

    private fun setupLargeIcon() {
        val mBuilder = NotificationCompat.Builder(requireContext(), "setupLargeIcon")
        mBuilder.setSmallIcon(R.mipmap.ic_fb)
        mBuilder.setContentTitle("Notification Test")
        mBuilder.setContentText("ahihi send for you a image")
        val image = BitmapFactory.decodeResource(
            activity?.resources,
            R.mipmap.ic_fb
        )
        mBuilder.setLargeIcon(image)
        val mNotificationManager: NotificationManager =
            activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "notification", NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }
        mNotificationManager.notify(idNotification++, mBuilder.build())
    }

    private fun setupWithAction() {
        val mBuilder = NotificationCompat.Builder(requireContext(), "setupWithAction")
        mBuilder.setSmallIcon(R.mipmap.ic_ins)
        mBuilder.setContentTitle("Notification Test")
        mBuilder.setContentText("ahihi send for you a image")
        val image = BitmapFactory.decodeResource(activity?.resources, R.mipmap.ic_ins)
        mBuilder.setLargeIcon(image)
        val action = NotificationCompat.Action(R.drawable.ic_vector, "view", null)
        mBuilder.addAction(action)
        val mNotificationManager: NotificationManager =
            activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "notification", NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }
        mNotificationManager.notify(idNotification++, mBuilder.build())
    }

    private fun setupWithSound() {
        val mBuilder = NotificationCompat.Builder(requireContext(), "setupSmallIcon")
        mBuilder.setSmallIcon(R.mipmap.ic_twt)
        mBuilder.setContentTitle("Notification Test")
        mBuilder.setContentText("ahihi send for you a image")

        val image = BitmapFactory.decodeResource(activity?.resources, R.mipmap.ic_twt)
        mBuilder.setLargeIcon(image)

        val action = NotificationCompat.Action(R.drawable.ic_vector, "view", null)
        mBuilder.addAction(action)

        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        mBuilder.setSound(notificationSound)

        val mNotificationManager: NotificationManager =
            activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "notification", NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }
        mNotificationManager.notify(idNotification++, mBuilder.build())
    }

    private fun setupBigContent() {
        val mBuilder = NotificationCompat.Builder(requireContext(), "setupSmallIcon")
        mBuilder.setSmallIcon(R.mipmap.ic_twt)
        mBuilder.setContentTitle("Notification Test")
        /*mBuilder.setContentText("ahihi send for you a image")*/

        val image = BitmapFactory.decodeResource(activity?.resources, R.mipmap.ic_twt)
        mBuilder.setLargeIcon(image)

        mBuilder.setStyle(
            NotificationCompat.InboxStyle()
                .addLine("Bro!, today Armin in Mamacona")
                .addLine("State of trance, fucking holy madness")
                .addLine("Goooooooooooo!")
        )

        val mNotificationManager: NotificationManager =
            activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "notification", NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }
        mNotificationManager.notify(idNotification++, mBuilder.build())
    }

    private fun setupBigImage() {
        val mBuilder = NotificationCompat.Builder(requireContext(), "setupSmallIcon")
        mBuilder.setSmallIcon(R.mipmap.ic_twt)
        mBuilder.setContentTitle("Notification Test")
        mBuilder.setContentText("big image")

        val largeImage = BitmapFactory.decodeResource(activity?.resources, R.mipmap.ic_twt)
        val bigImage = BitmapFactory.decodeResource(activity?.resources, R.mipmap.image_noti)

        val style: NotificationCompat.BigPictureStyle = NotificationCompat.BigPictureStyle()
        style.bigPicture(bigImage)
        style.setSummaryText("a image notification")
        style.bigLargeIcon(largeImage)
        mBuilder.setStyle(style)

        val mNotificationManager: NotificationManager =
            activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "notification", NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }
        mNotificationManager.notify(idNotification++, mBuilder.build())
    }

}