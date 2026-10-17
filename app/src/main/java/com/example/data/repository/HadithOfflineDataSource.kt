package com.example.data.repository

import android.content.Context
import com.example.data.model.Hadith
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

object HadithOfflineDataSource {

    @Volatile
    private var cachedHadiths: List<Hadith>? = null

    /**
     * Returns the full verified Hadiths dataset:
     * 100% of existing IslamicData.hadiths + all expanded verified Hadiths from assets.
     * Thread-safe, cached in-memory, fully offline.
     */
    @Synchronized
    fun getHadiths(context: Context): List<Hadith> {
        cachedHadiths?.let { return it }

        val combined = mutableListOf<Hadith>()

        // 1. Preserve all existing Hadiths from IslamicData
        combined.addAll(IslamicData.hadiths)

        val existingIds = IslamicData.hadiths.map { it.id }.toSet()
        val existingMatns = IslamicData.hadiths.map { QuranOfflineDataSource.normalizeArabic(it.matnArabic) }.toSet()

        // 2. Load expanded verified Hadiths from assets/hadith/hadith_offline.json
        try {
            context.assets.open("hadith/hadith_offline.json").use { stream ->
                BufferedReader(InputStreamReader(stream, Charsets.UTF_8)).use { reader ->
                    val jsonArray = JSONArray(reader.readText())
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val id = obj.getString("id")
                        val matn = obj.getString("matnArabic")
                        val normMatn = QuranOfflineDataSource.normalizeArabic(matn)

                        // Strict deduplication guard
                        if (!existingIds.contains(id) && !existingMatns.contains(normMatn)) {
                            combined.add(
                                Hadith(
                                    id = id,
                                    collectionName = obj.getString("collectionName"),
                                    chapter = obj.getString("chapter"),
                                    hadithNumber = obj.getInt("hadithNumber"),
                                    narratorArabic = obj.getString("narratorArabic"),
                                    matnArabic = matn,
                                    sourceArabic = obj.getString("sourceArabic"),
                                    englishMeaning = obj.optString("englishMeaning", ""),
                                    categoryArabic = obj.optString("categoryArabic", "العبادات"),
                                    gradeArabic = obj.optString("gradeArabic", "صحيح")
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        cachedHadiths = combined
        return combined
    }

    /**
     * Fast offline search across matn, narrator, book, and chapter
     * using Arabic diacritic normalization.
     */
    fun searchHadiths(context: Context, query: String, category: String = "الكل"): List<Hadith> {
        val all = getHadiths(context)
        val filteredByCategory = if (category == "الكل") all else all.filter { it.categoryArabic == category }

        val cleanQuery = QuranOfflineDataSource.normalizeArabic(query)
        if (cleanQuery.isBlank()) return filteredByCategory

        return filteredByCategory.filter { hadith ->
            QuranOfflineDataSource.normalizeArabic(hadith.matnArabic).contains(cleanQuery) ||
            QuranOfflineDataSource.normalizeArabic(hadith.narratorArabic).contains(cleanQuery) ||
            QuranOfflineDataSource.normalizeArabic(hadith.bookArabic).contains(cleanQuery) ||
            QuranOfflineDataSource.normalizeArabic(hadith.chapterArabic).contains(cleanQuery)
        }
    }
}
