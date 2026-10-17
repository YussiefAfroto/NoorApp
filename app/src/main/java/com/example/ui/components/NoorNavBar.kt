package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CelestialBorderHairline
import com.example.ui.theme.CelestialOnSurface
import com.example.ui.theme.CelestialPrimary
import com.example.ui.theme.CelestialPrimaryBright
import com.example.ui.theme.CelestialSlateMuted
import com.example.ui.theme.CelestialSurface
import com.example.ui.theme.CelestialSurfaceContainerLow
import com.example.ui.viewmodel.NoorNavTab

data class NavItem(
    val tab: NoorNavTab,
    val titleArabic: String,
    val titleEnglish: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun NoorNavBar(
    currentTab: NoorNavTab,
    isArabic: Boolean,
    onTabSelected: (NoorNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    // Exactly 6 sections in the exact order requested by user:
    // 1. Home (الرئيسية)
    // 2. Quran (القرآن الكريم)
    // 3. Prayer Times (مواعيد الصلاة)
    // 4. Qibla (القبلة)
    // 5. Azkar (الأذكار)
    // 6. Hadith (الأحاديث)
    val navItems = listOf(
        NavItem(
            tab = NoorNavTab.HOME,
            titleArabic = "الرئيسية",
            titleEnglish = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        NavItem(
            tab = NoorNavTab.QURAN,
            titleArabic = "القرآن الكريم",
            titleEnglish = "Quran",
            selectedIcon = Icons.Filled.MenuBook,
            unselectedIcon = Icons.Outlined.MenuBook
        ),
        NavItem(
            tab = NoorNavTab.PRAYER_TIMES,
            titleArabic = "مواعيد الصلاة",
            titleEnglish = "Prayers",
            selectedIcon = Icons.Filled.AccessTime,
            unselectedIcon = Icons.Outlined.AccessTime
        ),
        NavItem(
            tab = NoorNavTab.QIBLA,
            titleArabic = "القبلة",
            titleEnglish = "Qibla",
            selectedIcon = Icons.Filled.Explore,
            unselectedIcon = Icons.Outlined.Explore
        ),
        NavItem(
            tab = NoorNavTab.ATHKAR,
            titleArabic = "الأذكار",
            titleEnglish = "Azkar",
            selectedIcon = Icons.Filled.AutoAwesome,
            unselectedIcon = Icons.Outlined.AutoAwesome
        ),
        NavItem(
            tab = NoorNavTab.HADITH,
            titleArabic = "الأحاديث",
            titleEnglish = "Hadith",
            selectedIcon = Icons.Filled.LibraryBooks,
            unselectedIcon = Icons.Outlined.LibraryBooks
        )
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                spotColor = CelestialPrimary.copy(alpha = 0.12f)
            )
            .border(
                1.dp,
                CelestialBorderHairline,
                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        color = CelestialSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val isSelected = currentTab == item.tab
                val interactionSource = remember { MutableInteractionSource() }

                val animatedIconColor by animateColorAsState(
                    targetValue = if (isSelected) CelestialPrimary else CelestialSlateMuted,
                    label = "navIconColor"
                )

                val animatedTextColor by animateColorAsState(
                    targetValue = if (isSelected) CelestialPrimary else CelestialSlateMuted,
                    label = "navTextColor"
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = ripple(bounded = false, radius = 24.dp),
                            onClick = { onTabSelected(item.tab) }
                        )
                        .padding(vertical = 4.dp)
                        .testTag("nav_tab_${item.tab.name.lowercase()}")
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) CelestialSurfaceContainerLow else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = if (isArabic) item.titleArabic else item.titleEnglish,
                            tint = animatedIconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = if (isArabic) item.titleArabic else item.titleEnglish,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = animatedTextColor,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
