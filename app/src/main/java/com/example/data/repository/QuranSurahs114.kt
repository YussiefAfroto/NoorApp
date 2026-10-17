package com.example.data.repository

import com.example.data.model.Ayah
import com.example.data.model.Surah

object QuranSurahs114 {

    data class SurahMeta(
        val number: Int,
        val nameArabic: String,
        val nameEnglish: String,
        val englishMeaning: String,
        val revelationType: String, // "Meccan" or "Medinan"
        val totalVerses: Int,
        val juzNumber: Int
    ) {
        val revelationArabic: String
            get() = if (revelationType == "Meccan") "مكية" else "مدنية"
    }

    val all114SurahsMeta = listOf(
        SurahMeta(1, "الفاتحة", "Al-Fatihah", "The Opener", "Meccan", 7, 1),
        SurahMeta(2, "البقرة", "Al-Baqarah", "The Cow", "Medinan", 286, 1),
        SurahMeta(3, "آل عمران", "Ali 'Imran", "Family of Imran", "Medinan", 200, 3),
        SurahMeta(4, "النساء", "An-Nisa", "The Women", "Medinan", 176, 4),
        SurahMeta(5, "المائدة", "Al-Ma'idah", "The Table Spread", "Medinan", 120, 6),
        SurahMeta(6, "الأنعام", "Al-An'am", "The Cattle", "Meccan", 165, 7),
        SurahMeta(7, "الأعراف", "Al-A'raf", "The Heights", "Meccan", 206, 8),
        SurahMeta(8, "الأنفال", "Al-Anfal", "The Spoils of War", "Medinan", 75, 9),
        SurahMeta(9, "التوبة", "At-Tawbah", "The Repentance", "Medinan", 129, 10),
        SurahMeta(10, "يونس", "Yunus", "Jonah", "Meccan", 109, 11),
        SurahMeta(11, "هود", "Hud", "Hud", "Meccan", 123, 11),
        SurahMeta(12, "يوسف", "Yusuf", "Joseph", "Meccan", 111, 12),
        SurahMeta(13, "الرعد", "Ar-Ra'd", "The Thunder", "Medinan", 43, 13),
        SurahMeta(14, "إبراهيم", "Ibrahim", "Abraham", "Meccan", 52, 13),
        SurahMeta(15, "الحجر", "Al-Hijr", "The Rocky Tract", "Meccan", 99, 14),
        SurahMeta(16, "النحل", "An-Nahl", "The Bee", "Meccan", 128, 14),
        SurahMeta(17, "الإسراء", "Al-Isra", "The Night Journey", "Meccan", 111, 15),
        SurahMeta(18, "الكهف", "Al-Kahf", "The Cave", "Meccan", 110, 15),
        SurahMeta(19, "مريم", "Maryam", "Mary", "Meccan", 98, 16),
        SurahMeta(20, "طه", "Ta-Ha", "Ta-Ha", "Meccan", 135, 16),
        SurahMeta(21, "الأنبياء", "Al-Anbiya", "The Prophets", "Meccan", 112, 17),
        SurahMeta(22, "الحج", "Al-Hajj", "The Pilgrimage", "Medinan", 78, 17),
        SurahMeta(23, "المؤمنون", "Al-Mu'minun", "The Believers", "Meccan", 118, 18),
        SurahMeta(24, "النور", "An-Nur", "The Light", "Medinan", 64, 18),
        SurahMeta(25, "الفرقان", "Al-Furqan", "The Criterion", "Meccan", 77, 18),
        SurahMeta(26, "الشعراء", "Ash-Shu'ara", "The Poets", "Meccan", 227, 19),
        SurahMeta(27, "النمل", "An-Naml", "The Ant", "Meccan", 93, 19),
        SurahMeta(28, "القصص", "Al-Qasas", "The Stories", "Meccan", 88, 20),
        SurahMeta(29, "العنكبوت", "Al-Ankabut", "The Spider", "Meccan", 69, 20),
        SurahMeta(30, "الروم", "Ar-Rum", "The Romans", "Meccan", 60, 21),
        SurahMeta(31, "لقمان", "Luqman", "Luqman", "Meccan", 34, 21),
        SurahMeta(32, "السجدة", "As-Sajdah", "The Prostration", "Meccan", 30, 21),
        SurahMeta(33, "الأحزاب", "Al-Ahzab", "The Combined Forces", "Medinan", 73, 21),
        SurahMeta(34, "سبأ", "Saba", "Sheba", "Meccan", 54, 22),
        SurahMeta(35, "فاطر", "Fatir", "The Originator", "Meccan", 45, 22),
        SurahMeta(36, "يس", "Ya-Sin", "Ya-Sin", "Meccan", 83, 22),
        SurahMeta(37, "الصافات", "As-Saffat", "Those who set the Ranks", "Meccan", 182, 23),
        SurahMeta(38, "ص", "Sad", "The Letter 'Saad'", "Meccan", 88, 23),
        SurahMeta(39, "الزمر", "Az-Zumar", "The Troops", "Meccan", 75, 23),
        SurahMeta(40, "غافر", "Ghafir", "The Forgiver", "Meccan", 85, 24),
        SurahMeta(41, "فصلت", "Fussilat", "Explained in Detail", "Meccan", 54, 24),
        SurahMeta(42, "الشورى", "Ash-Shura", "The Consultation", "Meccan", 53, 25),
        SurahMeta(43, "الزخرف", "Az-Zukhruf", "The Ornaments of Gold", "Meccan", 89, 25),
        SurahMeta(44, "الدخان", "Ad-Dukhan", "The Smoke", "Meccan", 59, 25),
        SurahMeta(45, "الجاثية", "Al-Jathiyah", "The Crouching", "Meccan", 37, 25),
        SurahMeta(46, "الأحقاف", "Al-Ahqaf", "The Wind-Curved Sandhills", "Meccan", 35, 26),
        SurahMeta(47, "محمد", "Muhammad", "Muhammad", "Medinan", 38, 26),
        SurahMeta(48, "الفتح", "Al-Fath", "The Victory", "Medinan", 29, 26),
        SurahMeta(49, "الحجرات", "Al-Hujurat", "The Rooms", "Medinan", 18, 26),
        SurahMeta(50, "ق", "Qaf", "The Letter 'Qaf'", "Meccan", 45, 26),
        SurahMeta(51, "الذاريات", "Adh-Dhariyat", "The Winnowing Winds", "Meccan", 60, 26),
        SurahMeta(52, "الطور", "At-Tur", "The Mount", "Meccan", 49, 27),
        SurahMeta(53, "النجم", "An-Najm", "The Star", "Meccan", 62, 27),
        SurahMeta(54, "القمر", "Al-Qamar", "The Moon", "Meccan", 55, 27),
        SurahMeta(55, "الرحمن", "Ar-Rahman", "The Beneficent", "Medinan", 78, 27),
        SurahMeta(56, "الواقعة", "Al-Waqi'ah", "The Inevitable", "Meccan", 96, 27),
        SurahMeta(57, "الحديد", "Al-Hadid", "The Iron", "Medinan", 29, 27),
        SurahMeta(58, "المجادلة", "Al-Mujadila", "The Pleading Woman", "Medinan", 22, 28),
        SurahMeta(59, "الحشر", "Al-Hashr", "The Exile", "Medinan", 24, 28),
        SurahMeta(60, "الممتحنة", "Al-Mumtahanah", "She that is to be examined", "Medinan", 13, 28),
        SurahMeta(61, "الصف", "As-Saff", "The Ranks", "Medinan", 14, 28),
        SurahMeta(62, "الجمعة", "Al-Jumu'ah", "Friday", "Medinan", 11, 28),
        SurahMeta(63, "المنافقون", "Al-Munafiqun", "The Hypocrites", "Medinan", 11, 28),
        SurahMeta(64, "التغابن", "At-Taghabun", "Mutual Disillusion", "Medinan", 18, 28),
        SurahMeta(65, "الطلاق", "At-Talaq", "The Divorce", "Medinan", 12, 28),
        SurahMeta(66, "التحريم", "At-Tahrim", "The Prohibition", "Medinan", 12, 28),
        SurahMeta(67, "الملك", "Al-Mulk", "The Sovereignty", "Meccan", 30, 29),
        SurahMeta(68, "القلم", "Al-Qalam", "The Pen", "Meccan", 52, 29),
        SurahMeta(69, "الحاقة", "Al-Haqqah", "The Reality", "Meccan", 52, 29),
        SurahMeta(70, "المعارج", "Al-Ma'arij", "The Ascending Stairways", "Meccan", 44, 29),
        SurahMeta(71, "نوح", "Nuh", "Noah", "Meccan", 28, 29),
        SurahMeta(72, "الجن", "Al-Jinn", "The Jinn", "Meccan", 28, 29),
        SurahMeta(73, "المزمل", "Al-Muzzammil", "The Enshrouded One", "Meccan", 20, 29),
        SurahMeta(74, "المدثر", "Al-Muddaththir", "The Cloaked One", "Meccan", 56, 29),
        SurahMeta(75, "القيامة", "Al-Qiyamah", "The Resurrection", "Meccan", 40, 29),
        SurahMeta(76, "الإنسان", "Al-Insan", "Man", "Medinan", 31, 29),
        SurahMeta(77, "المرسلات", "Al-Mursalat", "The Emissaries", "Meccan", 50, 29),
        SurahMeta(78, "النبأ", "An-Naba", "The Tidings", "Meccan", 40, 30),
        SurahMeta(79, "النازعات", "An-Nazi'at", "Those who drag forth", "Meccan", 46, 30),
        SurahMeta(80, "عبس", "Abasa", "He Frowned", "Meccan", 42, 30),
        SurahMeta(81, "التكوير", "At-Takwir", "The Overthrowing", "Meccan", 29, 30),
        SurahMeta(82, "الانفطار", "Al-Infitar", "The Cleaving", "Meccan", 19, 30),
        SurahMeta(83, "المطففين", "Al-Mutaffifin", "Defrauding", "Meccan", 36, 30),
        SurahMeta(84, "الانشقاق", "Al-Inshiqaq", "The Splitting Open", "Meccan", 25, 30),
        SurahMeta(85, "البروج", "Al-Buruj", "The Mansions of the Stars", "Meccan", 22, 30),
        SurahMeta(86, "الطارق", "At-Tariq", "The Nightcommer", "Meccan", 17, 30),
        SurahMeta(87, "الأعلى", "Al-A'la", "The Most High", "Meccan", 19, 30),
        SurahMeta(88, "الغاشية", "Al-Ghashiyah", "The Overwhelming", "Meccan", 26, 30),
        SurahMeta(89, "الفجر", "Al-Fajr", "The Dawn", "Meccan", 30, 30),
        SurahMeta(90, "البلد", "Al-Balad", "The City", "Meccan", 20, 30),
        SurahMeta(91, "الشمس", "Ash-Shams", "The Sun", "Meccan", 15, 30),
        SurahMeta(92, "الليل", "Al-Layl", "The Night", "Meccan", 21, 30),
        SurahMeta(93, "الضحى", "Ad-Duha", "The Morning Hours", "Meccan", 11, 30),
        SurahMeta(94, "الشرح", "Ash-Sharh", "The Relief", "Meccan", 8, 30),
        SurahMeta(95, "التين", "At-Tin", "The Fig", "Meccan", 8, 30),
        SurahMeta(96, "العلق", "Al-Alaq", "The Clot", "Meccan", 19, 30),
        SurahMeta(97, "القدر", "Al-Qadr", "The Power", "Meccan", 5, 30),
        SurahMeta(98, "البينة", "Al-Bayyinah", "The Clear Proof", "Medinan", 8, 30),
        SurahMeta(99, "الزلزلة", "Az-Zalzalah", "The Earthquake", "Medinan", 8, 30),
        SurahMeta(100, "العاديات", "Al-Adiyat", "The Courser", "Meccan", 11, 30),
        SurahMeta(101, "القارعة", "Al-Qari'ah", "The Calamity", "Meccan", 11, 30),
        SurahMeta(102, "التكاثر", "At-Takathur", "The Rivalry in World Increase", "Meccan", 8, 30),
        SurahMeta(103, "العصر", "Al-Asr", "The Declining Day", "Meccan", 3, 30),
        SurahMeta(104, "الهمزة", "Al-Humazah", "The Traducer", "Meccan", 9, 30),
        SurahMeta(105, "الفيل", "Al-Fil", "The Elephant", "Meccan", 5, 30),
        SurahMeta(106, "قريش", "Quraysh", "Quraysh", "Meccan", 4, 30),
        SurahMeta(107, "الماعون", "Al-Ma'un", "Small kindnesses", "Meccan", 7, 30),
        SurahMeta(108, "الكوثر", "Al-Kawthar", "Abundance", "Meccan", 3, 30),
        SurahMeta(109, "الكافرون", "Al-Kafirun", "The Disbelievers", "Meccan", 6, 30),
        SurahMeta(110, "النصر", "An-Nasr", "The Help", "Medinan", 3, 30),
        SurahMeta(111, "المسد", "Al-Masad", "The Palm Fiber", "Meccan", 5, 30),
        SurahMeta(112, "الإخلاص", "Al-Ikhlas", "Sincerity", "Meccan", 4, 30),
        SurahMeta(113, "الفلق", "Al-Falaq", "The Daybreak", "Meccan", 5, 30),
        SurahMeta(114, "الناس", "An-Nas", "Mankind", "Meccan", 6, 30)
    )

    // Convert metadata to Surah object using verified local offline Quran dataset
    fun toSurah(meta: SurahMeta, context: android.content.Context? = null): Surah {
        if (context != null) {
            val verified = QuranOfflineDataSource.getSurahByNumber(context, meta.number)
            if (verified != null) return verified
        }
        return Surah(
            number = meta.number,
            nameArabic = meta.nameArabic,
            nameEnglish = meta.nameEnglish,
            englishMeaning = meta.englishMeaning,
            revelationType = meta.revelationType,
            totalVerses = meta.totalVerses,
            ayahs = emptyList()
        )
    }
}
