package com.example.ui.components

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.QuranBookmarkEntity
import com.example.data.model.CityPrayerPreset
import com.example.data.repository.IslamicData
import com.example.ui.theme.CelestialBackground
import com.example.ui.theme.CelestialBorderHairline
import com.example.ui.theme.CelestialOnSurface
import com.example.ui.theme.CelestialPrimaryBright
import com.example.ui.theme.CelestialSlateMuted
import com.example.ui.theme.CelestialSurface
import com.example.ui.theme.QuranVerseTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityPickerSheet(
    isArabic: Boolean,
    currentCity: CityPrayerPreset,
    onSelectCity: (CityPrayerPreset) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CelestialSurface,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 44.dp, height = 5.dp)
                    .clip(RoundedCornerShape(9999.dp))
                    .background(CelestialBorderHairline)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isArabic) "اختر المدينة لحساب الصلاة والقبلة" else "Select City for Prayer & Qibla",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CelestialOnSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = CelestialSlateMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(IslamicData.cities) { city ->
                    val isSelected = city.cityNameEnglish == currentCity.cityNameEnglish
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                1.dp,
                                if (isSelected) CelestialPrimaryBright else CelestialBorderHairline,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onSelectCity(city) }
                            .testTag("city_item_${city.cityNameEnglish.lowercase()}"),
                        color = if (isSelected) CelestialPrimaryBright.copy(alpha = 0.08f) else CelestialBackground
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationCity,
                                    contentDescription = null,
                                    tint = if (isSelected) CelestialPrimaryBright else CelestialSlateMuted
                                )
                                Column {
                                    Text(
                                        text = if (isArabic) city.cityNameArabic else city.cityNameEnglish,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        color = CelestialOnSurface
                                    )
                                    Text(
                                        text = "${city.country} • ${if (isArabic) "القبلة" else "Qibla"}: ${city.qiblaDegree.toInt()}°",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CelestialSlateMuted
                                    )
                                }
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = CelestialPrimaryBright
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationMethodSheet(
    isArabic: Boolean,
    currentMethodId: Int,
    onSelectMethod: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val methods = listOf(
        Triple(5, "الهيئة المصرية العامة للمساحة", "Egyptian General Authority of Survey (EGAS)"),
        Triple(4, "تقويم أم القرى (مكة المكرمة)", "Umm Al-Qura University, Makkah"),
        Triple(3, "رابطة العالم الإسلامي", "Muslim World League (MWL)"),
        Triple(2, "الجمعية الإسلامية لأمريكا الشمالية (ISNA)", "Islamic Society of North America"),
        Triple(1, "جامعة العلوم الإسلامية بكراتشي", "University of Islamic Sciences, Karachi")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CelestialSurface,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 44.dp, height = 5.dp)
                    .clip(RoundedCornerShape(9999.dp))
                    .background(CelestialBorderHairline)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isArabic) "طريقة حساب مواقيت الصلاة" else "Prayer Calculation Method",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CelestialOnSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = CelestialSlateMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(methods) { (methodId, titleAr, titleEn) ->
                    val isSelected = methodId == currentMethodId
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                1.dp,
                                if (isSelected) CelestialPrimaryBright else CelestialBorderHairline,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onSelectMethod(methodId) },
                        color = if (isSelected) CelestialPrimaryBright.copy(alpha = 0.08f) else CelestialBackground
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isArabic) titleAr else titleEn,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CelestialOnSurface
                                )
                                Text(
                                    text = if (isArabic) titleEn else titleAr,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CelestialSlateMuted
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = CelestialPrimaryBright
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksSheet(
    isArabic: Boolean,
    bookmarks: List<QuranBookmarkEntity>,
    onDeleteBookmark: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CelestialSurface,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 44.dp, height = 5.dp)
                    .clip(RoundedCornerShape(9999.dp))
                    .background(CelestialBorderHairline)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isArabic) "الآيات المحفوظة (${bookmarks.size})" else "Saved Verses (${bookmarks.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CelestialOnSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = CelestialSlateMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (bookmarks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = CelestialSlateMuted.copy(alpha = 0.5f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isArabic) "لا توجد آيات محفوظة حالياً" else "No saved verses yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CelestialSlateMuted
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(bookmarks) { item ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, CelestialBorderHairline, RoundedCornerShape(16.dp)),
                            color = CelestialBackground
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${if (isArabic) item.surahNameArabic else item.surahNameEnglish} • ${if (isArabic) "الآية" else "Ayah"} ${item.ayahNumber}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = CelestialPrimaryBright
                                    )
                                    IconButton(
                                        onClick = { onDeleteBookmark(item.id) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = CelestialSlateMuted,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = item.arabicText,
                                    style = QuranVerseTextStyle.copy(fontSize = 18.sp, lineHeight = 34.sp),
                                    color = CelestialOnSurface
                                )

                                if (item.englishText.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = item.englishText,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CelestialSlateMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
