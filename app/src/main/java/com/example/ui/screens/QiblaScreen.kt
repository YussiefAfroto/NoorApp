package com.example.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CityPrayerPreset
import com.example.ui.theme.CelestialBackground
import com.example.ui.theme.CelestialBorderHairline
import com.example.ui.theme.CelestialEmerald
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.CelestialOnSurface
import com.example.ui.theme.CelestialPrimaryBright
import com.example.ui.theme.CelestialPrimaryGradientEnd
import com.example.ui.theme.CelestialSky
import com.example.ui.theme.CelestialSlateDeep
import com.example.ui.theme.CelestialSlateMuted
import com.example.ui.theme.CelestialSurface
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun QiblaScreen(
    isArabic: Boolean,
    selectedCity: CityPrayerPreset,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var sensorAzimuth by remember { mutableFloatStateOf(0f) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            ?: sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return
                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    val rotationMatrix = FloatArray(9)
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    val azimuthInDegrees = Math.toDegrees(orientation[0].toDouble()).toFloat()
                    sensorAzimuth = (azimuthInDegrees + 360) % 360
                } else if (event.sensor.type == Sensor.TYPE_ORIENTATION) {
                    sensorAzimuth = event.values[0]
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (rotationSensor != null) {
            sensorManager?.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager?.unregisterListener(listener)
        }
    }

    // Smooth compass animation
    val animatedCompassRotation by animateFloatAsState(
        targetValue = -sensorAzimuth,
        animationSpec = tween(durationMillis = 200),
        label = "compassRotation"
    )

    // Calculate angle difference to Kaaba
    val qiblaTarget = selectedCity.qiblaDegree
    val angleDiff = abs((sensorAzimuth - qiblaTarget + 540) % 360 - 180)
    val isFacingKaaba = angleDiff < 5f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CelestialBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (isArabic) "اتِّجَاهُ القِبْلَةِ الشَّرِيفَة" else "Qibla Direction",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = CelestialOnSurface
            )
            Text(
                text = if (isArabic) "فَوَلِّ وَجْهَكَ شَطْرَ الْمَسْجِدِ الْحَرَامِ" else "Turn your face toward the Sacred Mosque",
                style = MaterialTheme.typography.labelSmall,
                color = CelestialSlateMuted
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Qibla Status Pill
        Surface(
            shape = RoundedCornerShape(9999.dp),
            color = if (isFacingKaaba) CelestialEmerald.copy(alpha = 0.15f) else CelestialPrimaryBright.copy(alpha = 0.12f),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isFacingKaaba) CelestialEmerald else CelestialPrimaryBright
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = if (isFacingKaaba) Icons.Default.Mosque else Icons.Default.Explore,
                    contentDescription = null,
                    tint = if (isFacingKaaba) CelestialEmerald else CelestialPrimaryBright,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (isFacingKaaba) {
                        if (isArabic) "أنت تواجه الكعبة المشرفة الآن!" else "You are facing the Kaaba!"
                    } else {
                        if (isArabic) "زاوية القبلة: ${qiblaTarget.toInt()}° مِن الشمال" else "Qibla Angle: ${qiblaTarget.toInt()}° from North"
                    },
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isFacingKaaba) CelestialEmerald else CelestialPrimaryBright
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Celestial Compass Dial
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(280.dp)
                .testTag("qibla_compass_dial")
        ) {
            // Luminous Celestial Outer Ring
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                (if (isFacingKaaba) CelestialEmerald else CelestialPrimaryBright).copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Compass Base Plate
            Surface(
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .shadow(12.dp, CircleShape, spotColor = CelestialPrimaryBright.copy(alpha = 0.25f))
                    .border(2.dp, CelestialBorderHairline, CircleShape),
                color = CelestialSurface
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(animatedCompassRotation),
                    contentAlignment = Alignment.Center
                ) {
                    // Compass Tick Marks
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val radius = size.width / 2f - 16.dp.toPx()

                        for (i in 0 until 360 step 15) {
                            val angleRad = Math.toRadians(i.toDouble() - 90)
                            val isCardinal = i % 90 == 0
                            val tickLength = if (isCardinal) 12.dp.toPx() else 6.dp.toPx()
                            val strokeWidth = if (isCardinal) 2.5.dp.toPx() else 1.dp.toPx()
                            val color = if (isCardinal) Color(0xFF0F172A) else Color(0xFFBFC7D4)

                            val startX = (center.x + (radius - tickLength) * cos(angleRad)).toFloat()
                            val startY = (center.y + (radius - tickLength) * sin(angleRad)).toFloat()
                            val endX = (center.x + radius * cos(angleRad)).toFloat()
                            val endY = (center.y + radius * sin(angleRad)).toFloat()

                            drawLine(
                                color = color,
                                start = Offset(startX, startY),
                                end = Offset(endX, endY),
                                strokeWidth = strokeWidth
                            )
                        }
                    }

                    // North, East, South, West labels
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "N",
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFE53935),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 18.dp)
                        )
                        Text(
                            text = "S",
                            fontWeight = FontWeight.Bold,
                            color = CelestialSlateMuted,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 18.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "W",
                            fontWeight = FontWeight.Bold,
                            color = CelestialSlateMuted,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 18.dp)
                        )
                        Text(
                            text = "E",
                            fontWeight = FontWeight.Bold,
                            color = CelestialSlateMuted,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(end = 18.dp)
                        )
                    }

                    // Kaaba Direction Marker (Gold Icon at qiblaTarget angle)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(qiblaTarget),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 34.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = CelestialGold,
                                shadowElevation = 4.dp,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Mosque,
                                        contentDescription = "Kaaba",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Fixed Center Pointer Needle
            Surface(
                shape = CircleShape,
                color = CelestialPrimaryBright,
                modifier = Modifier
                    .size(24.dp)
                    .border(3.dp, Color.White, CircleShape),
                shadowElevation = 6.dp
            ) {}
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Distance to Kaaba & City Details Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = CelestialSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CelestialBorderHairline),
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = CelestialPrimaryBright
                        )
                        Column {
                            Text(
                                text = if (isArabic) selectedCity.cityNameArabic else selectedCity.cityNameEnglish,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CelestialOnSurface
                            )
                            Text(
                                text = selectedCity.country,
                                style = MaterialTheme.typography.labelSmall,
                                color = CelestialSlateMuted
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${selectedCity.distanceToKaabaKm} km",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CelestialPrimaryBright
                        )
                        Text(
                            text = if (isArabic) "المَسَافَةُ إِلَى الكَعْبَة" else "Distance to Kaaba",
                            style = MaterialTheme.typography.labelSmall,
                            color = CelestialSlateMuted
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(CelestialBorderHairline)
                )

                Text(
                    text = if (isArabic) {
                        "للحصول على أدق قراءة، ضع الجهاز على سطح مستوٍ وأدره حتى يشير مؤشر الكعبة للأعلى."
                    } else {
                        "For optimal accuracy, place the device on a flat surface and rotate until the Kaaba indicator aligns to the top."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = CelestialSlateMuted,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(120.dp))
    }
}
