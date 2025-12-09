package com.example.openhandmobile.grading

import androidx.compose.foundation.BorderStroke
import com.example.openhandmobile.CameraStreamScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun GradingScreen(
    nav: NavHostController,
    id: String,
    modifier: Modifier = Modifier
) {
    // Last prediction from the model
    var currentLabel by remember { mutableStateOf("…") }
    var currentProb by remember { mutableStateOf(0f) }

    // To avoid navigating multiple times
    var hasNavigated by remember { mutableStateOf(false) }

    // Parse the grading id, e.g. "letter_A", "number_1", "word_MILK"
    val (type, rawLabel) = remember(id) {
        val parts = id.split("_", limit = 2)
        val t = parts.getOrNull(0) ?: ""
        val l = parts.getOrNull(1) ?: ""
        t to l
    }

    // What label we expect from the model (adjust if your model uses a different naming convention)
    val expectedLabel = rawLabel

    // Label to show in the UI (replace '_' with space for words like THANK_YOU)
    val displayLabel = remember(rawLabel) {
        rawLabel.replace('_', ' ')
    }

    // Normalize labels so spacing/case differences don't block matches (e.g., "DIAPER" vs "Diaper")
    val normalize: (String) -> String = { s ->
        s.trim().lowercase().replace(" ", "").replace("_", "")
    }

    // ✅ When prediction matches the expected label with high confidence, go to Congratulations
    LaunchedEffect(currentLabel, currentProb, hasNavigated, expectedLabel) {
        val match = normalize(currentLabel) == normalize(expectedLabel)
        if (!hasNavigated && match && currentProb >= 0.20f) {
            hasNavigated = true
            val encodedId = URLEncoder.encode(id, StandardCharsets.UTF_8.name())
            nav.navigate("CongratulationsScreen?id=$encodedId")
        }
    }

    Scaffold(
        containerColor = Color(0xFF1A1A1A),
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = when (type) {
                    "letter" -> "Show the sign for letter $displayLabel"
                    "number" -> "Show the sign for number $displayLabel"
                    "word" -> "Show the sign for \"$displayLabel\""
                    else -> "Show the sign for $displayLabel"
                },
                color = Color.White,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Camera area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Words (phrases) use the gesture model; letters/numbers stay on the default model.
                val serverUrl = when (type) {
                    "word" -> "ws://10.0.2.2:8000/ws?model=gestures"
                    else -> "ws://10.0.2.2:8000/ws?model=letters"
                }
                // Only send mode for letters/numbers; gesture model ignores it.
                val initialMode = when (type) {
                    "letter" -> "letters"
                    "number" -> "numbers"
                    else -> null
                }
                CameraStreamScreen(
                    serverUrl = serverUrl,
                    initialMode = initialMode,
                    onPrediction = { label, prob ->
                        currentLabel = label
                        currentProb = prob
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            // Feedback for the user (optional)
            Text(
                text = "Detected: $currentLabel (${(currentProb * 100).toInt()}%)",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                border = BorderStroke(2.dp, Color(0xFF00A6FF)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF00A6FF)
                )
            ) {
                Text("Back")
            }
        }
    }
}