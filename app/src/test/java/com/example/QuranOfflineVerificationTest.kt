package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.QuranOfflineDataSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class QuranOfflineVerificationTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun verifyTotalSurahCountIs114() {
        val surahs = QuranOfflineDataSource.getSurahs(context)
        assertEquals("Total number of Surahs must be exactly 114", 114, surahs.size)

        for (i in 1..114) {
            val surah = surahs[i - 1]
            assertEquals("Surah number must match index $i", i, surah.number)
            assertTrue("Surah Arabic name must not be blank", surah.nameArabic.isNotBlank())
            assertTrue("Surah English name must not be blank", surah.nameEnglish.isNotBlank())
            assertTrue("Total verses must be greater than 0", surah.totalVerses > 0)
            assertEquals("Verse list size must equal totalVerses", surah.totalVerses, surah.ayahs.size)
        }
    }

    @Test
    fun verifyTotalAyahCountIs6236() {
        val surahs = QuranOfflineDataSource.getSurahs(context)
        val totalAyahs = surahs.sumOf { it.ayahs.size }
        assertEquals("Total Quran Ayahs must be exactly 6236", 6236, totalAyahs)
    }

    @Test
    fun verifyFifteenRepresentativeSurahs() {
        val testSurahs = listOf(
            Triple(1, 7, "الفاتحة"),
            Triple(2, 286, "البقرة"),
            Triple(3, 200, "آل عمران"),
            Triple(4, 176, "النساء"),
            Triple(5, 120, "المائدة"),
            Triple(6, 165, "الأنعام"),
            Triple(18, 110, "الكهف"),
            Triple(19, 98, "مريم"),
            Triple(36, 83, "يس"),
            Triple(55, 78, "الرحمن"),
            Triple(56, 96, "الواقعة"),
            Triple(67, 30, "الملك"),
            Triple(112, 4, "الإخلاص"),
            Triple(113, 5, "الفلق"),
            Triple(114, 6, "الناس")
        )

        for ((surahNumber, expectedVerses, expectedNameAr) in testSurahs) {
            val surah = QuranOfflineDataSource.getSurahByNumber(context, surahNumber)
            assertNotNull("Surah $surahNumber must exist", surah)
            assertEquals("Surah $surahNumber Arabic name mismatch", expectedNameAr, surah!!.nameArabic)
            assertEquals("Surah $surahNumber verse count mismatch", expectedVerses, surah.ayahs.size)
            assertEquals("Surah $surahNumber totalVerses mismatch", expectedVerses, surah.totalVerses)

            // Verify first and last verse numbers
            assertEquals(1, surah.ayahs.first().ayahNumber)
            assertEquals(expectedVerses, surah.ayahs.last().ayahNumber)

            // Verify non-blank and proper Arabic text
            for (ayah in surah.ayahs) {
                assertTrue("Ayah ${surahNumber}:${ayah.ayahNumber} must have Arabic text", ayah.arabicText.isNotBlank())
                assertEquals(surahNumber, ayah.surahNumber)
            }
        }
    }

    @Test
    fun verifySurahAlFatihahAndAlBaqarahAndAnNas() {
        // Al-Fatihah
        val fatihah = QuranOfflineDataSource.getSurahByNumber(context, 1)
        assertNotNull(fatihah)
        assertEquals("Al-Fatihah must have 7 verses", 7, fatihah!!.ayahs.size)
        assertEquals(1, fatihah.ayahs[0].ayahNumber)
        assertTrue(fatihah.ayahs[0].arabicText.contains("بِسْمِ"))

        // Al-Baqarah
        val baqarah = QuranOfflineDataSource.getSurahByNumber(context, 2)
        assertNotNull(baqarah)
        assertEquals("Al-Baqarah must have 286 verses", 286, baqarah!!.ayahs.size)
        // Ayat Al-Kursi (2:255)
        val kursi = baqarah.ayahs[254]
        assertEquals(255, kursi.ayahNumber)
        val normalizedKursi = QuranOfflineDataSource.normalizeArabic(kursi.arabicText)
        assertTrue(normalizedKursi.contains("الله لا اله الا هو الحي القيوم"))

        // An-Nas
        val nas = QuranOfflineDataSource.getSurahByNumber(context, 114)
        assertNotNull(nas)
        assertEquals("An-Nas must have 6 verses", 6, nas!!.ayahs.size)
        assertEquals(6, nas.ayahs[5].ayahNumber)
        val normalizedNas = QuranOfflineDataSource.normalizeArabic(nas.ayahs[5].arabicText)
        assertTrue(normalizedNas.contains("من الجنه والناس"))
    }

    @Test
    fun verifyNoDuplicateOrPlaceholderAyahs() {
        val surahs = QuranOfflineDataSource.getSurahs(context)
        val allAyahKeys = mutableSetOf<String>()

        for (surah in surahs) {
            for (ayah in surah.ayahs) {
                val key = "${surah.number}:${ayah.ayahNumber}"
                assertFalse("Duplicate Ayah key found: $key", allAyahKeys.contains(key))
                allAyahKeys.add(key)

                assertTrue("Ayah $key must not be blank", ayah.arabicText.isNotBlank())
                assertFalse("Ayah $key must not contain 'placeholder'", ayah.arabicText.contains("placeholder", ignoreCase = true))
                assertFalse("Ayah $key must not contain 'dummy'", ayah.arabicText.contains("dummy", ignoreCase = true))
                assertFalse("Ayah $key must not contain 'sample'", ayah.arabicText.contains("sample", ignoreCase = true))
                assertFalse("Ayah $key must not contain 'Lorem'", ayah.arabicText.contains("Lorem", ignoreCase = true))
            }
        }
        assertEquals(6236, allAyahKeys.size)
    }

    @Test
    fun verifyOfflineSearch() {
        val resultsIkhlas = QuranOfflineDataSource.searchAyahs(context, "قل هو الله أحد", maxResults = 5)
        assertTrue("Search for Surah Al-Ikhlas must return matches", resultsIkhlas.isNotEmpty())
        assertEquals(112, resultsIkhlas[0].surahNumber)
        assertEquals(1, resultsIkhlas[0].ayahNumber)

        val resultsKursi = QuranOfflineDataSource.searchAyahs(context, "الحي القيوم", maxResults = 5)
        assertTrue("Search for 'الحي القيوم' must return matches", resultsKursi.isNotEmpty())
        assertTrue(resultsKursi.any { it.surahNumber == 2 && it.ayahNumber == 255 })
    }

    @Test
    fun verifyAll30JuzsDataAndSurahMapping() {
        val allJuzs = com.example.ui.screens.QuranJuzData.all30Juzs
        assertEquals("There must be exactly 30 Juzs", 30, allJuzs.size)

        for (i in 1..30) {
            val juz = allJuzs[i - 1]
            assertEquals("Juz number must be $i", i, juz.number)
            assertTrue("Juz Arabic name must not be blank", juz.nameArabic.isNotBlank())
            assertTrue("Juz start surah name must not be blank", juz.startSurahName.isNotBlank())
            assertTrue("Juz end surah name must not be blank", juz.endSurahName.isNotBlank())
            assertTrue("Juz surahNumbers must not be empty", juz.surahNumbers.isNotEmpty())
        }

        // Verify Juz 1 contains Surahs 1 and 2
        val juz1 = allJuzs[0]
        assertTrue(juz1.surahNumbers.contains(1))
        assertTrue(juz1.surahNumbers.contains(2))

        // Verify Juz 30 contains Surah 78 through 114
        val juz30 = allJuzs[29]
        assertEquals(37, juz30.surahNumbers.size)
        assertTrue(juz30.surahNumbers.contains(78))
        assertTrue(juz30.surahNumbers.contains(114))
    }

    @Test
    fun verifySurahBookmarksAndFavoritesPersistence() {
        val prefs = context.getSharedPreferences("test_noor_prefs", Context.MODE_PRIVATE)

        // Save bookmarks
        val testBookmarks = setOf(1, 18, 36)
        prefs.edit().putStringSet("bookmarked_surahs", testBookmarks.map { it.toString() }.toSet()).commit()

        val loadedBookmarks = prefs.getStringSet("bookmarked_surahs", emptySet())
            ?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        assertEquals(testBookmarks, loadedBookmarks)

        // Save favorites
        val testFavorites = setOf(18, 55, 67)
        prefs.edit().putStringSet("favorite_surahs", testFavorites.map { it.toString() }.toSet()).commit()

        val loadedFavorites = prefs.getStringSet("favorite_surahs", emptySet())
            ?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        assertEquals(testFavorites, loadedFavorites)
    }

    @Test
    fun verifyReadingProgressPersistence() {
        val prefs = context.getSharedPreferences("test_noor_prefs", Context.MODE_PRIVATE)
        val now = System.currentTimeMillis()

        prefs.edit()
            .putInt("last_read_surah", 18)
            .putInt("last_read_ayah", 46)
            .putLong("last_read_timestamp", now)
            .commit()

        assertEquals(18, prefs.getInt("last_read_surah", 1))
        assertEquals(46, prefs.getInt("last_read_ayah", 1))
        assertEquals(now, prefs.getLong("last_read_timestamp", 0L))
    }
}

