package com.example.data.repository

import android.content.Context
import com.example.data.model.AthkarCategory
import com.example.data.model.AthkarItem
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

object AthkarOfflineDataSource {

    @Volatile
    private var cachedAthkar: List<AthkarItem>? = null

    /**
     * Returns the complete verified Azkar dataset:
     * 100% of existing IslamicData.athkarList + all expanded verified Azkar from assets.
     * Thread-safe, cached in-memory, fully offline.
     */
    @Synchronized
    fun getAthkar(context: Context): List<AthkarItem> {
        cachedAthkar?.let { return it }

        val combined = mutableListOf<AthkarItem>()

        // 1. Preserve all existing Azkar from IslamicData
        combined.addAll(IslamicData.athkarList)

        val existingIds = IslamicData.athkarList.map { it.id }.toSet()
        val existingTexts = IslamicData.athkarList.map { QuranOfflineDataSource.normalizeArabic(it.arabicText) }.toSet()

        // 2. Load expanded verified Azkar from assets/azkar/azkar_offline.json
        try {
            context.assets.open("azkar/azkar_offline.json").use { stream ->
                BufferedReader(InputStreamReader(stream, Charsets.UTF_8)).use { reader ->
                    val jsonArray = JSONArray(reader.readText())
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val id = obj.getString("id")
                        val catStr = obj.getString("category")
                        val category = try {
                            AthkarCategory.valueOf(catStr)
                        } catch (e: Exception) {
                            AthkarCategory.VARIOUS
                        }
                        val arabicText = obj.getString("arabicText")
                        val normText = QuranOfflineDataSource.normalizeArabic(arabicText)

                        // Strict deduplication guard
                        if (!existingIds.contains(id) && !existingTexts.contains(normText)) {
                            combined.add(
                                AthkarItem(
                                    id = id,
                                    category = category,
                                    titleArabic = obj.getString("titleArabic"),
                                    titleEnglish = obj.getString("titleEnglish"),
                                    arabicText = arabicText,
                                    transliteration = obj.optString("transliteration", ""),
                                    translationEnglish = obj.optString("translationEnglish", ""),
                                    virtue = obj.optString("virtue", ""),
                                    repeatTarget = obj.optInt("repeatTarget", 1)
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        cachedAthkar = combined
        return combined
    }

    fun getAthkarByCategory(context: Context, category: AthkarCategory): List<AthkarItem> {
        return getAthkar(context).filter { it.category == category }
    }
}
