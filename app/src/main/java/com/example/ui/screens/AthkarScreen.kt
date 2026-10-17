package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AthkarProgressEntity
import com.example.data.model.AthkarCategory
import com.example.data.model.AthkarItem
import com.example.data.repository.AthkarOfflineDataSource
import com.example.data.repository.IslamicData
import com.example.ui.theme.CelestialBackground
import com.example.ui.theme.CelestialBorderHairline
import com.example.ui.theme.CelestialEmerald
import com.example.ui.theme.CelestialOnSurface
import com.example.ui.theme.CelestialPrimaryBright
import com.example.ui.theme.CelestialPrimaryGradientEnd
import com.example.ui.theme.CelestialSlateDeep
import com.example.ui.theme.CelestialSlateMuted
import com.example.ui.theme.CelestialSurface
import com.example.ui.theme.QuranVerseTextStyle

@Composable
fun AthkarScreen(
    isArabic: Boolean,
    selectedCategory: AthkarCategory,
    athkarProgressMap: Map<String, AthkarProgressEntity>,
    onSelectCategory: (AthkarCategory) -> Unit,
    onIncrementAthkar: (athkarId: String, currentCount: Int, targetCount: Int) -> Unit,
    onResetAthkar: (athkarId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val allAthkar = androidx.compose.runtime.remember { AthkarOfflineDataSource.getAthkar(context) }
    val categoryItems = androidx.compose.runtime.remember(allAthkar, selectedCategory) {
        allAthkar.filter { it.category == selectedCategory }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CelestialBackground)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Column {
            Text(
                text = if (isArabic) "الأَذْكَارُ وَالأَدْعِيَةُ اليَوْمِيَّة" else "Daily Athkar & Duas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = CelestialOnSurface
            )
            Text(
                text = if (isArabic) "حِصْنُ المُسْلِمِ وَسَكِينَةُ القَلْب" else "Fortress of the Believer & Serenity",
                style = MaterialTheme.typography.labelSmall,
                color = CelestialSlateMuted
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Categories Segmented Pills Horizontal Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(AthkarCategory.values()) { category ->
                val isSelected = category == selectedCategory
                Surface(
                    shape = RoundedCornerShape(9999.dp),
                    color = if (isSelected) CelestialPrimaryBright else CelestialSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) CelestialPrimaryBright else CelestialBorderHairline
                    ),
                    shadowElevation = if (isSelected) 3.dp else 1.dp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(9999.dp))
                        .clickable { onSelectCategory(category) }
                        .testTag("athkar_cat_${category.name.lowercase()}")
                ) {
                    Text(
                        text = if (isArabic) category.titleArabic else category.titleEnglish,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else CelestialOnSurface,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Athkar Cards List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            items(categoryItems, key = { it.id }) { athkar ->
                val progress = athkarProgressMap[athkar.id]
                val currentCount = progress?.currentCount ?: 0
                val isCompleted = currentCount >= athkar.repeatTarget

                AthkarCard(
                    isArabic = isArabic,
                    athkar = athkar,
                    currentCount = currentCount,
                    isCompleted = isCompleted,
                    onTap = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onIncrementAthkar(athkar.id, currentCount, athkar.repeatTarget)
                    },
                    onReset = {
                        onResetAthkar(athkar.id)
                    }
                )
            }
        }
    }
}

@Composable
fun AthkarCard(
    isArabic: Boolean,
    athkar: AthkarItem,
    currentCount: Int,
    isCompleted: Boolean,
    onTap: () -> Unit,
    onReset: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isCompleted) CelestialEmerald else CelestialBorderHairline,
        label = "borderColor"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isCompleted) 4.dp else 2.dp, RoundedCornerShape(22.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onTap)
            .testTag("athkar_card_${athkar.id}"),
        color = CelestialSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Top Row: Athkar Title, Repetition Target & Counter Bubble
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isArabic) athkar.titleArabic else athkar.titleEnglish,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isCompleted) CelestialEmerald else CelestialOnSurface
                    )
                    Text(
                        text = if (isArabic) "التِّكْرَار: ${athkar.repeatTarget} مَرَّات" else "Repeat: ${athkar.repeatTarget}x",
                        style = MaterialTheme.typography.labelSmall,
                        color = CelestialSlateMuted
                    )
                }

                // Interactive Tap Target Pill / Circle
                Surface(
                    shape = RoundedCornerShape(9999.dp),
                    color = if (isCompleted) CelestialEmerald else CelestialPrimaryBright,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(9999.dp))
                        .clickable(onClick = onTap)
                        .testTag("athkar_count_btn_${athkar.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Done",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isArabic) "اكْتَمَل" else "Done",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "$currentCount / ${athkar.repeatTarget}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Arabic Text with Harakat
            Text(
                text = athkar.arabicText,
                style = QuranVerseTextStyle.copy(fontSize = 19.sp, lineHeight = 36.sp),
                color = CelestialSlateDeep,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Transliteration
            Text(
                text = athkar.transliteration,
                style = MaterialTheme.typography.bodySmall,
                color = CelestialPrimaryBright,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            // English Meaning
            Text(
                text = athkar.translationEnglish,
                style = MaterialTheme.typography.bodySmall,
                color = CelestialSlateMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Virtue footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        CelestialBackground,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✨ ${athkar.virtue}",
                    style = MaterialTheme.typography.labelSmall,
                    color = CelestialSlateMuted,
                    modifier = Modifier.weight(1f)
                )

                if (currentCount > 0) {
                    IconButton(
                        onClick = onReset,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Count",
                            tint = CelestialSlateMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
