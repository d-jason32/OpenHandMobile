package com.example.openhandmobile

import android.media.AudioAttributes
import android.media.SoundPool
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
import com.example.openhandmobile.classes.classA
import com.example.openhandmobile.classes.classB
import com.example.openhandmobile.classes.classC
import com.example.openhandmobile.classes.classD
import com.example.openhandmobile.classes.classE
import com.example.openhandmobile.classes.classF
import com.example.openhandmobile.classes.classG
import com.example.openhandmobile.classes.classH
import com.example.openhandmobile.classes.classI
import com.example.openhandmobile.classes.classJ
import com.example.openhandmobile.classes.classK
import com.example.openhandmobile.classes.classL
import com.example.openhandmobile.classes.classM
import com.example.openhandmobile.classes.classN
import com.example.openhandmobile.classes.classO
import com.example.openhandmobile.classes.classP
import com.example.openhandmobile.classes.classQ
import com.example.openhandmobile.classes.classR
import com.example.openhandmobile.classes.classS
import com.example.openhandmobile.classes.classT
import com.example.openhandmobile.classes.classU
import com.example.openhandmobile.classes.classV
import com.example.openhandmobile.classes.classW
import com.example.openhandmobile.classes.classX
import com.example.openhandmobile.classes.classY
import com.example.openhandmobile.classes.classZ

import com.example.openhandmobile.onboarding.Onboarding1
import com.example.openhandmobile.onboarding.Onboarding2
import com.example.openhandmobile.onboarding.Onboarding2_5
import com.example.openhandmobile.onboarding.Onboarding3
import com.example.openhandmobile.onboarding.Onboarding4
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        SoundManager.init(this)


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
            //startDestination = "intro"
        ) {
            composable("intro") {

                IntroductionScreen(
                    onContinueClicked = {
                        // SET THIS TO REGISTER!
                        nav.navigate("onboarding1") {
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

            composable("classes") { Classes(nav) }

            composable("roadmap") { Roadmap(nav) }

            composable("settings") { Settings(nav) }

            composable("profile") { Profile(nav) }

            composable("leaderboard") { LeaderboardScreen(nav) }

            composable("friends") { FriendsScreen(nav) }

            composable("modeltest") { ModelTest(nav) }

            composable("onboarding1") { Onboarding1(nav) }

            composable("onboarding2") { Onboarding2(nav) }

            composable("onboarding3") { Onboarding3(nav) }

            composable("onboarding4") { Onboarding4(nav) }

            composable("onboarding2_5") { Onboarding2_5(nav) }

            composable("classA") { classA(nav) }
            composable("classB") { classB(nav) }
            composable("classC") { classC(nav) }
            composable("classD") { classD(nav) }
            composable("classE") { classE(nav) }
            composable("classF") { classF(nav) }
            composable("classG") { classG(nav) }
            composable("classH") { classH(nav) }
            composable("classI") { classI(nav) }
            composable("classJ") { classJ(nav) }
            composable("classK") { classK(nav) }
            composable("classL") { classL(nav) }
            composable("classM") { classM(nav) }
            composable("classN") { classN(nav) }
            composable("classO") { classO(nav) }
            composable("classP") { classP(nav) }
            composable("classQ") { classQ(nav) }
            composable("classR") { classR(nav) }
            composable("classS") { classS(nav) }
            composable("classT") { classT(nav) }
            composable("classU") { classU(nav) }
            composable("classV") { classV(nav) }
            composable("classW") { classW(nav) }
            composable("classX") { classX(nav) }
            composable("classY") { classY(nav) }
            composable("classZ") { classZ(nav) }



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