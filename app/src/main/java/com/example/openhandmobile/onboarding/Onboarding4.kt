package com.example.openhandmobile.onboarding


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.openhandmobile.OnboardingState
import com.example.openhandmobile.BottomNavBar
import com.example.openhandmobile.R
import com.example.openhandmobile.SoundManager
import com.example.openhandmobile.ui.theme.Raleway
import com.example.squares.Squares
import kotlinx.coroutines.delay

@Composable
fun Onboarding4(nav: NavHostController, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        delay(1000)
        SoundManager.play("practice")
    }

    Scaffold(
        containerColor = Color(0xFF1A1A1A),
        modifier = Modifier.fillMaxSize(),


        ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            Squares(
                modifier = Modifier.matchParentSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                Image(
                    painter = painterResource(id = R.drawable.practice_transparent),
                    contentDescription = "Handy",
                    modifier = Modifier
                        .width(190.dp)

                )

                Spacer(Modifier.height(10.dp))


                Image(
                    painter = painterResource(id = R.drawable.handy_smart_crop_fix),
                    contentDescription = "Handy",
                    modifier = Modifier
                        .size(250.dp)

                )


                Spacer(Modifier.height(100.dp))


            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            )
            {
                OutlinedButton(
                    onClick = {
                        SoundManager.play("click")
                        OnboardingState.setPractice("daily")
                        nav.navigate("register")
                    },
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    border = BorderStroke(2.dp, Color(0xFF00A6FF)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF00A6FF)
                    )
                ) {
                    Text(
                        "Daily",
                        fontSize = 16.sp
                    )
                }

                OutlinedButton(
                    onClick = {
                        SoundManager.play("click")
                        OnboardingState.setPractice("weekly")
                        nav.navigate("register")
                    },
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    border = BorderStroke(2.dp, Color(0xFF00A6FF)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF00A6FF)
                    )
                ) {
                    Text(
                        "Weekly",
                        fontSize = 16.sp
                    )
                }




                Spacer(Modifier.height(10.dp))


            }
        }
    }
}
