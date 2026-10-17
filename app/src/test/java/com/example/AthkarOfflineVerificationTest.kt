package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AthkarCategory
import com.example.data.repository.AthkarOfflineDataSource
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
class AthkarOfflineVerificationTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun verifyExistingAthkarPreserved() {
        val allAthkar = AthkarOfflineDataSource.getAthkar(context)
        val originalList = IslamicData.athkarList

        assertTrue("Dataset must contain more items than the original 12", allAthkar.size > originalList.size)

        for (orig in originalList) {
            val found = allAthkar.firstOrNull { it.id == orig.id }
            assertNotNull("Original Azkar ${orig.id} must be preserved", found)
            assertEquals("Category must match for ${orig.id}", orig.category, found!!.category)
            assertEquals("Title Arabic must match for ${orig.id}", orig.titleArabic, found.titleArabic)
            assertEquals("Arabic text must match for ${orig.id}", orig.arabicText, found.arabicText)
            assertEquals("Repeat target must match for ${orig.id}", orig.repeatTarget, found.repeatTarget)
        }
    }

    @Test
    fun verifyNoDuplicateAthkar() {
        val allAthkar = AthkarOfflineDataSource.getAthkar(context)
        val seenIds = mutableSetOf<String>()
        val seenTexts = mutableSetOf<String>()

        for (item in allAthkar) {
            assertFalse("Duplicate Azkar ID: ${item.id}", seenIds.contains(item.id))
            seenIds.add(item.id)

            val cleanText = item.arabicText.replace("\\s+".toRegex(), " ").trim()
            assertFalse("Duplicate Azkar text in ID ${item.id}", seenTexts.contains(cleanText))
            seenTexts.add(cleanText)
        }
    }

    @Test
    fun verifyAllCategoriesHaveItems() {
        for (category in AthkarCategory.values()) {
            val items = AthkarOfflineDataSource.getAthkarByCategory(context, category)
            assertTrue("Category '${category.name}' (${category.titleArabic}) must have items", items.isNotEmpty())
            for (item in items) {
                assertEquals("Item ${item.id} category mismatch", category, item.category)
            }
        }
    }

    @Test
    fun verifyAthkarDataIntegrity() {
        val allAthkar = AthkarOfflineDataSource.getAthkar(context)
        val placeholderWords = listOf("placeholder", "dummy", "sample", "lorem", "fake")

        for (item in allAthkar) {
            assertTrue("Item ${item.id} must have non-blank ID", item.id.isNotBlank())
            assertTrue("Item ${item.id} must have non-blank Arabic title", item.titleArabic.isNotBlank())
            assertTrue("Item ${item.id} must have non-blank English title", item.titleEnglish.isNotBlank())
            assertTrue("Item ${item.id} must have non-blank Arabic text", item.arabicText.isNotBlank())
            assertTrue("Item ${item.id} repeatTarget must be >= 1", item.repeatTarget >= 1)

            for (pw in placeholderWords) {
                assertFalse("Item ${item.id} contains '$pw'", item.arabicText.contains(pw, ignoreCase = true))
                assertFalse("Item ${item.id} title contains '$pw'", item.titleArabic.contains(pw, ignoreCase = true))
            }
        }
    }
}
