package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.HadithOfflineDataSource
import com.example.data.repository.IslamicData
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
class HadithOfflineVerificationTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun verifyExistingHadithsPreserved() {
        val allHadiths = HadithOfflineDataSource.getHadiths(context)
        val existingOriginal = IslamicData.hadiths

        assertTrue("Dataset must contain more hadiths than original 6", allHadiths.size > existingOriginal.size)

        for (original in existingOriginal) {
            val found = allHadiths.firstOrNull { it.id == original.id }
            assertNotNull("Original Hadith ${original.id} must be preserved", found)
            assertEquals("Matn must match exactly for ${original.id}", original.matnArabic, found!!.matnArabic)
            assertEquals("Narrator must match exactly for ${original.id}", original.narratorArabic, found.narratorArabic)
            assertEquals("Book must match exactly for ${original.id}", original.bookArabic, found.bookArabic)
        }
    }

    @Test
    fun verifyNoDuplicateHadithsOrIds() {
        val allHadiths = HadithOfflineDataSource.getHadiths(context)
        val seenIds = mutableSetOf<String>()
        val seenMatns = mutableSetOf<String>()

        for (hadith in allHadiths) {
            assertFalse("Duplicate Hadith ID found: ${hadith.id}", seenIds.contains(hadith.id))
            seenIds.add(hadith.id)

            val cleanMatn = hadith.matnArabic.replace("\\s+".toRegex(), " ").trim()
            assertFalse("Duplicate Hadith text found for ID ${hadith.id}", seenMatns.contains(cleanMatn))
            seenMatns.add(cleanMatn)
        }
    }

    @Test
    fun verifyHadithMetadataIntegrity() {
        val allHadiths = HadithOfflineDataSource.getHadiths(context)
        val placeholderWords = listOf("placeholder", "dummy", "sample", "lorem", "fake")

        for (hadith in allHadiths) {
            assertTrue("Hadith ${hadith.id} must have non-blank ID", hadith.id.isNotBlank())
            assertTrue("Hadith ${hadith.id} must have non-blank matn", hadith.matnArabic.isNotBlank())
            assertTrue("Hadith ${hadith.id} must have non-blank narrator", hadith.narratorArabic.isNotBlank())
            assertTrue("Hadith ${hadith.id} must have non-blank book/collection", hadith.bookArabic.isNotBlank())
            assertTrue("Hadith ${hadith.id} must have non-blank chapter", hadith.chapterArabic.isNotBlank())
            assertTrue("Hadith ${hadith.id} must have non-blank source", hadith.sourceArabic.isNotBlank())
            assertTrue("Hadith ${hadith.id} must have non-blank grade", hadith.gradeArabic.isNotBlank())
            assertTrue("Hadith ${hadith.id} must have non-blank category", hadith.categoryArabic.isNotBlank())
            assertTrue("Hadith number must be positive for ${hadith.id}", hadith.hadithNumber > 0)

            for (pw in placeholderWords) {
                assertFalse("Hadith ${hadith.id} must not contain '$pw'", hadith.matnArabic.contains(pw, ignoreCase = true))
                assertFalse("Hadith ${hadith.id} narrator must not contain '$pw'", hadith.narratorArabic.contains(pw, ignoreCase = true))
            }
        }
    }

    @Test
    fun verifyCategoriesFiltering() {
        val allHadiths = HadithOfflineDataSource.getHadiths(context)
        val categories = listOf("الكل", "الإيمان", "الأخلاق", "العبادات", "العلم", "الآداب والمعاملات", "السيرة النبوية")

        for (cat in categories) {
            val results = HadithOfflineDataSource.searchHadiths(context, query = "", category = cat)
            if (cat == "الكل") {
                assertEquals("All category must match total hadiths", allHadiths.size, results.size)
            } else {
                assertTrue("Category '$cat' must have hadiths", results.isNotEmpty())
                for (h in results) {
                    assertEquals("Hadith ${h.id} category mismatch", cat, h.categoryArabic)
                }
            }
        }
    }

    @Test
    fun verifyOfflineSearch() {
        // Search by keyword in matn
        val resultsNiyyah = HadithOfflineDataSource.searchHadiths(context, query = "النيات")
        assertTrue("Search for 'النيات' must return results", resultsNiyyah.isNotEmpty())
        assertTrue(resultsNiyyah.any { it.id == "nawawi_1" })

        // Search by keyword in narrator
        val resultsAbuHuraira = HadithOfflineDataSource.searchHadiths(context, query = "أبي هريرة")
        assertTrue("Search for 'أبي هريرة' must return results", resultsAbuHuraira.isNotEmpty())

        // Search with diacritic insensitivity (normalization)
        val resultsSalatWithAl = HadithOfflineDataSource.searchHadiths(context, query = "الصلاة")
        val resultsSalatWithoutAl = HadithOfflineDataSource.searchHadiths(context, query = "صلاة")
        assertTrue("Search for 'الصلاة' must return results", resultsSalatWithAl.isNotEmpty())
        assertTrue("Search for 'صلاة' must return results", resultsSalatWithoutAl.isNotEmpty())
    }
}
