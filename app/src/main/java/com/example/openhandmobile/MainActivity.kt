package com.example.openhandmobile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.openhandmobile.ui.theme.OpenHandMobileTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        auth = Firebase.auth

        setContent {
            OpenHandMobileTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {

        }
    }


}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    OpenHandMobileTheme {

        IntroductionScreen()
    }
}

@Preview
@Composable
fun MyAppPreview() {
    OpenHandMobileTheme {

        MyApp(Modifier.fillMaxSize())
    }
}

// Main app function to allow for navigation
@Composable
fun MyApp(
    modifier: Modifier = Modifier)
{
    SetLightStatusBar()

    // Needed to create a navigation controller
    val nav = rememberNavController()

    Surface(modifier) {
        NavHost(
            navController = nav,
            // app will start at the introduction screen
            startDestination = "home"
        ) {
            composable("intro") {

                IntroductionScreen(
                    onContinueClicked = {
                        // SET THIS TO REGISTER!
                        nav.navigate("register") {
                            popUpTo("intro") { inclusive = true }
                        }
                    },
                    onLoginClicked = {
                        nav.navigate("login") {
                            popUpTo("intro") { inclusive = true }
                        }
                    },




                )
            }
            // Route for the login screen
            composable("login") { Login(nav) }

            // route for the register screen
            composable("register") { RegisterScreen(nav) }

            // Route for the home page
            composable("home") { Home(nav) }

            // Route for the home page
            composable("cameraTest") { CameraTest(nav) }

            composable("classes") { Classes(nav) }

            composable("roadmap") { Roadmap(nav) }

            composable("settings") { Settings(nav) }

            composable("profile") { Profile(nav) }


        }
    }
}

@Composable
fun SetLightStatusBar() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = false
    val statusBarColor = Color.Black

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = useDarkIcons
        )
    }
}