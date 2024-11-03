package com.we.presentation.main.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.we.presentation.R
import com.we.presentation.main.MainActivity
import com.we.presentation.sign.SignActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    // 메시지 수신
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("메세지 위치 호출 ${remoteMessage.notification}")
        Timber.d("데이터 메세지 호출 ${remoteMessage.data}")
        sendNotification(remoteMessage)

    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val intent = Intent(this, SignActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )


        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_petal)
            .setContentTitle(remoteMessage.data.get("title"))
            .setContentText(remoteMessage.data.get("body"))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 이상에서 알림을 제공하려면 앱의 알림 채널을 시스템에 등록해야 한다.
        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(0, notificationBuilder.build())

    }


}