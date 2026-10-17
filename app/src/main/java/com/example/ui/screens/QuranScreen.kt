package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.QuranBookmarkEntity
import com.example.data.model.Ayah
import com.example.data.model.Surah
import com.example.data.repository.IslamicData
import com.example.data.repository.QuranSurahs114
import com.example.ui.theme.CelestialBackground
import com.example.ui.theme.CelestialBorderHairline
import com.example.ui.theme.CelestialEmerald
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.CelestialOnSurface
import com.example.ui.theme.CelestialPrimary
import com.example.ui.theme.CelestialPrimaryBright
import com.example.ui.theme.CelestialPrimaryGradientEnd
import com.example.ui.theme.CelestialSlateDeep
import com.example.ui.theme.CelestialSlateMuted
import com.example.ui.theme.CelestialSurface
import com.example.ui.theme.CelestialSurfaceContainerLow
import com.example.ui.theme.QuranVerseTextStyle

import androidx.compose.foundation.layout.statusBarsPadding

data class JuzData(
    val number: Int,
    val nameArabic: String,
    val startSurahName: String,
    val endSurahName: String,
    val surahNumbers: List<Int>
)

object QuranJuzData {
    val all30Juzs = listOf(
        JuzData(1, "الجزء الأول", "الفاتحة", "البقرة", listOf(1, 2)),
        JuzData(2, "الجزء الثاني", "البقرة", "البقرة", listOf(2)),
        JuzData(3, "الجزء الثالث", "البقرة", "آل عمران", listOf(2, 3)),
        JuzData(4, "الجزء الرابع", "آل عمران", "النساء", listOf(3, 4)),
        JuzData(5, "الجزء الخامس", "النساء", "النساء", listOf(4)),
        JuzData(6, "الجزء السادس", "النساء", "المائدة", listOf(4, 5)),
        JuzData(7, "الجزء السابع", "المائدة", "الأنعام", listOf(5, 6)),
        JuzData(8, "الجزء الثامن", "الأنعام", "الأعراف", listOf(6, 7)),
        JuzData(9, "الجزء التاسع", "الأعراف", "الأنفال", listOf(7, 8)),
        JuzData(10, "الجزء العاشر", "الأنفال", "التوبة", listOf(8, 9)),
        JuzData(11, "الجزء الحادي عشر", "التوبة", "هود", listOf(9, 10, 11)),
        JuzData(12, "الجزء الثاني عشر", "هود", "يوسف", listOf(11, 12)),
        JuzData(13, "الجزء الثالث عشر", "يوسف", "إبراهيم", listOf(12, 13, 14)),
        JuzData(14, "الجزء الرابع عشر", "الحجر", "النحل", listOf(15, 16)),
        JuzData(15, "الجزء الخامس عشر", "الإسراء", "الكهف", listOf(17, 18)),
        JuzData(16, "الجزء السادس عشر", "الكهف", "طه", listOf(18, 19, 20)),
        JuzData(17, "الجزء السابع عشر", "الأنبياء", "الحج", listOf(21, 22)),
        JuzData(18, "الجزء الثامن عشر", "المؤمنون", "الفرقان", listOf(23, 24, 25)),
        JuzData(19, "الجزء التاسع عشر", "الفرقان", "النمل", listOf(25, 26, 27)),
        JuzData(20, "الجزء العشرون", "النمل", "العنكبوت", listOf(27, 28, 29)),
        JuzData(21, "الجزء الحادي والعشرون", "العنكبوت", "الأحزاب", listOf(29, 30, 31, 32, 33)),
        JuzData(22, "الجزء الثاني والعشرون", "الأحزاب", "يس", listOf(33, 34, 35, 36)),
        JuzData(23, "الجزء الثالث والعشرون", "يس", "الزمر", listOf(36, 37, 38, 39)),
        JuzData(24, "الجزء الرابع والعشرون", "الزمر", "فصلت", listOf(39, 40, 41)),
        JuzData(25, "الجزء الخامس والعشرون", "فصلت", "الجاثية", listOf(41, 42, 43, 44, 45)),
        JuzData(26, "الجزء السادس والعشرون", "الأحقاف", "الذاريات", listOf(46, 47, 48, 49, 50, 51)),
        JuzData(27, "الجزء السابع والعشرون", "الذاريات", "الحديد", listOf(51, 52, 53, 54, 55, 56, 57)),
        JuzData(28, "الجزء الثامن والعشرون", "المجادلة", "التحريم", listOf(58, 59, 60, 61, 62, 63, 64, 65, 66)),
        JuzData(29, "الجزء التاسع والعشرون", "الملك", "المرسلات", listOf(67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77)),
        JuzData(30, "الجزء الثلاثون", "النبأ", "الناس", (78..114).toList())
    )
}

@Composable
fun QuranScreen(
    isArabic: Boolean,
    searchQuery: String,
    selectedSurah: Surah?,
    bookmarks: List<QuranBookmarkEntity>,
    bookmarkedSurahNumbers: Set<Int>,
    favoriteSurahNumbers: Set<Int>,
    lastReadSurahNumber: Int,
    lastReadAyahNumber: Int,
    lastReadTimestamp: Long,
    onSearchQueryChange: (String) -> Unit,
    onSelectSurah: (Surah?) -> Unit,
    onToggleBookmark: (Ayah, String, String, Boolean) -> Unit,
    onToggleSurahBookmark: (Int) -> Unit,
    onToggleSurahFavorite: (Int) -> Unit,
    onSaveReadingProgress: (Int, Int) -> Unit,
    getRelativeTimeString: (Long) -> String,
    modifier: Modifier = Modifier
) {
    if (selectedSurah != null) {
        // Detailed Surah Reading Screen
        SurahReaderView(
            isArabic = isArabic,
            surah = selectedSurah,
            bookmarks = bookmarks,
            onBack = { onSelectSurah(null) },
            onToggleBookmark = onToggleBookmark,
            onSelectSurah = onSelectSurah,
            onSaveReadingProgress = onSaveReadingProgress,
            modifier = modifier
        )
    } else {
        // Surahs Directory
        SurahsDirectoryView(
            isArabic = isArabic,
            searchQuery = searchQuery,
            bookmarks = bookmarks,
            bookmarkedSurahNumbers = bookmarkedSurahNumbers,
            favoriteSurahNumbers = favoriteSurahNumbers,
            lastReadSurahNumber = lastReadSurahNumber,
            lastReadAyahNumber = lastReadAyahNumber,
            lastReadTimestamp = lastReadTimestamp,
            onSearchQueryChange = onSearchQueryChange,
            onSelectSurah = onSelectSurah,
            onToggleBookmark = onToggleBookmark,
            onToggleSurahBookmark = onToggleSurahBookmark,
            onToggleSurahFavorite = onToggleSurahFavorite,
            onSaveReadingProgress = onSaveReadingProgress,
            getRelativeTimeString = getRelativeTimeString,
            modifier = modifier
        )
    }
}

@Composable
private fun SurahsDirectoryView(
    isArabic: Boolean,
    searchQuery: String,
    bookmarks: List<QuranBookmarkEntity>,
    bookmarkedSurahNumbers: Set<Int>,
    favoriteSurahNumbers: Set<Int>,
    lastReadSurahNumber: Int,
    lastReadAyahNumber: Int,
    lastReadTimestamp: Long,
    onSearchQueryChange: (String) -> Unit,
    onSelectSurah: (Surah?) -> Unit,
    onToggleBookmark: (Ayah, String, String, Boolean) -> Unit,
    onToggleSurahBookmark: (Int) -> Unit,
    onToggleSurahFavorite: (Int) -> Unit,
    onSaveReadingProgress: (Int, Int) -> Unit,
    getRelativeTimeString: (Long) -> String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategoryTab by remember { mutableIntStateOf(0) } // 0: السور, 1: الأجزاء, 2: علاماتي المرجعية, 3: المفضلة
    var selectedJuzNumber by remember { mutableStateOf<Int?>(null) }
    var isTadabburFavorited by remember { mutableStateOf(false) }

    val allSurahs = remember {
        com.example.data.repository.QuranOfflineDataSource.getSurahs(context)
    }

    val lastReadSurah = remember(lastReadSurahNumber, allSurahs) {
        allSurahs.find { it.number == lastReadSurahNumber } ?: allSurahs.firstOrNull() ?: Surah(18, "الكهف", "Al-Kahf", "", "Meccan", 110, emptyList())
    }

    val juzNum = remember(lastReadSurahNumber) {
        QuranSurahs114.all114SurahsMeta.firstOrNull { it.number == lastReadSurahNumber }?.juzNumber ?: 15
    }

    val progressPercent = remember(lastReadAyahNumber, lastReadSurah.totalVerses) {
        if (lastReadSurah.totalVerses > 0) {
            ((lastReadAyahNumber.toFloat() / lastReadSurah.totalVerses.toFloat()) * 100).toInt().coerceIn(1, 100)
        } else 0
    }

    val relativeTimeStr = remember(lastReadTimestamp) {
        getRelativeTimeString(lastReadTimestamp)
    }

    val filteredSurahs = remember(searchQuery, allSurahs) {
        if (searchQuery.isBlank()) {
            allSurahs
        } else {
            val normQuery = com.example.data.repository.QuranOfflineDataSource.normalizeArabic(searchQuery)
            allSurahs.filter {
                com.example.data.repository.QuranOfflineDataSource.normalizeArabic(it.nameArabic).contains(normQuery) ||
                it.nameEnglish.contains(searchQuery.trim(), ignoreCase = true) ||
                it.number.toString() == searchQuery.trim()
            }
        }
    }

    val matchedAyahs = remember(searchQuery) {
        if (searchQuery.trim().length >= 2) {
            com.example.data.repository.QuranOfflineDataSource.searchAyahs(context, searchQuery)
        } else {
            emptyList()
        }
    }

    val bookmarkedSurahsList = remember(bookmarkedSurahNumbers, allSurahs) {
        allSurahs.filter { bookmarkedSurahNumbers.contains(it.number) }
    }

    val favoriteSurahsList = remember(favoriteSurahNumbers, allSurahs) {
        allSurahs.filter { favoriteSurahNumbers.contains(it.number) }
    }

    val currentJuz = remember(selectedJuzNumber) {
        if (selectedJuzNumber != null) {
            QuranJuzData.all30Juzs.firstOrNull { it.number == selectedJuzNumber } ?: QuranJuzData.all30Juzs[0]
        } else null
    }

    val juzSurahsList = remember(selectedJuzNumber, allSurahs, currentJuz) {
        if (currentJuz != null) {
            allSurahs.filter { currentJuz.surahNumbers.contains(it.number) }
        } else emptyList()
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(CelestialBackground)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Search Bar with Mic and Search icon
            item {
                Surface(
                    shape = RoundedCornerShape(9999.dp),
                    color = CelestialSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline),
                    shadowElevation = 0.5.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "بحث",
                                tint = CelestialPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = onSearchQueryChange,
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    color = CelestialOnSurface,
                                    fontSize = 13.sp
                                ),
                                decorationBox = { innerTextField ->
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "ابحث عن سورة، جزء، أو آية...",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = CelestialSlateMuted,
                                            fontSize = 13.sp
                                        )
                                    }
                                    innerTextField()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("quran_search_input")
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "بحث صوتي",
                            tint = CelestialSlateMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // 2. Horizontal Category Tabs (السور, الأجزاء, علاماتي المرجعية, المفضلة)
            item {
                val tabs = listOf("السور", "الأجزاء", "علاماتي المرجعية", "المفضلة")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedCategoryTab == index
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(9999.dp))
                                .clickable {
                                    selectedCategoryTab = index
                                    if (index != 1) selectedJuzNumber = null
                                },
                            color = if (isSelected) CelestialPrimary else CelestialSurface,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) CelestialPrimary else CelestialBorderHairline
                            ),
                            shape = RoundedCornerShape(9999.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (title == "علاماتي المرجعية") {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else CelestialSlateMuted,
                                        modifier = Modifier.size(14.dp)
                                    )
                                } else if (title == "المفضلة") {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else CelestialSlateMuted,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else CelestialSlateMuted,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // Content depending on selectedCategoryTab
            when (selectedCategoryTab) {
                0 -> {
                    // --- TAB 0: السور ---
                    // Hero "آخر قراءة" Card
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .shadow(10.dp, RoundedCornerShape(24.dp), spotColor = CelestialPrimary.copy(alpha = 0.25f)),
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
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    // Badge Row
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(9999.dp))
                                                .background(Color.White.copy(alpha = 0.22f))
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.MenuBook,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Text(
                                                text = "آخر قراءة",
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Text(
                                            text = relativeTimeStr,
                                            color = Color.White.copy(alpha = 0.85f),
                                            fontSize = 11.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "سورة ${lastReadSurah.nameArabic}",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 28.sp
                                    )

                                    Text(
                                        text = "${if (lastReadSurah.revelationType == "Meccan") "مكية" else "مدنية"} • ${lastReadSurah.totalVerses} آية",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 12.sp
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.White.copy(alpha = 0.18f))
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "الآية $lastReadAyahNumber",
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.White.copy(alpha = 0.18f))
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "الجزء $juzNum",
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Progress Bar
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "تقدم القراءة في السورة",
                                                color = Color.White.copy(alpha = 0.9f),
                                                fontSize = 11.sp
                                            )
                                            Text(
                                                text = "$progressPercent%",
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        LinearProgressIndicator(
                                            progress = { (progressPercent / 100f).coerceIn(0.01f, 1f) },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(6.dp)
                                                .clip(RoundedCornerShape(9999.dp)),
                                            color = Color.White,
                                            trackColor = Color.White.copy(alpha = 0.3f)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(18.dp))

                                    // Action Row - "متابعة القراءة" button only
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(9999.dp))
                                                .clickable {
                                                    onSaveReadingProgress(lastReadSurah.number, lastReadAyahNumber)
                                                    onSelectSurah(lastReadSurah)
                                                },
                                            color = Color.White,
                                            shape = RoundedCornerShape(9999.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 9.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(
                                                    text = "متابعة القراءة",
                                                    color = CelestialPrimary,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp
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
                    }

                    // "آية وتدبر" Card
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .border(1.dp, CelestialBorderHairline, RoundedCornerShape(18.dp)),
                            color = CelestialSurface
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "✨ آية وتدبر",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = CelestialPrimary,
                                        fontSize = 13.sp
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        IconButton(
                                            onClick = { isTadabburFavorited = !isTadabburFavorited },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isTadabburFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                                contentDescription = "تفضيل",
                                                tint = if (isTadabburFavorited) Color(0xFFEF4444) else CelestialSlateMuted,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_SEND).apply {
                                                    type = "text/plain"
                                                    putExtra(
                                                        Intent.EXTRA_TEXT,
                                                        "﴿ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ ﴾ — [سورة الرعد: 28]"
                                                    )
                                                }
                                                context.startActivity(Intent.createChooser(intent, "مشاركة الآية"))
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Share,
                                                contentDescription = "مشاركة",
                                                tint = CelestialSlateMuted,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "﴿ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ ﴾",
                                    style = QuranVerseTextStyle.copy(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CelestialOnSurface,
                                        textAlign = TextAlign.Center
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "سورة الرعد • آية 28",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CelestialSlateMuted,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // "فهرس السور" Section Header
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "فهرس السور",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = CelestialOnSurface,
                                    fontSize = 16.sp
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(CelestialSurfaceContainerLow)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${filteredSurahs.size} سورة",
                                        color = CelestialPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sort,
                                    contentDescription = null,
                                    tint = CelestialSlateMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "حسب الترتيب",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CelestialSlateMuted,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    // Ayah Search Results (if user searched)
                    if (matchedAyahs.isNotEmpty()) {
                        item {
                            Text(
                                text = "نتائج البحث في الآيات الكريمة (${matchedAyahs.size})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CelestialPrimary,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                            )
                        }
                        items(matchedAyahs) { match ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable {
                                        val fullSurah = com.example.data.repository.QuranOfflineDataSource.getSurahByNumber(context, match.surahNumber)
                                        if (fullSurah != null) {
                                            onSaveReadingProgress(fullSurah.number, match.ayahNumber)
                                            onSelectSurah(fullSurah)
                                        }
                                    },
                                shape = RoundedCornerShape(14.dp),
                                color = CelestialSurface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "سورة ${match.surahNameArabic}",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = CelestialPrimary,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "الآية ${toArabicDigits(match.ayahNumber)}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = CelestialGold,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "﴿ ${match.arabicText} ﴾",
                                        style = QuranVerseTextStyle.copy(
                                            fontSize = 17.sp,
                                            lineHeight = 28.sp,
                                            color = CelestialOnSurface,
                                            textAlign = TextAlign.Start
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // List of Surahs with Bookmark and Heart/Favorite Icons
                    items(filteredSurahs) { surah ->
                        val isBookmarked = bookmarkedSurahNumbers.contains(surah.number)
                        val isFavorited = favoriteSurahNumbers.contains(surah.number)
                        SurahIndexCard(
                            surah = surah,
                            isBookmarked = isBookmarked,
                            isFavorited = isFavorited,
                            showBookmarkAndHeart = true,
                            onClick = {
                                onSaveReadingProgress(surah.number, 1)
                                onSelectSurah(surah)
                            },
                            onToggleBookmark = { onToggleSurahBookmark(surah.number) },
                            onToggleFavorite = { onToggleSurahFavorite(surah.number) }
                        )
                    }
                }

                1 -> {
                    // --- TAB 1: الأجزاء ---
                    if (selectedJuzNumber == null) {
                        // Show list of 30 Juzs
                        item {
                            Text(
                                text = "أجزاء القرآن الكريم (30 جزءًا)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CelestialOnSurface,
                                fontSize = 16.sp
                            )
                        }
                        items(QuranJuzData.all30Juzs) { juz ->
                            JuzIndexCard(
                                juz = juz,
                                onClick = { selectedJuzNumber = juz.number }
                            )
                        }
                    } else {
                        // Selected Juz Surah View
                        val currentJuzObj = currentJuz ?: QuranJuzData.all30Juzs[0]
                        val juzSurahs = allSurahs.filter { currentJuzObj.surahNumbers.contains(it.number) }

                        item {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { selectedJuzNumber = null }
                                    .border(1.dp, CelestialBorderHairline, RoundedCornerShape(16.dp)),
                                color = CelestialSurface
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "رجوع للأجزاء",
                                        tint = CelestialPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Column {
                                        Text(
                                            text = currentJuzObj.nameArabic,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = CelestialPrimary,
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            text = "من سورة ${currentJuzObj.startSurahName} إلى سورة ${currentJuzObj.endSurahName} • انقر للرجوع للأجزاء",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = CelestialSlateMuted,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }

                        // In the Juz view, show ONLY the Juz number/name and Surahs. Do NOT show bookmark or heart icons inside the Juz view.
                        items(juzSurahs) { surah ->
                            SurahIndexCard(
                                surah = surah,
                                isBookmarked = false,
                                isFavorited = false,
                                showBookmarkAndHeart = false,
                                onClick = {
                                    onSaveReadingProgress(surah.number, 1)
                                    onSelectSurah(surah)
                                }
                            )
                        }
                    }
                }

                2 -> {
                    // --- TAB 2: علاماتي المرجعية ---
                    item {
                        Text(
                            text = "علاماتي المرجعية المحفوظة",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CelestialOnSurface,
                            fontSize = 16.sp
                        )
                    }

                    if (bookmarkedSurahsList.isEmpty() && bookmarks.isEmpty()) {
                        item {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.dp, CelestialBorderHairline, RoundedCornerShape(16.dp)),
                                color = CelestialSurface
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BookmarkBorder,
                                        contentDescription = null,
                                        tint = CelestialSlateMuted,
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "لا توجد علامات مرجعية محفوظة بعد",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = CelestialSlateMuted,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    } else {
                        // Bookmarked Surahs
                        items(bookmarkedSurahsList) { surah ->
                            SurahIndexCard(
                                surah = surah,
                                isBookmarked = true,
                                isFavorited = favoriteSurahNumbers.contains(surah.number),
                                showBookmarkAndHeart = true,
                                onClick = {
                                    onSaveReadingProgress(surah.number, 1)
                                    onSelectSurah(surah)
                                },
                                onToggleBookmark = { onToggleSurahBookmark(surah.number) },
                                onToggleFavorite = { onToggleSurahFavorite(surah.number) }
                            )
                        }

                        // Bookmarked Ayahs from Room
                        items(bookmarks) { bEntity ->
                            val surah = allSurahs.find { it.number == bEntity.surahNumber }
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable {
                                        if (surah != null) {
                                            onSaveReadingProgress(surah.number, bEntity.ayahNumber)
                                            onSelectSurah(surah)
                                        }
                                    },
                                shape = RoundedCornerShape(14.dp),
                                color = CelestialSurface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "سورة ${bEntity.surahNameArabic}",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = CelestialPrimary,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "الآية ${toArabicDigits(bEntity.ayahNumber)}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = CelestialGold,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "﴿ ${bEntity.arabicText} ﴾",
                                        style = QuranVerseTextStyle.copy(
                                            fontSize = 16.sp,
                                            lineHeight = 26.sp,
                                            color = CelestialOnSurface,
                                            textAlign = TextAlign.Start
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                3 -> {
                    // --- TAB 3: المفضلة ---
                    item {
                        Text(
                            text = "السور المفضلة",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CelestialOnSurface,
                            fontSize = 16.sp
                        )
                    }

                    if (favoriteSurahsList.isEmpty()) {
                        item {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.dp, CelestialBorderHairline, RoundedCornerShape(16.dp)),
                                color = CelestialSurface
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FavoriteBorder,
                                        contentDescription = null,
                                        tint = CelestialSlateMuted,
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "لا توجد سور مفضلة محفوظة بعد",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = CelestialSlateMuted,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    } else {
                        items(favoriteSurahsList) { surah ->
                            SurahIndexCard(
                                surah = surah,
                                isBookmarked = bookmarkedSurahNumbers.contains(surah.number),
                                isFavorited = true,
                                showBookmarkAndHeart = true,
                                onClick = {
                                    onSaveReadingProgress(surah.number, 1)
                                    onSelectSurah(surah)
                                },
                                onToggleBookmark = { onToggleSurahBookmark(surah.number) },
                                onToggleFavorite = { onToggleSurahFavorite(surah.number) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun JuzIndexCard(
    juz: JuzData,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
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
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CelestialPrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${juz.number}",
                        fontWeight = FontWeight.Bold,
                        color = CelestialPrimary,
                        fontSize = 14.sp
                    )
                }

                Column {
                    Text(
                        text = "الجزء ${juz.number}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CelestialOnSurface,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "من سورة ${juz.startSurahName} إلى سورة ${juz.endSurahName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = CelestialSlateMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "عرض السور",
                tint = CelestialSlateMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun SurahIndexCard(
    surah: Surah,
    isBookmarked: Boolean,
    isFavorited: Boolean = false,
    showBookmarkAndHeart: Boolean = true,
    onClick: () -> Unit,
    onToggleBookmark: (() -> Unit)? = null,
    onToggleFavorite: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .border(
                1.dp,
                if (isBookmarked) CelestialPrimaryBright.copy(alpha = 0.5f) else CelestialBorderHairline,
                RoundedCornerShape(16.dp)
            ),
        color = if (isBookmarked) CelestialSurfaceContainerLow.copy(alpha = 0.4f) else CelestialSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Right Side: Star Number Badge + Surah Name + Type & Verses
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 8-point Islamic Star number badge
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isBookmarked) CelestialPrimary else CelestialSurfaceContainerLow
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${surah.number}",
                        fontWeight = FontWeight.Bold,
                        color = if (isBookmarked) Color.White else CelestialPrimary,
                        fontSize = 13.sp
                    )
                }

                Column {
                    Text(
                        text = "سورة ${surah.nameArabic}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CelestialOnSurface,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "${if (surah.revelationType == "Meccan") "مكية" else "مدنية"} • ${surah.totalVerses} آية",
                        style = MaterialTheme.typography.labelSmall,
                        color = CelestialSlateMuted,
                        fontSize = 11.sp
                    )
                }
            }

            // Left Side: Heart / Favorite & Bookmark buttons if enabled
            if (showBookmarkAndHeart) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = { onToggleFavorite?.invoke() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "المفضلة",
                            tint = if (isFavorited) Color(0xFFEF4444) else CelestialSlateMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { onToggleBookmark?.invoke() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "علامة مرجعية",
                            tint = if (isBookmarked) CelestialPrimary else CelestialSlateMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Helper: Convert integer to Arabic-Indic digits
// -------------------------------------------------------------
private fun toArabicDigits(number: Int): String {
    val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    return number.toString().map { char ->
        if (char in '0'..'9') arabicDigits[char - '0'] else char
    }.joinToString("")
}

// -------------------------------------------------------------
// Detailed Surah Reading Screen - Authentic Mushaf Experience
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SurahReaderView(
    isArabic: Boolean,
    surah: Surah,
    bookmarks: List<QuranBookmarkEntity>,
    onBack: () -> Unit,
    onToggleBookmark: (Ayah, String, String, Boolean) -> Unit,
    onSelectSurah: (Surah?) -> Unit,
    onSaveReadingProgress: (Int, Int) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var fontSizeSp by remember { mutableStateOf(26.sp) }
    var selectedAyahForSheet by remember { mutableStateOf<Ayah?>(null) }

    val surahMeta = remember(surah.number) {
        QuranSurahs114.all114SurahsMeta.firstOrNull { it.number == surah.number }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CelestialBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Top App Bar / Reader Toolbar
            ReaderTopBar(
                surah = surah,
                fontSizeSp = fontSizeSp,
                onBack = onBack,
                onFontSizeDecrease = {
                    if (fontSizeSp.value > 18f) fontSizeSp = (fontSizeSp.value - 2).sp
                },
                onFontSizeIncrease = {
                    if (fontSizeSp.value < 38f) fontSizeSp = (fontSizeSp.value + 2).sp
                }
            )

            // 2. Mushaf Page Canvas with vertical scrolling
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 4.dp, bottom = 48.dp)
            ) {
                // Surah Header Banner (إطار السورة)
                item {
                    SurahMushafHeaderBanner(surah = surah, surahMeta = surahMeta)
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Basmalah Calligraphy (Shown for all Surahs except Surah 1 Al-Fatihah where Basmalah is verse 1:1, and Surah 9 At-Tawbah)
                if (surah.number != 1 && surah.number != 9) {
                    item {
                        BasmalahMushafHeader()
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }

                // The Continuous Quran Reading Experience (MUSHAF)
                item {
                    MushafContinuousContent(
                        surah = surah,
                        fontSizeSp = fontSizeSp,
                        bookmarks = bookmarks,
                        onAyahClick = { ayah ->
                            onSaveReadingProgress(surah.number, ayah.ayahNumber)
                            selectedAyahForSheet = ayah
                        }
                    )
                }

                // Surah End / Sadaqallahul Adheem & Navigation between Surahs
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    SurahFooterNavigation(
                        currentSurahNumber = surah.number,
                        onNavigateToSurahNumber = { targetNumber ->
                            val targetSurah = com.example.data.repository.QuranOfflineDataSource.getSurahByNumber(context, targetNumber)
                            if (targetSurah != null) {
                                onSelectSurah(targetSurah)
                            }
                        },
                        onReturnToDirectory = onBack
                    )
                }
            }
        }

        // 3. Ayah Action Bottom Sheet when an Ayah is tapped
        selectedAyahForSheet?.let { ayah ->
            val isBookmarked = bookmarks.any {
                it.surahNumber == surah.number && it.ayahNumber == ayah.ayahNumber
            }
            AyahActionBottomSheet(
                surah = surah,
                ayah = ayah,
                isBookmarked = isBookmarked,
                onDismiss = { selectedAyahForSheet = null },
                onToggleBookmark = {
                    onToggleBookmark(ayah, surah.nameArabic, surah.nameEnglish, isBookmarked)
                },
                onCopy = {
                    val textToCopy = "﴿ ${ayah.arabicText} ﴾ [سورة ${surah.nameArabic}: الآية ${ayah.ayahNumber}]"
                    clipboardManager.setText(AnnotatedString(textToCopy))
                    Toast.makeText(context, "تم نسخ الآية الكريمة بنجاح", Toast.LENGTH_SHORT).show()
                },
                onShare = {
                    val shareText = "﴿ ${ayah.arabicText} ﴾\n[سورة ${surah.nameArabic}: الآية ${ayah.ayahNumber}]"
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(Intent.createChooser(intent, "مشاركة الآية"))
                }
            )
        }
    }
}

// -------------------------------------------------------------
// Reader Top Bar: Navigation & Secondary Controls
// -------------------------------------------------------------
@Composable
private fun ReaderTopBar(
    surah: Surah,
    fontSizeSp: androidx.compose.ui.unit.TextUnit,
    onBack: () -> Unit,
    onFontSizeDecrease: () -> Unit,
    onFontSizeIncrease: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back Button & Surah Title Info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                shape = CircleShape,
                color = CelestialSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline),
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onBack)
                    .testTag("surah_back_button")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = CelestialPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column {
                Text(
                    text = "سُورَةُ ${surah.nameArabic}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CelestialOnSurface,
                    fontSize = 16.sp
                )
                Text(
                    text = "${if (surah.revelationType == "Meccan") "مكية" else "مدنية"} • ${surah.totalVerses} آية",
                    style = MaterialTheme.typography.labelSmall,
                    color = CelestialSlateMuted,
                    fontSize = 11.sp
                )
            }
        }

        // Secondary Controls: Font Size Pill
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Font Size Adjuster Pill
            Surface(
                shape = RoundedCornerShape(9999.dp),
                color = CelestialSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    IconButton(
                        onClick = onFontSizeDecrease,
                        modifier = Modifier.size(26.dp)
                    ) {
                        Text(
                            text = "A-",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CelestialPrimary
                        )
                    }
                    Text(
                        text = "${fontSizeSp.value.toInt()}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelestialSlateMuted,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                    IconButton(
                        onClick = onFontSizeIncrease,
                        modifier = Modifier.size(26.dp)
                    ) {
                        Text(
                            text = "A+",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CelestialPrimary
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Traditional Ornate Surah Mushaf Header Banner
// -------------------------------------------------------------
@Composable
private fun SurahMushafHeaderBanner(
    surah: Surah,
    surahMeta: QuranSurahs114.SurahMeta?
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CelestialSurfaceContainerLow,
        border = androidx.compose.foundation.BorderStroke(1.dp, CelestialGold.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "❖",
                    color = CelestialGold,
                    fontSize = 12.sp
                )
                Text(
                    text = "سُورَةُ ${surah.nameArabic}",
                    style = QuranVerseTextStyle.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelestialPrimary
                    )
                )
                Text(
                    text = "❖",
                    color = CelestialGold,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (surah.revelationType == "Meccan") "مَكِّيَّةٌ" else "مَدَنِيَّةٌ",
                    style = MaterialTheme.typography.labelSmall,
                    color = CelestialSlateMuted,
                    fontSize = 11.sp
                )
                Text("•", color = CelestialSlateMuted, fontSize = 10.sp)
                Text(
                    text = "آيَاتُهَا ${toArabicDigits(surah.totalVerses)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = CelestialSlateMuted,
                    fontSize = 11.sp
                )
                if (surahMeta != null) {
                    Text("•", color = CelestialSlateMuted, fontSize = 10.sp)
                    Text(
                        text = "الجُزْءُ ${toArabicDigits(surahMeta.juzNumber)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = CelestialPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Basmalah Calligraphy Header
// -------------------------------------------------------------
@Composable
private fun BasmalahMushafHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            style = QuranVerseTextStyle.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CelestialSlateDeep,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(1.dp)
                    .background(CelestialGold.copy(alpha = 0.4f))
            )
            Text("✤", color = CelestialGold, fontSize = 9.sp)
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(1.dp)
                    .background(CelestialGold.copy(alpha = 0.4f))
            )
        }
    }
}

// -------------------------------------------------------------
// Continuous Quran Reading Content (The Pure Mushaf View)
// -------------------------------------------------------------
@Composable
private fun MushafContinuousContent(
    surah: Surah,
    fontSizeSp: androidx.compose.ui.unit.TextUnit,
    bookmarks: List<QuranBookmarkEntity>,
    onAyahClick: (Ayah) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = CelestialSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 22.dp)
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                // Chunk Ayahs (10 per chunk) to ensure smooth layout, measurement, and scrolling for long Surahs like Al-Baqarah
                val ayahChunks = remember(surah.ayahs) {
                    if (surah.ayahs.size > 15) surah.ayahs.chunked(10) else listOf(surah.ayahs)
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(0.dp) // Unbroken seamless reading flow
                ) {
                    ayahChunks.forEach { chunk ->
                        val annotatedQuranText = remember(chunk, fontSizeSp, bookmarks) {
                            buildAnnotatedString {
                                chunk.forEach { ayah ->
                                    val isBookmarked = bookmarks.any {
                                        it.surahNumber == surah.number && it.ayahNumber == ayah.ayahNumber
                                    }

                                    pushStringAnnotation(tag = "AYAH", annotation = "${ayah.ayahNumber}")

                                    // Clean Arabic Quranic verse text
                                    append(ayah.arabicText.trim())
                                    append(" ")

                                    // Subtle inline Ayah number marker in authentic Quran style
                                    val arabicNum = toArabicDigits(ayah.ayahNumber)
                                    withStyle(
                                        SpanStyle(
                                            color = if (isBookmarked) CelestialGold else CelestialPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = (fontSizeSp.value * 0.82f).sp
                                        )
                                    ) {
                                        if (isBookmarked) {
                                            append("\uFD3F$arabicNum ★\uFD3E")
                                        } else {
                                            append("\uFD3F$arabicNum\uFD3E")
                                        }
                                    }

                                    pop()
                                    append("  ")
                                }
                            }
                        }

                        ClickableText(
                            text = annotatedQuranText,
                            style = QuranVerseTextStyle.copy(
                                fontSize = fontSizeSp,
                                lineHeight = (fontSizeSp.value * 2.1f).sp,
                                color = CelestialOnSurface,
                                textAlign = if (surah.ayahs.size <= 4) TextAlign.Center else TextAlign.Justify
                            ),
                            onClick = { offset ->
                                annotatedQuranText.getStringAnnotations(tag = "AYAH", start = offset, end = offset)
                                    .firstOrNull()?.let { annotation ->
                                        val ayahNum = annotation.item.toIntOrNull()
                                        val clicked = chunk.firstOrNull { it.ayahNumber == ayahNum }
                                        if (clicked != null) {
                                            onAyahClick(clicked)
                                        }
                                    }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("mushaf_continuous_text")
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Surah End / Sadaqallahul Adheem & Navigation between Surahs
// -------------------------------------------------------------
@Composable
private fun SurahFooterNavigation(
    currentSurahNumber: Int,
    onNavigateToSurahNumber: (Int) -> Unit,
    onReturnToDirectory: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Closing calligraphy
        Text(
            text = "صَدَقَ اللَّهُ الْعَظِيمُ",
            style = QuranVerseTextStyle.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CelestialSlateMuted,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(18.dp))

        // Navigation row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous Surah (if number > 1)
            if (currentSurahNumber > 1) {
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9999.dp))
                        .clickable { onNavigateToSurahNumber(currentSurahNumber - 1) },
                    shape = RoundedCornerShape(9999.dp),
                    color = CelestialSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = CelestialPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "السورة السابقة",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = CelestialPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            // Return to Directory
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .clickable(onClick = onReturnToDirectory),
                shape = RoundedCornerShape(9999.dp),
                color = CelestialSurfaceContainerLow
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = CelestialPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "فهرس السور",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = CelestialPrimary,
                        fontSize = 12.sp
                    )
                }
            }

            // Next Surah (if number < 114)
            if (currentSurahNumber < 114) {
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9999.dp))
                        .clickable { onNavigateToSurahNumber(currentSurahNumber + 1) },
                    shape = RoundedCornerShape(9999.dp),
                    color = CelestialSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "السورة التالية",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = CelestialPrimary,
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
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }
        }
    }
}

// -------------------------------------------------------------
// Ayah Action Bottom Sheet (Study, Bookmark, Copy, Share)
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AyahActionBottomSheet(
    surah: Surah,
    ayah: Ayah,
    isBookmarked: Boolean,
    onDismiss: () -> Unit,
    onToggleBookmark: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CelestialSurface,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 32.dp)
        ) {
            // Sheet Header
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
                            .clip(RoundedCornerShape(8.dp))
                            .background(CelestialSurfaceContainerLow)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "سورة ${surah.nameArabic} • الآية ${ayah.ayahNumber}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = CelestialPrimary,
                            fontSize = 12.sp
                        )
                    }
                    if (isBookmarked) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CelestialGold.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "★ محفوظة",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = CelestialGold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "إغلاق",
                        tint = CelestialSlateMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic text of selected Ayah
            Text(
                text = "﴿ ${ayah.arabicText} ﴾",
                style = QuranVerseTextStyle.copy(
                    fontSize = 22.sp,
                    lineHeight = 38.sp,
                    color = CelestialOnSurface,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons Row (Bookmark, Copy, Share)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Bookmark button
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onToggleBookmark),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isBookmarked) CelestialGold.copy(alpha = 0.15f) else CelestialSurfaceContainerLow,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isBookmarked) CelestialGold else CelestialBorderHairline
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = if (isBookmarked) CelestialGold else CelestialPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBookmarked) "محفوظة" else "حفظ علامة",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isBookmarked) CelestialGold else CelestialPrimary,
                            fontSize = 12.sp
                        )
                    }
                }

                // Copy button
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onCopy),
                    shape = RoundedCornerShape(12.dp),
                    color = CelestialSurfaceContainerLow,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            tint = CelestialPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "نسخ الآية",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = CelestialPrimary,
                            fontSize = 12.sp
                        )
                    }
                }

                // Share button
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onShare),
                    shape = RoundedCornerShape(12.dp),
                    color = CelestialSurfaceContainerLow,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = CelestialPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "مشاركة",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = CelestialPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
