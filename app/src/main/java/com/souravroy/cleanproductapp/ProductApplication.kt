package com.souravroy.cleanproductapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.souravroy.cleanproductapp.modules.notification.NotificationService.Companion.CHANNEL_ID
import dagger.hilt.android.HiltAndroidApp

/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

@HiltAndroidApp
class ProductApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Create the NotificationChannel
        val name = "Product Channel"
        val descriptionText = "Channel for product notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}