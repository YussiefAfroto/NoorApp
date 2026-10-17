package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CityPrayerPreset
import com.example.ui.theme.CelestialBackground
import com.example.ui.theme.CelestialBorderHairline
import com.example.ui.theme.CelestialOnSurface
import com.example.ui.theme.CelestialPrimary
import com.example.ui.theme.CelestialPrimaryBright
import com.example.ui.theme.CelestialSlateMuted
import com.example.ui.theme.CelestialSurface

@Composable
fun NoorHeader(
    isArabic: Boolean,
    selectedCity: CityPrayerPreset,
    onCityClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = CelestialBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Right Side (in RTL): Noor Official Logo + Title + City/Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onCityClick)
                    .testTag("noor_header_brand_location")
            ) {
                // Official Noor Logo with soft rounded corners
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, CelestialBorderHairline, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.noor_logo),
                        contentDescription = "شعار تطبيق نور الرسمي",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isArabic) "نُور" else "Noor",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CelestialOnSurface,
                        fontSize = 18.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "الموقع الحالي",
                            tint = CelestialPrimaryBright,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = if (isArabic) "${selectedCity.cityNameArabic}، ${selectedCity.country}" else "${selectedCity.cityNameEnglish}, ${selectedCity.country}",
                            style = MaterialTheme.typography.labelSmall,
                            color = CelestialSlateMuted,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                }
            }

            // Left Side (in RTL): Settings (Gear) Icon + Notifications (Bell) Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Notifications Icon Button
                Surface(
                    shape = CircleShape,
                    color = CelestialSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline),
                    shadowElevation = 0.5.dp,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onNotificationsClick)
                        .testTag("notifications_icon_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "التنبيهات والأذان",
                            tint = CelestialOnSurface,
                            modifier = Modifier.size(20.dp)
                        )
                        // Subtle indicator dot
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .align(Alignment.TopEnd)
                                .padding(top = 8.dp, end = 8.dp)
                                .clip(CircleShape)
                                .background(CelestialPrimaryBright)
                        )
                    }
                }

                // Settings (Gear) Icon Button
                Surface(
                    shape = CircleShape,
                    color = CelestialSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline),
                    shadowElevation = 0.5.dp,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onSettingsClick)
                        .testTag("settings_icon_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "الإعدادات العامة",
                            tint = CelestialPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
