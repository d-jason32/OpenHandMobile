package com.example.openhandmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.openhandmobile.ui.theme.OpenHandMobileTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var keepOnScreen = true
        splashScreen.setKeepOnScreenCondition { keepOnScreen }

        setContent {
            OpenHandMobileTheme {
                var showSplash by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(2000)
                    showSplash = false
                    keepOnScreen = false
                }

                if (showSplash) {
                    SplashScreenContent()
                } else {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun SplashScreenContent() {
    // Your splash screen content goes here
    Text(text = "Splash Screen")
}

@Composable
fun HomeScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Text(
                text = "Hello, Open Hand Mobile!",
                modifier = Modifier.padding(paddingValues)
            )
        }
    )
}