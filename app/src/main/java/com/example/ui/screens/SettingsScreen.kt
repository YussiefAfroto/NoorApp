package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CelestialBackground
import com.example.ui.theme.CelestialBorderHairline
import com.example.ui.theme.CelestialOnSurface
import com.example.ui.theme.CelestialPrimary
import com.example.ui.theme.CelestialSlateMuted
import com.example.ui.theme.CelestialSurface
import com.example.ui.theme.CelestialSurfaceContainerLow

@Composable
fun SettingsScreen(
    isArabic: Boolean,
    adhanReminderEnabled: Boolean,
    preAdhanReminder: Boolean,
    iqamahReminder: Boolean,
    morningReminder: Boolean,
    eveningReminder: Boolean,
    fridayReminder: Boolean,
    qiyamReminder: Boolean,
    hapticEnabled: Boolean,
    onBack: () -> Unit,
    onToggleAdhan: () -> Unit,
    onTogglePreAdhan: () -> Unit,
    onToggleIqamah: () -> Unit,
    onToggleMorning: () -> Unit,
    onToggleEvening: () -> Unit,
    onToggleFriday: () -> Unit,
    onToggleQiyam: () -> Unit,
    onToggleHaptic: () -> Unit,
    onToggleLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CelestialBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Screen Title Bar with Back Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = CelestialSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onBack)
                        .testTag("settings_back_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = if (isArabic) "رجوع" else "Back",
                            tint = CelestialPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column {
                    Text(
                        text = if (isArabic) "الإعدادات العامة" else "General Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = CelestialOnSurface,
                        fontSize = 18.sp
                    )
                    Text(
                        text = if (isArabic) "تخصيص الصلوات، الأذكار، والمظهر" else "Customize prayers, athkar, and appearance",
                        style = MaterialTheme.typography.labelSmall,
                        color = CelestialSlateMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // 2. Category: تنبيهات الصلاة والأذان (Prayer & Adhan Notifications)
        item {
            SettingsSectionCard(title = if (isArabic) "تنبيهات الصلاة والأذان" else "Prayer & Adhan Notifications") {
                // Main Adhan notification switch (Plays full Adhan at every prayer)
                SettingsToggleRow(
                    title = if (isArabic) "تنبيه أذان الصلاة" else "Prayer Adhan Notification",
                    checked = adhanReminderEnabled,
                    onCheckedChange = { onToggleAdhan() },
                    icon = Icons.Default.NotificationsActive
                )
                Divider(color = CelestialBorderHairline, thickness = 0.5.dp)
                SettingsToggleRow(
                    title = if (isArabic) "تنبيه قبل الأذان بـ 15 دقيقة" else "Notification 15 min before Adhan",
                    checked = preAdhanReminder,
                    onCheckedChange = { onTogglePreAdhan() },
                    icon = Icons.Default.AccessTime
                )
                Divider(color = CelestialBorderHairline, thickness = 0.5.dp)
                SettingsToggleRow(
                    title = if (isArabic) "تنبيه وقت الإقامة" else "Iqamah Time Notification",
                    checked = iqamahReminder,
                    onCheckedChange = { onToggleIqamah() },
                    icon = Icons.Default.Notifications
                )
            }
        }

        // 3. Category: أذكار وتذكيرات العبادة (Athkar & Worship Reminders - matching screenshot)
        item {
            SettingsSectionCard(title = if (isArabic) "أذكار وتذكيرات العبادة" else "Athkar & Worship Reminders") {
                SettingsToggleRow(
                    title = if (isArabic) "تذكير أذكار الصباح (06:30 ص)" else "Morning Athkar Reminder (06:30 AM)",
                    checked = morningReminder,
                    onCheckedChange = { onToggleMorning() },
                    icon = Icons.Default.WbSunny
                )
                Divider(color = CelestialBorderHairline, thickness = 0.5.dp)
                SettingsToggleRow(
                    title = if (isArabic) "تذكير أذكار المساء (05:00 م)" else "Evening Athkar Reminder (05:00 PM)",
                    checked = eveningReminder,
                    onCheckedChange = { onToggleEvening() },
                    icon = Icons.Default.Bedtime
                )
                Divider(color = CelestialBorderHairline, thickness = 0.5.dp)
                SettingsToggleRow(
                    title = if (isArabic) "تذكير قراءة سورة الكهف يوم الجمعة" else "Friday Surat Al-Kahf Reminder",
                    checked = fridayReminder,
                    onCheckedChange = { onToggleFriday() },
                    icon = Icons.Default.AutoAwesome
                )
                Divider(color = CelestialBorderHairline, thickness = 0.5.dp)
                SettingsToggleRow(
                    title = if (isArabic) "تنبيه الثلث الأخير من الليل (القيام)" else "Last Third of Night Reminder (Qiyam)",
                    checked = qiyamReminder,
                    onCheckedChange = { onToggleQiyam() },
                    icon = Icons.Default.DarkMode
                )
            }
        }

        // 4. Category: المظهر والنظام (Appearance & System)
        item {
            SettingsSectionCard(title = if (isArabic) "المظهر والنظام" else "Appearance & System") {
                SettingsActionRow(
                    title = if (isArabic) "لغة التطبيق" else "App Language",
                    subtitle = if (isArabic) "العربية (Arabic)" else "English",
                    icon = Icons.Default.Language,
                    onClick = onToggleLanguage
                )
                Divider(color = CelestialBorderHairline, thickness = 0.5.dp)
                SettingsToggleRow(
                    title = if (isArabic) "الاهتزاز اللمسي (Haptic Feedback)" else "Haptic Feedback",
                    checked = hapticEnabled,
                    onCheckedChange = { onToggleHaptic() },
                    icon = Icons.Default.Vibration
                )
            }
        }

        // 5. Footer / App Info & Version 1.0
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, CelestialBorderHairline, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.noor_logo),
                        contentDescription = "شعار تطبيق نور الرسمي",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (isArabic) "تطبيق نُور (Noor)" else "Noor App",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = CelestialOnSurface,
                    fontSize = 13.sp
                )
                Text(
                    text = if (isArabic) "مشكاة السكينة، والقرآن، والصلوات • صدقة جارية" else "Serenity, Quran & Prayer Times • Ongoing Charity",
                    style = MaterialTheme.typography.labelSmall,
                    color = CelestialSlateMuted,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Version 1.0",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = CelestialPrimary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = CelestialOnSurface,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, CelestialBorderHairline, RoundedCornerShape(18.dp)),
            color = CelestialSurface
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsActionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CelestialPrimary,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = CelestialOnSurface,
                    fontSize = 13.sp
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = CelestialSlateMuted,
                    fontSize = 11.sp
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = null,
            tint = CelestialSlateMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CelestialPrimary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = CelestialOnSurface,
                fontSize = 13.sp
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = CelestialPrimary,
                uncheckedThumbColor = CelestialSlateMuted,
                uncheckedTrackColor = CelestialSurfaceContainerLow
            )
        )
    }
}
