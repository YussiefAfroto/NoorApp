package com.example.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CityPrayerPreset
import com.example.data.model.PrayerTime
import com.example.ui.theme.CelestialBackground
import com.example.ui.theme.CelestialBorderHairline
import com.example.ui.theme.CelestialEmerald
import com.example.ui.theme.CelestialOnSurface
import com.example.ui.theme.CelestialPrimary
import com.example.ui.theme.CelestialPrimaryBright
import com.example.ui.theme.CelestialPrimaryGradientEnd
import com.example.ui.theme.CelestialSlateDeep
import com.example.ui.theme.CelestialSlateMuted
import com.example.ui.theme.CelestialSurface
import com.example.ui.theme.CelestialSurfaceContainerLow

@Composable
fun PrayerTimesScreen(
    isArabic: Boolean,
    selectedCity: CityPrayerPreset,
    countdown: String,
    prayerTimes: List<PrayerTime> = emptyList(),
    nextPrayerName: String = "",
    onNavigateToQibla: () -> Unit,
    onOpenCityPicker: () -> Unit,
    onTogglePrayerCompleted: (String) -> Unit,
    calculationMethodTitle: String = "الهيئة المصرية العامة للمساحة",
    imsakTime: String = "",
    lastThirdTime: String = "",
    hijriDateText: String = "",
    userLocationDisplayName: String? = null,
    onDateChanged: (java.time.LocalDate) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var dayOffset by remember { mutableIntStateOf(0) }

    val targetDate = remember(dayOffset) {
        java.time.LocalDate.now().plusDays(dayOffset.toLong())
    }

    androidx.compose.runtime.LaunchedEffect(targetDate) {
        onDateChanged(targetDate)
    }

    val dateInfo = remember(targetDate) {
        com.example.util.DateProvider.getCurrentDateInfo(targetDate)
    }

    // Dynamic prayer times for targetDate & location
    val activeTimes = remember(targetDate, selectedCity, prayerTimes, dayOffset) {
        if (prayerTimes.isNotEmpty()) {
            prayerTimes
        } else {
            val calc = com.example.util.PrayerTimeCalculator.calculatePrayerTimes(
                date = targetDate,
                latitude = selectedCity.latitude,
                longitude = selectedCity.longitude
            )
            fun parseHourMinute(millis: Long): Pair<Int, Int> {
                val c = java.util.Calendar.getInstance().apply { timeInMillis = millis }
                return c.get(java.util.Calendar.HOUR_OF_DAY) to c.get(java.util.Calendar.MINUTE)
            }
            val (fajrH, fajrM) = parseHourMinute(calc.fajrMillis)
            val (sunriseH, sunriseM) = parseHourMinute(calc.sunriseMillis)
            val (dhuhrH, dhuhrM) = parseHourMinute(calc.dhuhrMillis)
            val (asrH, asrM) = parseHourMinute(calc.asrMillis)
            val (maghribH, maghribM) = parseHourMinute(calc.maghribMillis)
            val (ishaH, ishaM) = parseHourMinute(calc.ishaMillis)

            listOf(
                PrayerTime("fajr", "الفجر", "Fajr", calc.fajr, fajrH, fajrM),
                PrayerTime("sunrise", "الشروق", "Sunrise", calc.sunrise, sunriseH, sunriseM),
                PrayerTime("dhuhr", "الظهر", "Dhuhr", calc.dhuhr, dhuhrH, dhuhrM),
                PrayerTime("asr", "العصر", "Asr", calc.asr, asrH, asrM),
                PrayerTime("maghrib", "المغرب", "Maghrib", calc.maghrib, maghribH, maghribM),
                PrayerTime("isha", "العشاء", "Isha", calc.isha, ishaH, ishaM)
            )
        }
    }

    val (calcLastThird, calcImsak) = remember(targetDate, selectedCity) {
        com.example.util.PrayerTimeCalculator.calculateExtraNightTimes(
            date = targetDate,
            latitude = selectedCity.latitude,
            longitude = selectedCity.longitude
        )
    }

    val displayImsak = imsakTime.ifEmpty { calcImsak }
    val displayLastThird = lastThirdTime.ifEmpty { calcLastThird }

    val fajrItem = activeTimes.find { it.key == "fajr" }
    val sunriseItem = activeTimes.find { it.key == "sunrise" }
    val dhuhrItem = activeTimes.find { it.key == "dhuhr" }
    val asrItem = activeTimes.find { it.key == "asr" }
    val maghribItem = activeTimes.find { it.key == "maghrib" }
    val ishaItem = activeTimes.find { it.key == "isha" }

    val currentNext = activeTimes.firstOrNull { it.isNext } ?: dhuhrItem ?: activeTimes.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CelestialBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Top Location Bar with Umm Al-Qura calculation method
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onOpenCityPicker)
                    .border(1.dp, CelestialBorderHairline, RoundedCornerShape(16.dp))
                    .shadow(1.dp, RoundedCornerShape(16.dp)),
                color = CelestialSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CelestialSurfaceContainerLow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = CelestialPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = userLocationDisplayName ?: "${selectedCity.cityNameArabic}، ${selectedCity.country}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CelestialOnSurface,
                                fontSize = 14.sp
                            )
                            Text(
                                text = calculationMethodTitle,
                                style = MaterialTheme.typography.labelSmall,
                                color = CelestialSlateMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "تغيير",
                            color = CelestialPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = CelestialPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // 2. Hijri / Gregorian Date Navigator
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, CelestialBorderHairline, RoundedCornerShape(16.dp)),
                color = CelestialSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { dayOffset-- }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "اليوم السابق",
                            tint = CelestialSlateMuted
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = if (dayOffset == 0) "اليوم، ${dateInfo.hijriFormatted}" else dateInfo.hijriFormatted,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CelestialOnSurface,
                                fontSize = 14.sp
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CelestialEmerald.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = dateInfo.dayNameArabic,
                                    color = CelestialEmerald,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = "${dateInfo.gregorianFormatted} م",
                            style = MaterialTheme.typography.labelSmall,
                            color = CelestialSlateMuted,
                            fontSize = 11.sp
                        )
                    }

                    IconButton(onClick = { dayOffset++ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "اليوم التالي",
                            tint = CelestialSlateMuted
                        )
                    }
                }
            }
        }

        // 3. Hero Prayer Card (صلاة الظهر • الرياض • أذان الحرم المكي • بوصلة القبلة)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = CelestialPrimary.copy(alpha = 0.25f)),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    CelestialPrimaryBright,
                                    CelestialPrimaryGradientEnd,
                                    CelestialPrimary
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Top row: Adhan time & Next Prayer badge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "أذان ${currentNext?.nameArabic ?: "الظهر"} • ${currentNext?.time ?: ""}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(9999.dp))
                                    .background(Color.White.copy(alpha = 0.22f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (currentNext?.isNext == true) "الصلاة القادمة" else "الصلاة التالية",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Large Prayer Title
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "صلاة ${currentNext?.nameArabic ?: "الظهر"}",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 34.sp
                            )
                            Text(
                                text = "${selectedCity.cityNameArabic}، أذان الحرم المكي الشريف",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Countdown Pill
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(9999.dp))
                                    .background(Color.Black.copy(alpha = 0.18f))
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.HourglassBottom,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = if (dayOffset == 0) "الوقت المتبقي للأذان: $countdown" else "موعد الأذان: ${currentNext?.time ?: ""}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Bottom Qibla Navigation Pill Button
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable(onClick = onNavigateToQibla)
                                .testTag("qibla_compass_banner_button"),
                            color = Color.White,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Explore,
                                    contentDescription = null,
                                    tint = CelestialPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "بوصلة القبلة • ${selectedCity.qiblaAngleDegrees.toInt()}° • مكة المكرمة",
                                    color = CelestialPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. "مواقيت الصلوات الخمس" Section
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "مواقيت الصلوات الخمس",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CelestialOnSurface,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${dateInfo.dayNameArabic}، ${dateInfo.hijriFormatted}",
                        style = MaterialTheme.typography.labelSmall,
                        color = CelestialSlateMuted,
                        fontSize = 12.sp
                    )
                }

                // 6 Prayers List
                // 1. الفجر
                PrayerRowCard(
                    prayerName = "الفجر",
                    description = "أذان الفجر",
                    time = fajrItem?.time ?: "--:--",
                    badge = if (fajrItem?.isNext == true) "القادمة" else if (fajrItem?.isCompleted == true) "أُدِّيَت" else null,
                    badgeColor = if (fajrItem?.isNext == true) CelestialPrimary else CelestialSlateMuted,
                    icon = Icons.Default.Bedtime,
                    isHighlighted = fajrItem?.isNext ?: false,
                    isPassed = fajrItem?.isCompleted ?: false,
                    onToggle = { onTogglePrayerCompleted("fajr") }
                )

                // 2. الشروق
                PrayerRowCard(
                    prayerName = "الشروق",
                    description = "شروق الشمس",
                    time = sunriseItem?.time ?: "--:--",
                    badge = null,
                    badgeColor = null,
                    icon = Icons.Default.WbTwilight,
                    isHighlighted = false,
                    isPassed = false
                )

                // 3. الظهر
                PrayerRowCard(
                    prayerName = "الظهر",
                    description = if (dhuhrItem?.isNext == true) "الأذان قريباً" else "وقت صلاة الظهر",
                    time = dhuhrItem?.time ?: "--:--",
                    badge = if (dhuhrItem?.isNext == true) "القادمة" else if (dhuhrItem?.isCompleted == true) "أُدِّيَت" else null,
                    badgeColor = if (dhuhrItem?.isNext == true) CelestialPrimary else CelestialEmerald,
                    icon = Icons.Default.WbSunny,
                    isHighlighted = dhuhrItem?.isNext ?: false,
                    isPassed = dhuhrItem?.isCompleted ?: false,
                    onToggle = { onTogglePrayerCompleted("dhuhr") }
                )

                // 4. العصر
                PrayerRowCard(
                    prayerName = "العصر",
                    description = "الصلاة الوسطى",
                    time = asrItem?.time ?: "--:--",
                    badge = if (asrItem?.isNext == true) "القادمة" else if (asrItem?.isCompleted == true) "أُدِّيَت" else null,
                    badgeColor = if (asrItem?.isNext == true) CelestialPrimary else CelestialEmerald,
                    icon = Icons.Outlined.WbSunny,
                    isHighlighted = asrItem?.isNext ?: false,
                    isPassed = asrItem?.isCompleted ?: false,
                    onToggle = { onTogglePrayerCompleted("asr") }
                )

                // 5. المغرب
                PrayerRowCard(
                    prayerName = "المغرب",
                    description = "غروب قرص الشمس • موعد الإفطار",
                    time = maghribItem?.time ?: "--:--",
                    badge = if (maghribItem?.isNext == true) "القادمة" else if (maghribItem?.isCompleted == true) "أُدِّيَت" else "موعد الإفطار",
                    badgeColor = if (maghribItem?.isNext == true) CelestialPrimary else Color(0xFFF59E0B),
                    icon = Icons.Default.WbTwilight,
                    isHighlighted = maghribItem?.isNext ?: false,
                    isPassed = maghribItem?.isCompleted ?: false,
                    onToggle = { onTogglePrayerCompleted("maghrib") }
                )

                // 6. العشاء
                PrayerRowCard(
                    prayerName = "العشاء",
                    description = "صلاة العشاء والوتر",
                    time = ishaItem?.time ?: "--:--",
                    badge = if (ishaItem?.isNext == true) "القادمة" else if (ishaItem?.isCompleted == true) "أُدِّيَت" else null,
                    badgeColor = if (ishaItem?.isNext == true) CelestialPrimary else CelestialEmerald,
                    icon = Icons.Default.DarkMode,
                    isHighlighted = ishaItem?.isNext ?: false,
                    isPassed = ishaItem?.isCompleted ?: false,
                    onToggle = { onTogglePrayerCompleted("isha") }
                )
            }
        }

        // 5. Night Prayers & Imsak Grid (ثلث الليل الأخير & وقت الإمساك)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // ثلث الليل الأخير
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, CelestialBorderHairline, RoundedCornerShape(16.dp)),
                    color = CelestialSurface
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DarkMode,
                                contentDescription = null,
                                tint = Color(0xFF6366F1),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "ثلث الليل الأخير",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = CelestialOnSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = displayLastThird,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CelestialPrimary,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "أفضل أوقات استجابة الدعاء والقيام",
                            style = MaterialTheme.typography.labelSmall,
                            color = CelestialSlateMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                // وقت الإمساك
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, CelestialBorderHairline, RoundedCornerShape(16.dp)),
                    color = CelestialSurface
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bedtime,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "وقت الإمساك",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = CelestialOnSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = displayImsak,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CelestialPrimary,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "قبل أذان الفجر بـ 10 دقائق احتياطاً",
                            style = MaterialTheme.typography.labelSmall,
                            color = CelestialSlateMuted,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // 6. Azan Voice Selector Bar (بصوت الشيخ علي ملا - الحرم المكي)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, CelestialBorderHairline, RoundedCornerShape(16.dp)),
                color = CelestialSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CelestialSurfaceContainerLow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = CelestialPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "صوت الأذان والتنبيه",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CelestialOnSurface,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "بصوت الشيخ علي ملا (الحرم المكي)",
                                style = MaterialTheme.typography.labelSmall,
                                color = CelestialSlateMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "تغيير",
                            color = CelestialPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = CelestialPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PrayerRowCard(
    prayerName: String,
    description: String,
    time: String,
    badge: String?,
    badgeColor: Color?,
    icon: ImageVector,
    isHighlighted: Boolean,
    isPassed: Boolean,
    onToggle: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = onToggle != null, onClick = { onToggle?.invoke() })
            .border(
                width = if (isHighlighted) 1.5.dp else 1.dp,
                color = if (isHighlighted) CelestialPrimary else CelestialBorderHairline,
                shape = RoundedCornerShape(16.dp)
            )
            .shadow(if (isHighlighted) 4.dp else 1.dp, RoundedCornerShape(16.dp)),
        color = if (isHighlighted) CelestialSurfaceContainerLow.copy(alpha = 0.4f) else CelestialSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Right Side: Icon + Name & Description + Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            if (isHighlighted) CelestialPrimary else CelestialSurfaceContainerLow
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = prayerName,
                        tint = if (isHighlighted) Color.White else CelestialPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = prayerName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isHighlighted) CelestialPrimary else CelestialOnSurface,
                            fontSize = 15.sp
                        )
                        if (badge != null && badgeColor != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(badgeColor.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = badge,
                                    color = badgeColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Text(
                        text = description,
                        style = MaterialTheme.typography.labelSmall,
                        color = CelestialSlateMuted,
                        fontSize = 11.sp
                    )
                }
            }

            // Left Side: Time + Action button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isHighlighted) CelestialPrimary else CelestialOnSurface,
                    fontSize = 15.sp
                )

                if (isHighlighted) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CelestialPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "تنبيه الأذان",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CelestialSurfaceContainerLow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPassed) Icons.Default.NotificationsNone else Icons.Default.Notifications,
                            contentDescription = "تنبيه",
                            tint = if (isPassed) CelestialSlateMuted.copy(alpha = 0.6f) else CelestialSlateMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
