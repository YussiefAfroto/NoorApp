package com.example.data.repository

import android.content.Context
import com.example.data.model.Ayah
import com.example.data.model.Surah
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

data class QuranAyahMatch(
    val surahNumber: Int,
    val surahNameArabic: String,
    val surahNameEnglish: String,
    val ayahNumber: Int,
    val arabicText: String
)

object QuranOfflineDataSource {

    @Volatile
    private var cachedSurahs: List<Surah>? = null

    /**
     * Retrieves all 114 Surahs with their complete, verified Uthmani Ayahs
     * loaded locally and offline from assets/quran/quran_offline.json.
     */
    @Synchronized
    fun getSurahs(context: Context): List<Surah> {
        cachedSurahs?.let { return it }

        val loadedSurahs = mutableListOf<Surah>()
        try {
            context.assets.open("quran/quran_offline.json").use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).use { reader ->
                    val jsonString = reader.readText()
                    val jsonArray = JSONArray(jsonString)

                    for (i in 0 until jsonArray.length()) {
                        val surahObj = jsonArray.getJSONObject(i)
                        val number = surahObj.getInt("number")
                        val nameArabic = surahObj.getString("nameArabic")
                        val nameEnglish = surahObj.getString("nameEnglish")
                        val englishMeaning = surahObj.optString("englishMeaning", "")
                        val revelationType = surahObj.getString("revelationType")
                        val totalVerses = surahObj.getInt("totalVerses")
                        val ayahsArray = surahObj.getJSONArray("ayahs")

                        val ayahs = ArrayList<Ayah>(ayahsArray.length())
                        for (j in 0 until ayahsArray.length()) {
                            val aObj = ayahsArray.getJSONObject(j)
                            ayahs.add(
                                Ayah(
                                    surahNumber = aObj.getInt("surahNumber"),
                                    ayahNumber = aObj.getInt("ayahNumber"),
                                    arabicText = aObj.getString("arabicText"),
                                    translationEnglish = "" // Arabic Quran only
                                )
                            )
                        }

                        loadedSurahs.add(
                            Surah(
                                number = number,
                                nameArabic = nameArabic,
                                nameEnglish = nameEnglish,
                                englishMeaning = englishMeaning,
                                revelationType = revelationType,
                                totalVerses = totalVerses,
                                ayahs = ayahs
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        cachedSurahs = loadedSurahs
        return loadedSurahs
    }

    /**
     * Retrieves a single Surah by its number (1 to 114).
     */
    fun getSurahByNumber(context: Context, number: Int): Surah? {
        return getSurahs(context).firstOrNull { it.number == number }
    }

    /**
     * Normalizes Arabic text for accurate offline search.
     * Strips Tashkeel/diacritics, dagger alefs, Tatweel, and Quranic pause marks,
     * and normalizes alefs, yaa, and taa marbuta.
     */
    fun normalizeArabic(text: String): String {
        return text
            .replace("[\u064B-\u065F\u0670\u06D6-\u06ED\u0640\u06E5\u06E6]".toRegex(), "")
            .replace("[إأآاٱ]".toRegex(), "ا")
            .replace("ى".toRegex(), "ي")
            .replace("ة".toRegex(), "ه")
            .replace("\\s+".toRegex(), " ")
            .trim()
    }

    /**
     * Searches all 114 Surahs and 6,236 Ayahs completely offline.
     */
    fun searchAyahs(context: Context, query: String, maxResults: Int = 50): List<QuranAyahMatch> {
        val cleanQuery = normalizeArabic(query)
        if (cleanQuery.isBlank()) return emptyList()

        val results = mutableListOf<QuranAyahMatch>()
        val surahs = getSurahs(context)

        for (surah in surahs) {
            for (ayah in surah.ayahs) {
                val normalizedVerse = normalizeArabic(ayah.arabicText)
                if (normalizedVerse.contains(cleanQuery)) {
                    results.add(
                        QuranAyahMatch(
                            surahNumber = surah.number,
                            surahNameArabic = surah.nameArabic,
                            surahNameEnglish = surah.nameEnglish,
                            ayahNumber = ayah.ayahNumber,
                            arabicText = ayah.arabicText
                        )
                    )
                    if (results.size >= maxResults) return results
                }
            }
        }
        return results
    }
}
