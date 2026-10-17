package com.example.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.model.PrayerTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object AdhanNotificationHelper {

    private const val TAG = "AdhanNotificationHelper"
    const val CHANNEL_ID = "noor_adhan_channel_v2"
    const val CHANNEL_NAME = "تنبيهات أذان الصلاة"
    const val ACTION_PRAYER_ADHAN = "com.example.ACTION_PRAYER_ADHAN"

    const val EXTRA_PRAYER_KEY = "extra_prayer_key"
    const val EXTRA_PRAYER_NAME_AR = "extra_prayer_name_ar"
    const val EXTRA_PRAYER_NAME_EN = "extra_prayer_name_en"
    const val EXTRA_PRAYER_TIME = "extra_prayer_time"

    private val PRAYER_REQUEST_CODES = mapOf(
        "fajr" to 101,
        "dhuhr" to 102,
        "asr" to 103,
        "maghrib" to 104,
        "isha" to 105
    )

    private var activeMediaPlayer: MediaPlayer? = null

    /**
     * Initializes the Adhan notification channel with custom Adhan sound
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val soundResId = context.resources.getIdentifier("adhan", "raw", context.packageName)
            val soundUri = Uri.parse("android.resource://${context.packageName}/$soundResId")
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "قناة إشعارات أذان الصلوات الخمس مع الصوت"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                setSound(soundUri, audioAttributes)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Schedules exact Adhan alarms for all 5 obligatory daily prayers based on dynamic prayer times
     */
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleAllPrayerAdhans(context: Context, prayerTimes: List<PrayerTime>) {
        if (prayerTimes.isEmpty()) return
        createNotificationChannel(context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val obligatoryKeys = setOf("fajr", "dhuhr", "asr", "maghrib", "isha")
        val now = System.currentTimeMillis()

        for (prayer in prayerTimes) {
            val key = prayer.key.lowercase()
            if (!obligatoryKeys.contains(key)) continue

            val triggerTime = parseTimeToEpoch(prayer.time) ?: continue
            val scheduledTime = if (triggerTime > now) {
                triggerTime
            } else {
                // If already passed today, schedule for next day at same time
                triggerTime + 24 * 60 * 60 * 1000L
            }

            val requestCode = PRAYER_REQUEST_CODES[key] ?: 100
            val intent = Intent(context, AdhanAlarmReceiver::class.java).apply {
                action = ACTION_PRAYER_ADHAN
                putExtra(EXTRA_PRAYER_KEY, key)
                putExtra(EXTRA_PRAYER_NAME_AR, prayer.nameArabic)
                putExtra(EXTRA_PRAYER_NAME_EN, prayer.nameEnglish)
                putExtra(EXTRA_PRAYER_TIME, prayer.time)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        scheduledTime,
                        pendingIntent
                    )
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        scheduledTime,
                        pendingIntent
                    )
                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        scheduledTime,
                        pendingIntent
                    )
                }
                Log.d(TAG, "Scheduled Adhan for ${prayer.nameArabic} ($key) at ${Date(scheduledTime)}")
            } catch (se: SecurityException) {
                Log.w(TAG, "Exact alarm permission denied, falling back to setAndAllowWhileIdle for $key")
                try {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        scheduledTime,
                        pendingIntent
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Failed fallback alarm scheduling for $key: ${e.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to schedule alarm for $key: ${e.message}")
            }
        }
    }

    /**
     * Cancels all scheduled Adhan alarms for all prayers
     */
    fun cancelAllPrayerAdhans(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        for ((key, requestCode) in PRAYER_REQUEST_CODES) {
            val intent = Intent(context, AdhanAlarmReceiver::class.java).apply {
                action = ACTION_PRAYER_ADHAN
                putExtra(EXTRA_PRAYER_KEY, key)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
                Log.d(TAG, "Cancelled Adhan alarm for $key")
            }
        }
        stopAdhanAudio()
    }

    /**
     * Shows notification and plays the bundled Adhan audio
     */
    fun showAdhanNotification(
        context: Context,
        prayerKey: String,
        prayerNameAr: String,
        prayerNameEn: String,
        prayerTime: String
    ) {
        createNotificationChannel(context)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val contentIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingContentIntent = PendingIntent.getActivity(
            context,
            0,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundResId = context.resources.getIdentifier("adhan", "raw", context.packageName)
        val soundUri = Uri.parse("android.resource://${context.packageName}/$soundResId")

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("حان الآن وقت أذان $prayerNameAr ($prayerTime)")
            .setContentText("الله أكبر، الله أكبر • حان موعد صلاة $prayerNameAr. حي على الصلاة، حي على الفلاح.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("الله أكبر، الله أكبر • حان الآن موعد أذان $prayerNameAr بحسب التوقيت المحلي ($prayerTime).\nحيّ على الصلاة، حيّ على الفلاح.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingContentIntent)
            .setColor(0xFF2563EB.toInt())
            .build()

        val notificationId = PRAYER_REQUEST_CODES[prayerKey.lowercase()] ?: 200
        notificationManager.notify(notificationId, notification)

        // Play audio directly via MediaPlayer for guaranteed playback across all Android devices
        playAdhanAudio(context)
    }

    /**
     * Plays the bundled local Adhan audio using MediaPlayer
     */
    @Synchronized
    fun playAdhanAudio(context: Context) {
        try {
            stopAdhanAudio()
            val soundResId = context.resources.getIdentifier("adhan", "raw", context.packageName)
            if (soundResId != 0) {
                val mp = MediaPlayer.create(context, soundResId)
                if (mp != null) {
                    mp.setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .build()
                    )
                    mp.setOnCompletionListener { player ->
                        player.release()
                        activeMediaPlayer = null
                    }
                    mp.start()
                    activeMediaPlayer = mp
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error playing Adhan audio: ${e.message}")
        }
    }

    /**
     * Stops active Adhan audio playback
     */
    @Synchronized
    fun stopAdhanAudio() {
        try {
            activeMediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
        } catch (_: Exception) {}
        activeMediaPlayer = null
    }

    /**
     * Converts a time string like "05:12" or "05:12 ص" into today's epoch milliseconds
     */
    fun parseTimeToEpoch(timeStr: String): Long? {
        return try {
            val clean = timeStr.trim().replace("ص", "AM").replace("م", "PM").trim()
            val formats = listOf(
                SimpleDateFormat("HH:mm", Locale.getDefault()),
                SimpleDateFormat("hh:mm a", Locale.US),
                SimpleDateFormat("hh:mm", Locale.getDefault())
            )

            var parsedDate: Date? = null
            for (format in formats) {
                try {
                    parsedDate = format.parse(clean)
                    if (parsedDate != null) break
                } catch (_: Exception) {}
            }

            if (parsedDate == null) return null

            val timeCal = Calendar.getInstance().apply { time = parsedDate }
            val nowCal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE))
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            nowCal.timeInMillis
        } catch (e: Exception) {
            null
        }
    }
}
