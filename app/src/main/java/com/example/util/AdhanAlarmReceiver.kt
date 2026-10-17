package com.example.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AdhanAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d("AdhanAlarmReceiver", "Received intent action: $action")

        if (action == Intent.ACTION_BOOT_COMPLETED) {
            val prefs = context.getSharedPreferences("noor_settings", Context.MODE_PRIVATE)
            val adhanEnabled = prefs.getBoolean("adhan_reminder_enabled", true)
            if (adhanEnabled) {
                Log.d("AdhanAlarmReceiver", "Device rebooted: Adhan reminder is enabled. Will reschedule on app start.")
            }
        } else if (action == AdhanNotificationHelper.ACTION_PRAYER_ADHAN) {
            val prayerKey = intent.getStringExtra(AdhanNotificationHelper.EXTRA_PRAYER_KEY) ?: "prayer"
            val prayerNameAr = intent.getStringExtra(AdhanNotificationHelper.EXTRA_PRAYER_NAME_AR) ?: "الصلاة"
            val prayerNameEn = intent.getStringExtra(AdhanNotificationHelper.EXTRA_PRAYER_NAME_EN) ?: "Prayer"
            val prayerTime = intent.getStringExtra(AdhanNotificationHelper.EXTRA_PRAYER_TIME) ?: ""

            // 1. Show notification and play bundled Adhan audio
            AdhanNotificationHelper.showAdhanNotification(
                context = context,
                prayerKey = prayerKey,
                prayerNameAr = prayerNameAr,
                prayerNameEn = prayerNameEn,
                prayerTime = prayerTime
            )

            // 2. Re-schedule this prayer for tomorrow (24 hours later)
            val prefs = context.getSharedPreferences("noor_settings", Context.MODE_PRIVATE)
            val adhanEnabled = prefs.getBoolean("adhan_reminder_enabled", true)
            if (adhanEnabled) {
                val triggerEpoch = AdhanNotificationHelper.parseTimeToEpoch(prayerTime)
                if (triggerEpoch != null) {
                    val nextDayEpoch = triggerEpoch + 24 * 60 * 60 * 1000L
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? android.app.AlarmManager
                    val nextIntent = Intent(context, AdhanAlarmReceiver::class.java).apply {
                        this.action = AdhanNotificationHelper.ACTION_PRAYER_ADHAN
                        putExtra(AdhanNotificationHelper.EXTRA_PRAYER_KEY, prayerKey)
                        putExtra(AdhanNotificationHelper.EXTRA_PRAYER_NAME_AR, prayerNameAr)
                        putExtra(AdhanNotificationHelper.EXTRA_PRAYER_NAME_EN, prayerNameEn)
                        putExtra(AdhanNotificationHelper.EXTRA_PRAYER_TIME, prayerTime)
                    }
                    val requestCode = when (prayerKey.lowercase()) {
                        "fajr" -> 101
                        "dhuhr" -> 102
                        "asr" -> 103
                        "maghrib" -> 104
                        "isha" -> 105
                        else -> 100
                    }
                    val pendingIntent = android.app.PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        nextIntent,
                        android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                    )
                    try {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            alarmManager?.setExactAndAllowWhileIdle(
                                android.app.AlarmManager.RTC_WAKEUP,
                                nextDayEpoch,
                                pendingIntent
                            )
                        } else {
                            alarmManager?.setExact(
                                android.app.AlarmManager.RTC_WAKEUP,
                                nextDayEpoch,
                                pendingIntent
                            )
                        }
                    } catch (se: SecurityException) {
                        try {
                            alarmManager?.setAndAllowWhileIdle(
                                android.app.AlarmManager.RTC_WAKEUP,
                                nextDayEpoch,
                                pendingIntent
                            )
                        } catch (_: Exception) {}
                    } catch (e: Exception) {
                        Log.e("AdhanAlarmReceiver", "Failed to reschedule next day alarm: ${e.message}")
                    }
                }
            }
        }
    }
}
