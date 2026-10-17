package com.example.data.model

data class Hadith(
    val id: String,
    val collectionName: String, // e.g. "الأربعين النووية", "صحيح البخاري", "رياض الصالحين"
    val chapter: String,
    val hadithNumber: Int,
    val narratorArabic: String,
    val matnArabic: String,
    val sourceArabic: String,
    val englishMeaning: String = "",
    val categoryArabic: String = "العبادات",
    val gradeArabic: String = "صحيح"
) {
    val bookArabic: String get() = collectionName
    val chapterArabic: String get() = chapter
    val translationEnglish: String get() = englishMeaning
}
