package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PrayerTime
import com.example.ui.theme.CelestialBorderHairline
import com.example.ui.theme.CelestialEmerald
import com.example.ui.theme.CelestialOnSurface
import com.example.ui.theme.CelestialPrimaryBright
import com.example.ui.theme.CelestialPrimaryGradientEnd
import com.example.ui.theme.CelestialSky
import com.example.ui.theme.CelestialSlateMuted
import com.example.ui.theme.CelestialSurface

@Composable
fun PrayerCard(
    prayer: PrayerTime,
    isArabic: Boolean,
    onToggleCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isNext = prayer.isNext
    val isCompleted = prayer.isCompleted

    val backgroundBrush = if (isNext) {
        Brush.linearGradient(
            colors = listOf(CelestialPrimaryBright, CelestialSky)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(CelestialSurface, CelestialSurface)
        )
    }

    val textColor = if (isNext) Color.White else CelestialOnSurface
    val subTextColor = if (isNext) Color.White.copy(alpha = 0.85f) else CelestialSlateMuted
    val borderColor = if (isNext) Color.Transparent else CelestialBorderHairline

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isNext) 10.dp else 2.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = if (isNext) CelestialPrimaryBright.copy(alpha = 0.35f) else Color.Black.copy(alpha = 0.04f)
            )
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable(onClick = onToggleCompleted)
            .testTag("prayer_card_${prayer.key}"),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundBrush)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading: Completion Checkbox & Prayer Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Checkbox Circle
                    val checkBg = if (isCompleted) {
                        if (isNext) Color.White else CelestialEmerald
                    } else {
                        if (isNext) Color.White.copy(alpha = 0.2f) else Color.Transparent
                    }
                    val checkBorder = if (isCompleted) Color.Transparent else {
                        if (isNext) Color.White.copy(alpha = 0.6f) else CelestialBorderHairline
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(checkBg)
                            .border(1.5.dp, checkBorder, CircleShape)
                            .clickable(onClick = onToggleCompleted),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = if (isNext) CelestialPrimaryBright else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (isArabic) prayer.nameArabic else prayer.nameEnglish,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )

                            if (isNext) {
                                Surface(
                                    shape = RoundedCornerShape(9999.dp),
                                    color = Color.White.copy(alpha = 0.25f)
                                ) {
                                    Text(
                                        text = if (isArabic) "القادمة" else "Next",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        if (prayer.key != "sunrise") {
                            Text(
                                text = if (isCompleted) {
                                    if (isArabic) "تمت الصلاة بفضل الله" else "Prayer performed"
                                } else {
                                    if (isArabic) "فرض مفروض" else "Obligatory prayer"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = subTextColor
                            )
                        } else {
                            Text(
                                text = if (isArabic) "شروق الشمس" else "Sunrise time",
                                style = MaterialTheme.typography.labelSmall,
                                color = subTextColor
                            )
                        }
                    }
                }

                // Trailing: Prayer Time & Status Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = prayer.timeFormatted,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )

                    if (isNext) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Active Adhan",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
