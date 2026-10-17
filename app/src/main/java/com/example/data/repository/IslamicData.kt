package com.example.data.repository

import com.example.data.model.AthkarCategory
import com.example.data.model.AthkarItem
import com.example.data.model.Ayah
import com.example.data.model.CityPrayerPreset
import com.example.data.model.DhikrPreset
import com.example.data.model.Hadith
import com.example.data.model.Surah

object IslamicData {

    val cities = listOf(
        CityPrayerPreset(
            cityNameEnglish = "Cairo",
            cityNameArabic = "القاهرة",
            country = "مصر",
            latitude = 30.0444,
            longitude = 31.2357,
            qiblaDegree = 136f,
            distanceToKaabaKm = 1285
        ),
        CityPrayerPreset(
            cityNameEnglish = "Makkah",
            cityNameArabic = "مكة المكرمة",
            country = "Saudi Arabia",
            latitude = 21.4225,
            longitude = 39.8262,
            qiblaDegree = 0f,
            distanceToKaabaKm = 0
        ),
        CityPrayerPreset(
            cityNameEnglish = "Madinah",
            cityNameArabic = "المدينة المنورة",
            country = "Saudi Arabia",
            latitude = 24.4672,
            longitude = 39.6111,
            qiblaDegree = 177f,
            distanceToKaabaKm = 340
        ),
        CityPrayerPreset(
            cityNameEnglish = "Jerusalem",
            cityNameArabic = "القدس الشريف",
            country = "Palestine",
            latitude = 31.7683,
            longitude = 35.2137,
            qiblaDegree = 157f,
            distanceToKaabaKm = 1240
        ),
        CityPrayerPreset(
            cityNameEnglish = "Riyadh",
            cityNameArabic = "الرياض",
            country = "المملكة العربية السعودية",
            latitude = 24.7136,
            longitude = 46.6753,
            qiblaDegree = 245f,
            distanceToKaabaKm = 790
        ),
        CityPrayerPreset(
            cityNameEnglish = "Dubai",
            cityNameArabic = "دبي",
            country = "United Arab Emirates",
            latitude = 25.2048,
            longitude = 55.2708,
            qiblaDegree = 262f,
            distanceToKaabaKm = 1650
        ),
        CityPrayerPreset(
            cityNameEnglish = "Istanbul",
            cityNameArabic = "إسطنبول",
            country = "Turkey",
            latitude = 41.0082,
            longitude = 28.9784,
            qiblaDegree = 151f,
            distanceToKaabaKm = 2400
        ),
        CityPrayerPreset(
            cityNameEnglish = "London",
            cityNameArabic = "لندن",
            country = "United Kingdom",
            latitude = 51.5074,
            longitude = -0.1278,
            qiblaDegree = 119f,
            distanceToKaabaKm = 4800
        ),
        CityPrayerPreset(
            cityNameEnglish = "New York",
            cityNameArabic = "نيويورك",
            country = "USA",
            latitude = 40.7128,
            longitude = -74.0060,
            qiblaDegree = 58f,
            distanceToKaabaKm = 10280
        )
    )

    val dhikrPresets = listOf(
        DhikrPreset(
            id = "subhanallah",
            arabicText = "سُبْحَانَ اللَّهِ",
            transliteration = "SubhanAllah",
            englishMeaning = "Glory be to Allah",
            defaultTarget = 33
        ),
        DhikrPreset(
            id = "alhamdulillah",
            arabicText = "الْحَمْدُ لِلَّهِ",
            transliteration = "Alhamdulillah",
            englishMeaning = "Praise be to Allah",
            defaultTarget = 33
        ),
        DhikrPreset(
            id = "allahuakbar",
            arabicText = "اللَّهُ أَكْبَرُ",
            transliteration = "Allahu Akbar",
            englishMeaning = "Allah is the Greatest",
            defaultTarget = 34
        ),
        DhikrPreset(
            id = "lailahaillallah",
            arabicText = "لَا إِلَهَ إِلَّا اللَّهُ",
            transliteration = "La ilaha illa Allah",
            englishMeaning = "There is no god but Allah",
            defaultTarget = 100
        ),
        DhikrPreset(
            id = "astaghfirullah",
            arabicText = "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ",
            transliteration = "Astaghfirullah wa atubu ilayh",
            englishMeaning = "I seek forgiveness from Allah and turn to Him",
            defaultTarget = 100
        ),
        DhikrPreset(
            id = "salawat",
            arabicText = "اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ",
            transliteration = "Allahumma salli 'ala Sayyidina Muhammad",
            englishMeaning = "O Allah, send blessings upon our Master Muhammad",
            defaultTarget = 100
        ),
        DhikrPreset(
            id = "hawqala",
            arabicText = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
            transliteration = "La hawla wa la quwwata illa billah",
            englishMeaning = "There is no power nor strength except with Allah",
            defaultTarget = 33
        ),
        DhikrPreset(
            id = "subhanallah_bihamdihi",
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ ، سُبْحَانَ اللَّهِ الْعَظِيمِ",
            transliteration = "SubhanAllahi wa bihamdihi, SubhanAllahil 'Adheem",
            englishMeaning = "Glory be to Allah and His praise, Glory be to Allah the Supreme",
            defaultTarget = 100
        )
    )

    val dailyAyahOfTheDay = Ayah(
        surahNumber = 24,
        ayahNumber = 35,
        arabicText = "اللَّهُ نُورُ السَّمَاوَاتِ وَالْأَرْضِ ۚ مَثَلُ نُورِهِ كَمِشْكَاةٍ فِيهَا مِصْبَاحٌ ۖ الْمِصْبَاحُ فِي زُجَاجَةٍ ۖ الزُّجَاجَةُ كَأَنَّهَا كَوْكَبٌ دُرِّيٌّ يُوقَدُ مِن شَجَرَةٍ مُّبَارَكَةٍ زَيْتُونَةٍ لَّا شَرْقِيَّةٍ وَلَا غَرْبِيَّةٍ يَكَادُ زَيْتُهَا يُضِيءُ وَلَوْ لَمْ تَمْسَسْهُ نَارٌ ۚ نُّورٌ عَلَىٰ نُورٍ ۗ يَهْدِي اللَّهُ لِنُورِهِ مَن يَشَاءُ",
        translationEnglish = "Allah is the Light of the heavens and the earth. The example of His light is like a niche wherein is a lamp; the lamp is in a glass, the glass as if it were a brilliant star lit from a blessed olive tree, neither of the east nor of the west, whose oil would almost glow even if untouched by fire. Light upon light. Allah guides to His light whom He wills.",
        tafsirShort = "Ayat An-Nur: A sublime parable of Divine Illumination penetrating receptive pure hearts with guidance and peace."
    )

    val surahs: List<Surah> = listOf(
        Surah(
            number = 1,
            nameArabic = "الفاتحة",
            nameEnglish = "Al-Fatihah",
            englishMeaning = "The Opening",
            revelationType = "Meccan",
            totalVerses = 7,
            ayahs = listOf(
                Ayah(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "In the name of Allah, the Entirely Merciful, the Especially Merciful."),
                Ayah(1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "[All] praise is [due] to Allah, Lord of the worlds -"),
                Ayah(1, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "The Entirely Merciful, the Especially Merciful,"),
                Ayah(1, 4, "مَالِكِ يَوْمِ الدِّينِ", "Sovereign of the Day of Recompense."),
                Ayah(1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "It is You we worship and You we ask for help."),
                Ayah(1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Guide us to the straight path -"),
                Ayah(1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.")
            )
        ),
        Surah(
            number = 2,
            nameArabic = "البقرة",
            nameEnglish = "Al-Baqarah",
            englishMeaning = "The Cow",
            revelationType = "Medinan",
            totalVerses = 286,
            ayahs = listOf(
                Ayah(2, 1, "الم", "Alif, Lam, Meem."),
                Ayah(2, 2, "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ", "This is the Book about which there is no doubt, a guidance for those conscious of Allah -"),
                Ayah(2, 3, "الَّذِينَ يُؤْمِنُونَ بِالْغَيْبِ وَيُقِيمُونَ الصَّلَاةَ وَمِمَّا رَزَقْنَاهُمْ يُنفِقُونَ", "Who believe in the unseen, establish prayer, and spend out of what We have provided for them,"),
                Ayah(2, 255, "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ", "Allah - there is no deity except Him, the Ever-Living, the Sustainer of [all] existence. Neither drowsiness overtakes Him nor sleep...")
            )
        ),
        Surah(
            number = 3,
            nameArabic = "آل عمران",
            nameEnglish = "Ali 'Imran",
            englishMeaning = "Family of Imran",
            revelationType = "Medinan",
            totalVerses = 200,
            ayahs = listOf(
                Ayah(3, 1, "الم", "Alif, Lam, Meem."),
                Ayah(3, 2, "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ", "Allah - there is no deity except Him, the Ever-Living, the Sustainer of existence."),
                Ayah(3, 3, "نَزَّلَ عَلَيْكَ الْكِتَابَ بِالْحَقِّ مُصَدِّقًا لِّمَا بَيْنَ يَدَيْهِ", "He has sent down upon you, [O Muhammad], the Book in truth, confirming what was before it...")
            )
        ),
        Surah(
            number = 4,
            nameArabic = "النساء",
            nameEnglish = "An-Nisa",
            englishMeaning = "The Women",
            revelationType = "Medinan",
            totalVerses = 176,
            ayahs = listOf(
                Ayah(4, 1, "يَا أَيُّهَا النَّاسُ اتَّقُوا رَبَّكُمُ الَّذِي خَلَقَكُم مِّن نَّفْسٍ وَاحِدَةٍ", "O mankind, fear your Lord, who created you from one soul and created from it its mate..."),
                Ayah(4, 103, "إِنَّ الصَّلَاةَ كَانَتْ عَلَى الْمُؤْمِنِينَ كِتَابًا مَّوْقُوتًا", "Indeed, prayer has been decreed upon the believers a decree of specified times.")
            )
        ),
        Surah(
            number = 18,
            nameArabic = "الكهف",
            nameEnglish = "Al-Kahf",
            englishMeaning = "The Cave",
            revelationType = "Meccan",
            totalVerses = 110,
            ayahs = listOf(
                Ayah(18, 1, "الْحَمْدُ لِلَّهِ الَّذِي أَنزَلَ عَلَىٰ عَبْدِهِ الْكِتَابَ وَلَمْ يَجْعَل لَّهُ عِوَجًا ۜ", "[All] praise is [due] to Allah, who has sent down upon His Servant the Book and has not made therein any deviance."),
                Ayah(18, 2, "قَيِّمًا لِّيُنذِرَ بَأْسًا شَدِيدًا مِّن لَّدُنْهُ وَيُبَشِّرَ الْمُؤْمِنِينَ الَّذِينَ يَعْمَلُونَ الصَّالِحَاتِ أَنَّ لَهُمْ أَجْرًا حَسَنًا", "[He has made it] straight, to warn of severe punishment from Him and to give good tidings to the believers..."),
                Ayah(18, 10, "إِذْ أَوَى الْفِتْيَةُ إِلَى الْكَهْفِ فَقَالُوا رَبَّنَا آتِنَا مِن لَّدُنكَ رَحْمَةً وَهَيِّئْ لَنَا مِنْ أَمْرِنَا رَشَدًا", "When the youths retreated to the cave and said, 'Our Lord, grant us from Yourself mercy and prepare for us from our affair right guidance.'"),
                Ayah(18, 46, "الْمَالُ وَالْبَنُونَ زِينَةُ الْحَيَاةِ الدُّنْيَا ۖ وَالْبَاقِيَاتُ الصَّالِحَاتُ خَيْرٌ عِندَ رَبِّكَ ثَوَابًا وَخَيْرٌ أَمَلًا", "Wealth and children are [but] adornment of the worldly life. But the enduring good deeds are better to your Lord for reward and better for [one's] hope.")
            )
        ),
        Surah(
            number = 24,
            nameArabic = "النور",
            nameEnglish = "An-Nur",
            englishMeaning = "The Light",
            revelationType = "Medinan",
            totalVerses = 64,
            ayahs = listOf(
                Ayah(24, 35, "اللَّهُ نُورُ السَّمَاوَاتِ وَالْأَرْضِ ۚ مَثَلُ نُورِهِ كَمِشْكَاةٍ فِيهَا مِصْبَاحٌ ۖ الْمِصْبَاحُ فِي زُجَاجَةٍ ۖ الزُّجَاجَةُ كَأَنَّهَا كَوْكَبٌ دُرِّيٌّ يُوقَدُ مِن شَجَرَةٍ مُّبَارَكَةٍ زَيْتُونَةٍ لَّا شَرْقِيَّةٍ وَلَا غَرْبِيَّةٍ يَكَادُ زَيْتُهَا يُضِيءُ وَلَوْ لَمْ تَمْسَسْهُ نَارٌ ۚ نُّورٌ عَلَىٰ نُورٍ ۗ يَهْدِي اللَّهُ لِنُورِهِ مَن يَشَاءُ", "Allah is the Light of the heavens and the earth. The parable of His light is as a niche wherein is a lamp... Light upon light. Allah guides to His light whom He wills."),
                Ayah(24, 36, "فِي بُيُوتٍ أَذِنَ اللَّهُ أَن تُرْفَعَ وَيُذْكَرَ فِيهَا اسْمُهُ يُسَبِّحُ لَهُ فِيهَا بِالْغُدُوِّ وَالْآصَالِ", "[Such niches are] in mosques which Allah has ordered to be raised and that His name be mentioned therein; exalting Him within them in the mornings and the evenings"),
                Ayah(24, 37, "رِجَالٌ لَّا تُلْهِيهِمْ تِجَارَةٌ وَلَا بَيْعٌ عَن ذِكْرِ اللَّهِ وَإِقَامِ الصَّلَاةِ وَإِيتَاءِ الزَّكَاةِ ۙ يَخَافُونَ يَوْمًا تَتَقَلَّبُ فِيهِ الْقُلُوبُ وَالْأَبْصَارُ", "Men whom neither commerce nor sale distracts from the remembrance of Allah and performance of prayer and giving of zakah. They fear a Day in which hearts and eyes will become overturned.")
            )
        ),
        Surah(
            number = 36,
            nameArabic = "يس",
            nameEnglish = "Ya-Sin",
            englishMeaning = "Ya-Sin",
            revelationType = "Meccan",
            totalVerses = 83,
            ayahs = listOf(
                Ayah(36, 1, "يس", "Ya, Seen."),
                Ayah(36, 2, "وَالْقُرْآنِ الْحَكِيمِ", "By the wise Qur'an."),
                Ayah(36, 3, "إِنَّكَ لَمِنَ الْمُرْسَلِينَ", "Indeed you, [O Muhammad], are from among the messengers,"),
                Ayah(36, 4, "عَلَىٰ صِرَاطٍ مُّسْتَقِيمٍ", "On a straight path."),
                Ayah(36, 5, "تَنزِيلَ الْعَزِيزِ الرَّحِيمِ", "[This is] a revelation of the Exalted in Might, the Merciful,")
            )
        ),
        Surah(
            number = 55,
            nameArabic = "الرحمن",
            nameEnglish = "Ar-Rahman",
            englishMeaning = "The Beneficent",
            revelationType = "Medinan",
            totalVerses = 78,
            ayahs = listOf(
                Ayah(55, 1, "الرَّحْمَٰنُ", "The Most Merciful"),
                Ayah(55, 2, "عَلَّمَ الْقُرْآنَ", "Taught the Qur'an,"),
                Ayah(55, 3, "خَلَقَ الْإِنسَانَ", "Created man,"),
                Ayah(55, 4, "عَلَّمَهُ الْبَيَانَ", "[And] taught him eloquence."),
                Ayah(55, 13, "فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ", "So which of the favors of your Lord would you deny?")
            )
        ),
        Surah(
            number = 67,
            nameArabic = "الملك",
            nameEnglish = "Al-Mulk",
            englishMeaning = "Sovereignty",
            revelationType = "Meccan",
            totalVerses = 30,
            ayahs = listOf(
                Ayah(67, 1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "Blessed is He in whose hand is dominion, and He is over all things competent -"),
                Ayah(67, 2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ", "[He] who created death and life to test you [as to] which of you is best in deed - and He is the Exalted in Might, the Forgiving -"),
                Ayah(67, 15, "هُوَ الَّذِي جَعَلَ لَكُمُ الْأَرْضَ ذَلُولًا فَامْشُوا فِي مَنَاكِبِهَا وَكُلُوا مِن رِّزْقِهِ ۖ وَإِلَيْهِ النُّشُورُ", "It is He who made the earth tame for you - so walk among its slopes and eat of His provision - and to Him is the resurrection.")
            )
        ),
        Surah(
            number = 93,
            nameArabic = "الضحى",
            nameEnglish = "Ad-Duha",
            englishMeaning = "The Morning Hours",
            revelationType = "Meccan",
            totalVerses = 11,
            ayahs = listOf(
                Ayah(93, 1, "وَالضُّحَىٰ", "By the morning sunlight,"),
                Ayah(93, 2, "وَاللَّيْلِ إِذَا سَجَىٰ", "And by the night when it covers with darkness,"),
                Ayah(93, 3, "مَا وَدَّعَكَ رَبُّكَ وَمَا قَلَىٰ", "Your Lord has not taken leave of you, [O Muhammad], nor has He detested [you]."),
                Ayah(93, 4, "وَلَلْآخِرَةُ خَيْرٌ لَّكَ مِنَ الْأُولَىٰ", "And the Hereafter is better for you than the first [life]."),
                Ayah(93, 5, "وَلَسَوْفَ يُعْطِيكَ رَبُّكَ فَتَرْضَىٰ", "And your Lord is going to give you, and you will be satisfied.")
            )
        ),
        Surah(
            number = 94,
            nameArabic = "الشرح",
            nameEnglish = "Ash-Sharh",
            englishMeaning = "The Relief",
            revelationType = "Meccan",
            totalVerses = 8,
            ayahs = listOf(
                Ayah(94, 1, "أَلَمْ نَشْرَحْ لَكَ صَدْرَكَ", "Did We not expand for you, [O Muhammad], your breast?"),
                Ayah(94, 2, "وَوَضَعْنَا عَنكَ وِزْرَكَ", "And We removed from you your burden"),
                Ayah(94, 5, "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا", "For indeed, with hardship [will be] ease."),
                Ayah(94, 6, "إِنَّ مَعَ الْعُسْرِ يُسْرًا", "Indeed, with hardship [will be] ease.")
            )
        ),
        Surah(
            number = 112,
            nameArabic = "الإخلاص",
            nameEnglish = "Al-Ikhlas",
            englishMeaning = "Sincerity",
            revelationType = "Meccan",
            totalVerses = 4,
            ayahs = listOf(
                Ayah(112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Say, 'He is Allah, [who is] One,"),
                Ayah(112, 2, "اللَّهُ الصَّمَدُ", "Allah, the Eternal Refuge."),
                Ayah(112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "He neither begets nor is born,"),
                Ayah(112, 4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "Nor is there to Him any equivalent.'")
            )
        ),
        Surah(
            number = 113,
            nameArabic = "الفلق",
            nameEnglish = "Al-Falaq",
            englishMeaning = "The Daybreak",
            revelationType = "Meccan",
            totalVerses = 5,
            ayahs = listOf(
                Ayah(113, 1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "Say, 'I seek refuge in the Lord of daybreak"),
                Ayah(113, 2, "مِن شَرِّ مَا خَلَقَ", "From the evil of that which He created"),
                Ayah(113, 3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "And from the evil of darkness when it settles"),
                Ayah(113, 4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "And from the evil of the blowers in knots"),
                Ayah(113, 5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "And from the evil of an envier when he envies.'")
            )
        ),
        Surah(
            number = 114,
            nameArabic = "الناس",
            nameEnglish = "An-Nas",
            englishMeaning = "Mankind",
            revelationType = "Meccan",
            totalVerses = 6,
            ayahs = listOf(
                Ayah(114, 1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "Say, 'I seek refuge in the Lord of mankind,"),
                Ayah(114, 2, "مَلِكِ النَّاسِ", "The Sovereign of mankind,"),
                Ayah(114, 3, "إِلَٰهِ النَّاسِ", "The God of mankind,"),
                Ayah(114, 4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "From the evil of the retreating whisperer -"),
                Ayah(114, 5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "Who whispers into the breasts of mankind -"),
                Ayah(114, 6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "From among the jinn and mankind.'")
            )
        )
    )

    val athkarList: List<AthkarItem> = listOf(
        // Morning Athkar
        AthkarItem(
            id = "m1",
            category = AthkarCategory.MORNING,
            titleArabic = "سيد الاستغفار",
            titleEnglish = "Master of Forgiveness",
            arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ.",
            transliteration = "Allahumma Anta Rabbi la ilaha illa Anta, khalaqtani wa ana 'abduka...",
            translationEnglish = "O Allah, You are my Lord, there is none worthy of worship except You. You created me and I am Your servant, and I abide by Your covenant and promise as best I can...",
            virtue = "Whoever says this with conviction in the morning and dies before evening will be among the people of Paradise (Bukhari).",
            repeatTarget = 1
        ),
        AthkarItem(
            id = "m2",
            category = AthkarCategory.MORNING,
            titleArabic = "أصبحنا وأصبح الملك لله",
            titleEnglish = "We have reached morning",
            arabicText = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ، رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَذَا الْيَوْمِ وَخَيْرَ مَا بَعْدَهُ.",
            transliteration = "Asbahna wa asbahal mulku lillah, walhamdu lillah...",
            translationEnglish = "We have entered the morning and the kingdom belongs to Allah, praise be to Allah. None has the right to be worshipped except Allah alone...",
            virtue = "A shield and supplication of comprehensive goodness for the whole day.",
            repeatTarget = 1
        ),
        AthkarItem(
            id = "m3",
            category = AthkarCategory.MORNING,
            titleArabic = "آية الكرسي",
            titleEnglish = "Ayat Al-Kursi",
            arabicText = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            transliteration = "Allahu la ilaha illa Huwa, Al-Hayyul-Qayyum...",
            translationEnglish = "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence...",
            virtue = "Whoever recites it in the morning is protected until the evening.",
            repeatTarget = 1
        ),
        AthkarItem(
            id = "m4",
            category = AthkarCategory.MORNING,
            titleArabic = "حفظ النفس والبدن",
            titleEnglish = "Protection from Harm",
            arabicText = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ.",
            transliteration = "Bismillahil-ladhi la yadurru ma'asmihi shay'un fil-ardi wa la fis-sama'i wa Huwas-Sami'ul-'Alim.",
            translationEnglish = "In the name of Allah, with whose name nothing on earth or in heaven can cause harm, and He is the All-Hearing, All-Knowing.",
            virtue = "Whoever recites it three times will not be harmed by anything (Tirmidhi).",
            repeatTarget = 3
        ),
        AthkarItem(
            id = "m5",
            category = AthkarCategory.MORNING,
            titleArabic = "رضيت بالله ربا",
            titleEnglish = "Contentment in Faith",
            arabicText = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا.",
            transliteration = "Raditu billahi Rabba, wa bil-Islami dina, wa bi Muhammadin sallAllahu 'alayhi wa sallama Nabiyya.",
            translationEnglish = "I am pleased with Allah as my Lord, with Islam as my religion, and with Muhammad (peace be upon him) as my Prophet.",
            virtue = "Allah has promised to please whoever recites this thrice in the morning and evening.",
            repeatTarget = 3
        ),

        // Evening Athkar
        AthkarItem(
            id = "e1",
            category = AthkarCategory.EVENING,
            titleArabic = "أمسينا وأمسى الملك لله",
            titleEnglish = "We have reached evening",
            arabicText = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
            transliteration = "Amsayna wa amsal mulku lillah, walhamdu lillah...",
            translationEnglish = "We have reached the evening and the entire dominion belongs to Allah, praise be to Allah alone...",
            virtue = "Bestows tranquility and divine guardianship throughout the night.",
            repeatTarget = 1
        ),
        AthkarItem(
            id = "e2",
            category = AthkarCategory.EVENING,
            titleArabic = "الاستعاذة بكلمات الله التامات",
            titleEnglish = "Refuge in Perfect Words",
            arabicText = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ.",
            transliteration = "A'udhu bi kalimatillahit-tammati min sharri ma khalaq.",
            translationEnglish = "I seek refuge in the perfect words of Allah from the evil of what He has created.",
            virtue = "Protects from all nocturnal venomous or harmful creatures.",
            repeatTarget = 3
        ),

        // After Prayer
        AthkarItem(
            id = "p1",
            category = AthkarCategory.AFTER_PRAYER,
            titleArabic = "الاستغفار بعد السلام",
            titleEnglish = "Istighfar after Salam",
            arabicText = "أَسْتَغْفِرُ اللَّهَ، أَسْتَغْفِرُ اللَّهَ، أَسْتَغْفِرُ اللَّهَ. اللَّهُمَّ أَنْتَ السَّلَامُ وَمِنْكَ السَّلَامُ، تَبَارَكْتَ يَا ذَا الْجَلَالِ وَالْإِكْرَامِ.",
            transliteration = "Astaghfirullah (3x). Allahumma Antas-Salamu wa minkas-Salam...",
            translationEnglish = "I ask Allah for forgiveness (thrice). O Allah, You are Peace and from You comes peace. Blessed are You, O Possessor of majesty and honor.",
            virtue = "Sunnah recited immediately upon concluding obligatory prayer.",
            repeatTarget = 1
        ),
        AthkarItem(
            id = "p2",
            category = AthkarCategory.AFTER_PRAYER,
            titleArabic = "التسبيح والتحميد والتكبير",
            titleEnglish = "Tasbih 33 times each",
            arabicText = "سُبْحَانَ اللَّهِ (٣٣) ، الْحَمْدُ لِلَّهِ (٣٣) ، اللَّهُ أَكْبَرُ (٣٣) ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ (١).",
            transliteration = "SubhanAllah (33x), Alhamdulillah (33x), Allahu Akbar (33x)...",
            translationEnglish = "Glory to Allah (33x), Praise to Allah (33x), Allah is greatest (33x), followed by the declaration of oneness.",
            virtue = "Forgives sins even if they were like the foam of the sea (Muslim).",
            repeatTarget = 33
        ),

        // Sleep
        AthkarItem(
            id = "s1",
            category = AthkarCategory.SLEEP,
            titleArabic = "باسمك ربي وضعت جنبي",
            titleEnglish = "In Your Name my Lord",
            arabicText = "بِاسْمِكَ رَبِّي وَضَعْتُ جَنْبِي، وَبِكَ أَرْفَعُهُ، فَإِنْ أَمْسَكْتَ نَفْسِي فَارْحَمْهَا، وَإِنْ أَرْسَلْتَهَا فَاحْفَظْهَا بِمَا تَحْفَظُ بِهِ عِبَادَكَ الصَّالِحِينَ.",
            transliteration = "Bismika Rabbi wada'tu janbi wa bika arfa'uhu...",
            translationEnglish = "In Your name my Lord, I lie down and in Your name I arise. If You should take my soul, then have mercy upon it, and if You release it, protect it as You protect Your righteous servants.",
            virtue = "Peace of heart and divine guardianship during sleep.",
            repeatTarget = 1
        ),

        // Quranic Dua
        AthkarItem(
            id = "q1",
            category = AthkarCategory.QURANIC_DUA,
            titleArabic = "دعاء سعة الرزق والعافية",
            titleEnglish = "Supplication for Good in Both Worlds",
            arabicText = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
            transliteration = "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar.",
            translationEnglish = "Our Lord, give us in this world [that which is] good and in the Hereafter [that which is] good and protect us from the punishment of the Fire. (Surah Al-Baqarah 2:201)",
            virtue = "The most frequent supplication of Prophet Muhammad (peace be upon him).",
            repeatTarget = 3
        ),
        AthkarItem(
            id = "q2",
            category = AthkarCategory.QURANIC_DUA,
            titleArabic = "دعاء انشراح الصدر وتيسير الأمر",
            titleEnglish = "Dua of Prophet Musa",
            arabicText = "رَبِّ اشْرَحْ لِي صَدْرِي وَيَسِّرْ لِي أَمْرِي وَاحْلُلْ عُقْدَةً مِّن لِّسَانِي يَفْقَهُوا قَوْلِي",
            transliteration = "Rabbish-rah li sadri, wa yassir li amri, wahlul 'uqdatan min lisani, yafqahu qawli.",
            translationEnglish = "My Lord, expand for me my breast [with assurance] and ease for me my task and untie the knot from my tongue that they may understand my speech. (Surah Taha 20:25-28)",
            virtue = "Bestowed clarity, eloquence, and relief from distress.",
            repeatTarget = 3
        )
    )

    val hadiths: List<Hadith> = listOf(
        Hadith(
            id = "nawawi_1",
            collectionName = "الأربعين النووية",
            chapter = "باب النية والإخلاص",
            hadithNumber = 1,
            narratorArabic = "عَنْ أَمِيرِ الْمُؤْمِنِينَ أَبِي حَفْصٍ عُمَرَ بْنِ الْخَطَّابِ رَضِيَ اللَّهُ عَنْهُ قَالَ: سَمِعْتُ رَسُولَ اللَّهِ ﷺ يَقُولُ:",
            matnArabic = "«إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى، فَمَنْ كَانَتْ هِجْرَتُهُ إِلَى اللَّهِ وَرَسُولِهِ فَهِجْرَتُهُ إِلَى اللَّهِ وَرَسُولِهِ، وَمَنْ كَانَتْ هِجْرَتُهُ لِدُنْيَا يُصِيبُهَا أَوْ امْرَأَةٍ يَنْكِحُهَا فَهِجْرَتُهُ إِلَى مَا هَاجَرَ إِلَيْهِ».",
            sourceArabic = "رواه البخاري ومسلم",
            englishMeaning = "Actions are according to intentions, and everyone will get what was intended.",
            categoryArabic = "الإيمان"
        ),
        Hadith(
            id = "nawawi_2",
            collectionName = "الأربعين النووية",
            chapter = "باب مراتب الدين (الإسلام والإيمان والإحسان)",
            hadithNumber = 2,
            narratorArabic = "عَنْ عُمَرَ بْنِ الْخَطَّابِ رَضِيَ اللَّهُ عَنْهُ، فِي حَدِيثِ جِبْرِيلَ عَلَيْهِ السَّلَامُ قَالَ:",
            matnArabic = "«قَالَ: فَأَخْبِرْنِي عَنِ الإِحْسَانِ؟ قَالَ: أَنْ تَعْبُدَ اللَّهَ كَأَنَّكَ تَرَاهُ، فَإِنْ لَمْ تَكُنْ تَرَاهُ فَإِنَّهُ يَرَاكَ... قَالَ: هَذَا جِبْرِيلُ أَتَاكُمْ يُعَلِّمُكُمْ دِينَكُمْ».",
            sourceArabic = "رواه مسلم",
            englishMeaning = "Ihsan is to worship Allah as if you see Him, and if you do not see Him, He sees you.",
            categoryArabic = "الإيمان"
        ),
        Hadith(
            id = "nawawi_3",
            collectionName = "الأربعين النووية",
            chapter = "باب أركان الإسلام",
            hadithNumber = 3,
            narratorArabic = "عَنْ عَبْدِ اللَّهِ بْنِ عُمَرَ رَضِيَ اللَّهُ عَنْهُمَا قَالَ: قَالَ رَسُولُ اللَّهِ ﷺ:",
            matnArabic = "«بُنِيَ الإِسْلاَمُ عَلَى خَمْسٍ: شَهَادَةِ أَنْ لاَ إِلَهَ إِلاَّ اللَّهُ وَأَنَّ مُحَمَّدًا رَسُولُ اللَّهِ، وَإِقَامِ الصَّلاَةِ، وَإِيتَاءِ الزَّكَاةِ، وَحَجِّ البَيْتِ، وَصَوْمِ رَمَضَانَ».",
            sourceArabic = "متفق عليه",
            englishMeaning = "Islam has been built upon five pillars...",
            categoryArabic = "العبادات"
        ),
        Hadith(
            id = "bukhari_1",
            collectionName = "صحيح البخاري",
            chapter = "فضل تلاوة القرآن",
            hadithNumber = 5027,
            narratorArabic = "عَنْ عُثْمَانَ بْنِ عَفَّانَ رَضِيَ اللَّهُ عَنْهُ، عَنِ النَّبِيِّ ﷺ قَالَ:",
            matnArabic = "«خَيْرُكُمْ مَنْ تَعَلَّمَ القُرْآنَ وَعَلَّمَهُ».",
            sourceArabic = "صحيح البخاري",
            englishMeaning = "The best among you are those who learn the Qur'an and teach it.",
            categoryArabic = "العلم"
        ),
        Hadith(
            id = "muslim_1",
            collectionName = "صحيح مسلم",
            chapter = "فضل الذكر والدعاء",
            hadithNumber = 2691,
            narratorArabic = "عَنْ أَبِي هُرَيْرَةَ رَضِيَ اللَّهُ عَنْهُ، أَنَّ رَسُولَ اللَّهِ ﷺ قَالَ:",
            matnArabic = "«كَلِمَتَانِ خَفِيفَتَانِ عَلَى اللِّسَانِ، ثَقِيلَتَانِ فِي الْمِيزَانِ، حَبِيبَتَانِ إِلَى الرَّحْمَنِ: سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، سُبْحَانَ اللَّهِ الْعَظِيمِ».",
            sourceArabic = "متفق عليه",
            englishMeaning = "Two words are light on the tongue, heavy in the scale, beloved to the Most Merciful: SubhanAllahi wa bihamdihi, SubhanAllahil 'Adheem.",
            categoryArabic = "العبادات"
        ),
        Hadith(
            id = "riyadh_1",
            collectionName = "رياض الصالحين",
            chapter = "باب فضل المحبة في الله وإفشاء السلام",
            hadithNumber = 378,
            narratorArabic = "عَنْ أَبِي هُرَيْرَةَ رَضِيَ اللَّهُ عَنْهُ قَالَ: قَالَ رَسُولُ اللَّهِ ﷺ:",
            matnArabic = "«وَالَّذِي نَفْسِي بِيَدِهِ لَا تَدْخُلُوا الْجَنَّةَ حَتَّى تُؤْمِنُوا، وَلَا تُؤْمِنُوا حَتَّى تَحَابُّوا، أَوَلَا أَدُلُّكُمْ عَلَى أَمْرٍ إِذَا فَعَلْتُمُوهُ تَحَابَبْتُمْ؟ أَفْشُوا السَّلَامَ بَيْنَكُمْ».",
            sourceArabic = "رواه مسلم",
            englishMeaning = "You will not enter Paradise until you believe, and you will not believe until you love one another. Spread peace among yourselves.",
            categoryArabic = "الأخلاق"
        )
    )
}
