package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TasbihEntity
import com.example.data.repository.IslamicData
import com.example.ui.theme.CelestialBackground
import com.example.ui.theme.CelestialBorderHairline
import com.example.ui.theme.CelestialEmerald
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.CelestialOnSurface
import com.example.ui.theme.CelestialPrimaryBright
import com.example.ui.theme.CelestialPrimaryGradientEnd
import com.example.ui.theme.CelestialSky
import com.example.ui.theme.CelestialSlateDeep
import com.example.ui.theme.CelestialSlateMuted
import com.example.ui.theme.CelestialSurface
import kotlinx.coroutines.launch

@Composable
fun TasbihScreen(
    isArabic: Boolean,
    tasbihList: List<TasbihEntity>,
    activeTasbihId: String,
    hapticEnabled: Boolean,
    onSelectTasbih: (String) -> Unit,
    onIncrement: (Int) -> Unit,
    onReset: () -> Unit,
    onUpdateTarget: (Int) -> Unit,
    onToggleHaptic: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    val activeTasbih = tasbihList.firstOrNull { it.id == activeTasbihId }
        ?: tasbihList.firstOrNull()
        ?: TasbihEntity(
            id = "subhanallah",
            arabicText = "سُبْحَانَ اللَّهِ",
            transliteration = "SubhanAllah",
            englishMeaning = "Glory be to Allah",
            count = 0,
            target = 33,
            totalLifetimeCount = 0
        )

    val currentCount = activeTasbih.count
    val targetCount = activeTasbih.target
    val progress = if (targetCount > 0) (currentCount.toFloat() / targetCount.toFloat()).coerceIn(0f, 1f) else 0f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CelestialBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Title & Controls Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isArabic) "المِسْبَحَةُ الإِلِكْتُرُونِيَّة" else "Interactive Tasbih",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = CelestialOnSurface
                )
                Text(
                    text = if (isArabic) "اذْكُرِ اللَّهَ يَذْكُرْك" else "Remember Allah, and He will remember you",
                    style = MaterialTheme.typography.labelSmall,
                    color = CelestialSlateMuted
                )
            }

            // Haptic feedback toggle button
            Surface(
                shape = CircleShape,
                color = if (hapticEnabled) CelestialPrimaryBright.copy(alpha = 0.12f) else CelestialSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onToggleHaptic)
                    .testTag("tasbih_haptic_toggle")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Vibration,
                        contentDescription = "Haptic Vibration",
                        tint = if (hapticEnabled) CelestialPrimaryBright else CelestialSlateMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Preset Chips Horizontal Row
        Text(
            text = if (isArabic) "الصِّيَغُ والأَدْعِيَةُ المَأْثُورَة" else "Select Dhikr",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = CelestialSlateMuted,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(IslamicData.dhikrPresets) { preset ->
                val isSelected = preset.id == activeTasbih.id
                Surface(
                    shape = RoundedCornerShape(9999.dp),
                    color = if (isSelected) CelestialPrimaryBright else CelestialSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) CelestialPrimaryBright else CelestialBorderHairline
                    ),
                    shadowElevation = if (isSelected) 4.dp else 1.dp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(9999.dp))
                        .clickable { onSelectTasbih(preset.id) }
                        .testTag("dhikr_preset_${preset.id}")
                ) {
                    Text(
                        text = if (isArabic) preset.arabicText else preset.transliteration,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else CelestialOnSurface,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Active Dhikr Details Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = CelestialSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline),
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = activeTasbih.arabicText,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = CelestialSlateDeep,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = activeTasbih.transliteration,
                    style = MaterialTheme.typography.labelMedium,
                    color = CelestialPrimaryBright,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = activeTasbih.englishMeaning,
                    style = MaterialTheme.typography.bodySmall,
                    color = CelestialSlateMuted,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Generously Sized Luminous Interactive Touch Target (220dp)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(240.dp)
        ) {
            // Ambient Outer Glow Ring
            Box(
                modifier = Modifier
                    .size(236.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                CelestialPrimaryBright.copy(alpha = 0.20f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Progress Arc Indicator
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(228.dp),
                strokeWidth = 6.dp,
                color = CelestialPrimaryBright,
                trackColor = CelestialPrimaryBright.copy(alpha = 0.12f)
            )

            // Central Touch Target Button
            Surface(
                modifier = Modifier
                    .size(196.dp)
                    .scale(scale.value)
                    .clip(CircleShape)
                    .shadow(
                        elevation = 14.dp,
                        shape = CircleShape,
                        spotColor = CelestialPrimaryBright.copy(alpha = 0.35f)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, color = Color.White),
                        onClick = {
                            if (hapticEnabled) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                            coroutineScope.launch {
                                scale.animateTo(0.94f, spring(stiffness = Spring.StiffnessMedium))
                                scale.animateTo(1.0f, spring(stiffness = Spring.StiffnessLow))
                            }
                            onIncrement(currentCount)
                        }
                    )
                    .testTag("tasbih_counter_button"),
                shape = CircleShape,
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(CelestialPrimaryBright, CelestialPrimaryGradientEnd)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "$currentCount",
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 54.sp),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = if (isArabic) "مِن $targetCount" else "of $targetCount",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isArabic) "المِسْ سَبِّح" else "Tap to count",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.70f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Target Preset Buttons (33, 99, 100) & Reset Action
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Target Selector Pills
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(33, 99, 100).forEach { targetOption ->
                    val isCurrentTarget = activeTasbih.target == targetOption
                    Surface(
                        shape = RoundedCornerShape(9999.dp),
                        color = if (isCurrentTarget) CelestialPrimaryBright.copy(alpha = 0.15f) else CelestialSurface,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isCurrentTarget) CelestialPrimaryBright else CelestialBorderHairline
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .clickable { onUpdateTarget(targetOption) }
                            .testTag("target_option_$targetOption")
                    ) {
                        Text(
                            text = "$targetOption",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isCurrentTarget) FontWeight.Bold else FontWeight.Medium,
                            color = if (isCurrentTarget) CelestialPrimaryBright else CelestialOnSurface,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Reset Button
            Surface(
                shape = RoundedCornerShape(9999.dp),
                color = CelestialSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline),
                modifier = Modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .clickable(onClick = onReset)
                    .testTag("tasbih_reset_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        tint = CelestialSlateMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isArabic) "إعادة ضبط" else "Reset",
                        style = MaterialTheme.typography.labelMedium,
                        color = CelestialSlateMuted,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Lifetime stats summary card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = CelestialSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${activeTasbih.totalLifetimeCount}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CelestialPrimaryBright
                    )
                    Text(
                        text = if (isArabic) "إِجْمَالِي التَّسْبِيحَات" else "Total Lifetime",
                        style = MaterialTheme.typography.labelSmall,
                        color = CelestialSlateMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .width(1.dp)
                        .background(CelestialBorderHairline)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val cycles = if (activeTasbih.target > 0) activeTasbih.count / activeTasbih.target else 0
                    Text(
                        text = "$cycles",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CelestialEmerald
                    )
                    Text(
                        text = if (isArabic) "الدَّوْرَاتُ المُكْتَمِلَة" else "Completed Cycles",
                        style = MaterialTheme.typography.labelSmall,
                        color = CelestialSlateMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
