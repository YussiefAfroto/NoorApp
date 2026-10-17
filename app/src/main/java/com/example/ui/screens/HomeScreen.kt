package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AthkarCategory
import com.example.data.model.Ayah
import com.example.data.model.PrayerTime
import com.example.data.repository.IslamicData
import com.example.ui.theme.CelestialBackground
import com.example.ui.theme.CelestialBorderHairline
import com.example.ui.theme.CelestialEmerald
import com.example.ui.theme.CelestialOnSurface
import com.example.ui.theme.CelestialPrimary
import com.example.ui.theme.CelestialPrimaryBright
import com.example.ui.theme.CelestialPrimaryGradientEnd
import com.example.ui.theme.CelestialSky
import com.example.ui.theme.CelestialSlateDeep
import com.example.ui.theme.CelestialSlateMuted
import com.example.ui.theme.CelestialSurface
import com.example.ui.theme.CelestialSurfaceContainerLow
import com.example.ui.theme.QuranVerseTextStyle

@Composable
fun HomeScreen(
    isArabic: Boolean,
    prayerTimes: List<PrayerTime>,
    nextPrayerName: String,
    countdown: String,
    completedPrayersCount: Int,
    lastReadSurahNumber: Int = 18,
    lastReadAyahNumber: Int = 46,
    onNavigateToQuran: () -> Unit,
    onNavigateToPrayers: () -> Unit,
    onNavigateToHadith: () -> Unit,
    onNavigateToAzkar: (AthkarCategory) -> Unit,
    onOpenTasbih: () -> Unit,
    dateInfo: com.example.util.DateProvider.FormattedDateInfo = com.example.util.DateProvider.getCurrentDateInfo(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CelestialBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Personalized Greeting & Hijri/Gregorian Date
        item {
            HomeGreetingCard(dateInfo = dateInfo)
        }

        // 2. Hero Prayer Times Card (Gradient Blue with Countdown & 6 Times bar)
        item {
            HomeHeroPrayerCard(
                nextPrayerName = nextPrayerName,
                countdown = countdown,
                prayerTimes = prayerTimes,
                onCardClick = onNavigateToPrayers
            )
        }

        // 3. Ayah of the Day Card (نور الآيات • آية اليوم)
        item {
            HomeAyahOfTheDayCard(
                onShare = { text ->
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, text)
                    }
                    context.startActivity(Intent.createChooser(intent, "مشاركة الآية"))
                }
            )
        }

        // 5. Islamic Services Section (الخدمات الإسلامية • الوصول السريع)
        item {
            HomeIslamicServicesSection(
                isArabic = isArabic,
                lastReadSurahNumber = lastReadSurahNumber,
                lastReadAyahNumber = lastReadAyahNumber,
                onNavigateToQuran = onNavigateToQuran,
                onNavigateToHadith = onNavigateToHadith,
                onNavigateToAzkar = onNavigateToAzkar,
                onOpenTasbih = onOpenTasbih
            )
        }
    }
}

// -------------------------------------------------------------
// Greeting Card
// -------------------------------------------------------------
@Composable
private fun HomeGreetingCard(
    dateInfo: com.example.util.DateProvider.FormattedDateInfo
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Right side (RTL): Mosque icon + Welcome greeting + User Name
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
                    imageVector = Icons.Default.Mosque,
                    contentDescription = null,
                    tint = CelestialPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column {
                Text(
                    text = "السلام عليكم ورحمة الله وبركاته",
                    style = MaterialTheme.typography.labelSmall,
                    color = CelestialSlateMuted,
                    fontSize = 11.sp
                )
                Text(
                    text = "طاب يومُكَ بنور الإيمان",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CelestialOnSurface,
                    fontSize = 15.sp
                )
            }
        }

        // Left side (RTL): Hijri & Gregorian Date Box
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = CelestialSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline),
            shadowElevation = 0.5.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${dateInfo.dayNameArabic}، ${dateInfo.hijriFormatted}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = CelestialPrimary,
                    fontSize = 11.sp
                )
                Text(
                    text = "${dateInfo.gregorianFormatted} م",
                    style = MaterialTheme.typography.labelSmall,
                    color = CelestialSlateMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// Hero Prayer Card
// -------------------------------------------------------------
@Composable
private fun HomeHeroPrayerCard(
    nextPrayerName: String,
    countdown: String,
    prayerTimes: List<PrayerTime>,
    onCardClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onCardClick)
            .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = CelestialPrimary.copy(alpha = 0.25f))
            .testTag("home_hero_prayer_card"),
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
                // Top row: Next Prayer Badge & Countdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Next Prayer pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "الصلاة القادمة • ${if (nextPrayerName.isNotEmpty()) nextPrayerName else "صلاة الظُّهْر"}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    // Remaining time with live dot
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(Color.Black.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4ADE80)) // emerald dot
                        )
                        Text(
                            text = "متبقي $countdown",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val nextPrayer = prayerTimes.firstOrNull { it.isNext } ?: prayerTimes.firstOrNull()

                // Huge Time Display & Azan Voice
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = nextPrayer?.timeFormatted ?: "--:--",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 38.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(Color.White.copy(alpha = 0.18f))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = if (nextPrayer != null) "موعد أذان ${nextPrayer.nameArabic}" else "مواقيت الصلاة",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 6 Prayer slots horizontally inside card - dynamically bound
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .padding(horizontal = 6.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val prayerIcons = mapOf(
                        "fajr" to Icons.Default.Bedtime,
                        "sunrise" to Icons.Default.WbTwilight,
                        "dhuhr" to Icons.Default.WbSunny,
                        "asr" to Icons.Outlined.WbSunny,
                        "maghrib" to Icons.Default.WbTwilight,
                        "isha" to Icons.Default.DarkMode
                    )

                    if (prayerTimes.isNotEmpty()) {
                        prayerTimes.forEach { prayer ->
                            val isHighlighted = prayer.isNext
                            val icon = prayerIcons[prayer.key] ?: Icons.Default.WbSunny
                            val displayTime = prayer.timeFormatted.replace(" ص", "").replace(" م", "")

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isHighlighted) Color.White else Color.Transparent
                                    )
                                    .padding(horizontal = if (isHighlighted) 8.dp else 4.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = prayer.nameArabic,
                                    tint = if (isHighlighted) CelestialPrimary else Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = prayer.nameArabic,
                                    fontSize = 10.sp,
                                    fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isHighlighted) CelestialPrimary else Color.White.copy(alpha = 0.85f),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                Text(
                                    text = displayTime,
                                    fontSize = 10.sp,
                                    fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isHighlighted) CelestialPrimary else Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Ayah of the Day Card
// -------------------------------------------------------------
@Composable
private fun HomeAyahOfTheDayCard(
    onShare: (String) -> Unit
) {
    val ayahText = "اللَّهُ نُورُ السَّمَاوَاتِ وَالْأَرْضِ"
    val fullText = "﴿ اللَّهُ نُورُ السَّمَاوَاتِ وَالْأَرْضِ ﴾ — [سورة النور: 35]"

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, CelestialBorderHairline, RoundedCornerShape(20.dp))
            .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = CelestialPrimary.copy(alpha = 0.05f))
            .testTag("home_ayah_card"),
        color = CelestialSurface,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: Title + Share & Audio Icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CelestialSurfaceContainerLow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = CelestialPrimaryBright,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "نور الآيات • آية اليوم",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CelestialOnSurface,
                        fontSize = 14.sp
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { onShare(fullText) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "مشاركة الآية",
                            tint = CelestialSlateMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Verse container with subtle gradient border
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CelestialSurfaceContainerLow.copy(alpha = 0.6f))
                    .border(1.dp, CelestialBorderHairline, RoundedCornerShape(16.dp))
                    .padding(vertical = 18.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "﴿ $ayahText ﴾",
                        style = QuranVerseTextStyle.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = CelestialPrimary,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "─── [سورة النور: 35] ───",
                        style = MaterialTheme.typography.labelSmall,
                        color = CelestialSlateMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Islamic Services Section (الخدمات الإسلامية • الوصول السريع)
// -------------------------------------------------------------
@Composable
private fun HomeIslamicServicesSection(
    isArabic: Boolean,
    lastReadSurahNumber: Int,
    lastReadAyahNumber: Int,
    onNavigateToQuran: () -> Unit,
    onNavigateToHadith: () -> Unit,
    onNavigateToAzkar: (AthkarCategory) -> Unit,
    onOpenTasbih: () -> Unit
) {
    val context = LocalContext.current
    val lastReadSurah = remember(lastReadSurahNumber) {
        com.example.data.repository.QuranOfflineDataSource.getSurahByNumber(context, lastReadSurahNumber)
    }
    val lastReadSurahName = if (isArabic) {
        lastReadSurah?.nameArabic ?: "الكهف"
    } else {
        lastReadSurah?.nameEnglish ?: "Al-Kahf"
    }
    val quranSubtitle = if (isArabic) {
        "سورة $lastReadSurahName • آية $lastReadAyahNumber"
    } else {
        "Surah $lastReadSurahName • Verse $lastReadAyahNumber"
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "الخدمات الإسلامية",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CelestialOnSurface,
                fontSize = 16.sp
            )
            Text(
                text = "الوصول السريع",
                style = MaterialTheme.typography.labelSmall,
                color = CelestialPrimary,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
        }

        // 1. Wide Card: القرآن الكريم (متابعة القراءة - سورة الكهف • آية 46)
        ServiceWideCard(
            title = "القرآن الكريم",
            badge = "متابعة القراءة",
            subtitle = quranSubtitle,
            icon = Icons.Default.MenuBook,
            iconTint = CelestialPrimary,
            iconBg = CelestialSurfaceContainerLow,
            onClick = onNavigateToQuran
        )

        // 2. Wide Card: أحاديث الرسول ﷺ (الصحيحان - رياض الصالحين والأربعين النووية)
        ServiceWideCard(
            title = "أحاديث الرسول ﷺ",
            badge = "الصحيحان",
            subtitle = "رياض الصالحين والأربعين النووية",
            icon = Icons.Default.LibraryBooks,
            iconTint = CelestialPrimary,
            iconBg = CelestialSurfaceContainerLow,
            onClick = onNavigateToHadith
        )

        // 3. 2x2 Grid of services:
        // - أذكار الصباح (بركة ويقين ليومك)
        // - أذكار المساء (سكينة وحفظ وأمان)
        // - جميع الأدعية (أدعية الكتاب والسنة)
        // - التسبيح (سُبْحَانَ اللَّهِ وَبِحَمْدِهِ)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ServiceGridCard(
                title = "أذكار الصباح",
                subtitle = "بركة ويقين ليومك",
                icon = Icons.Default.WbSunny,
                iconTint = Color(0xFFF59E0B),
                iconBg = Color(0xFFFEF3C7),
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToAzkar(AthkarCategory.MORNING) }
            )
            ServiceGridCard(
                title = "أذكار المساء",
                subtitle = "سكينة وحفظ وأمان",
                icon = Icons.Default.Bedtime,
                iconTint = Color(0xFF6366F1),
                iconBg = Color(0xFFEEF2FF),
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToAzkar(AthkarCategory.EVENING) }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ServiceGridCard(
                title = "جميع الأدعية",
                subtitle = "أدعية الكتاب والسنة",
                icon = Icons.Default.AutoAwesome,
                iconTint = Color(0xFF10B981),
                iconBg = Color(0xFFD1FAE5),
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToAzkar(AthkarCategory.QURANIC_DUA) }
            )
            ServiceGridCard(
                title = "التسبيح",
                subtitle = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
                icon = Icons.Default.CheckCircle,
                iconTint = CelestialPrimary,
                iconBg = CelestialSurfaceContainerLow,
                modifier = Modifier.weight(1f),
                onClick = onOpenTasbih
            )
        }

        // 4. Wide Card at bottom: أذكار متنوعة (الاستيقاظ، السفر، النوم، ودخول المنزل)
        ServiceWideCard(
            title = "أذكار متنوعة",
            badge = "جامع الذكر",
            subtitle = "الاستيقاظ، السفر، النوم، ودخول المنزل",
            icon = Icons.Default.Mosque,
            iconTint = Color(0xFF0D9488),
            iconBg = Color(0xFFCCFBF1),
            onClick = { onNavigateToAzkar(AthkarCategory.SLEEP) }
        )
    }
}

@Composable
private fun ServiceWideCard(
    title: String,
    badge: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .border(1.dp, CelestialBorderHairline, RoundedCornerShape(18.dp))
            .shadow(2.dp, RoundedCornerShape(18.dp), spotColor = CelestialPrimary.copy(alpha = 0.05f))
            .testTag("service_card_${title.lowercase().replace(" ", "_")}"),
        color = CelestialSurface,
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CelestialOnSurface,
                            fontSize = 15.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CelestialSurfaceContainerLow)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badge,
                                color = CelestialPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = CelestialSlateMuted,
                        fontSize = 12.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = CelestialSlateMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ServiceGridCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .border(1.dp, CelestialBorderHairline, RoundedCornerShape(18.dp))
            .shadow(2.dp, RoundedCornerShape(18.dp), spotColor = CelestialPrimary.copy(alpha = 0.05f))
            .testTag("service_grid_${title.lowercase().replace(" ", "_")}"),
        color = CelestialSurface,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = CelestialSlateMuted.copy(alpha = 0.7f),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CelestialOnSurface,
                fontSize = 14.sp
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = CelestialSlateMuted,
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}
