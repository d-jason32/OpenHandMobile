package com.example.openhandmobile

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.openhandmobile.BottomNavBar
import com.example.openhandmobile.R
import com.example.openhandmobile.SoundManager
import com.example.openhandmobile.ui.theme.Raleway
import com.example.squares.Squares
import kotlinx.coroutines.delay
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

@Composable
fun CongratulationsScreen(nav: NavHostController, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        delay(1000)
        SoundManager.play("nice")
        // Award XP to the current user
        val user = Firebase.auth.currentUser
        if (user != null) {
            try {
                Firebase.firestore.collection("users")
                    .document(user.uid)
                    .update("xp", FieldValue.increment(10))
            } catch (_: Exception) {
                // Ignore failures silently
            }
        }
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

                Text(
                    text = "+10 XP",
                    color = Color(0xFF00A6FF),
                    fontFamily = Raleway,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))


                Image(
                    painter = painterResource(id = R.drawable.nice_job),
                    contentDescription = "Handy",
                    modifier = Modifier
                        .width(225.dp)

                )

                Spacer(Modifier.height(10.dp))


                Image(
                    painter = painterResource(id = R.drawable.handy_smart_crop_fix),
                    contentDescription = "Handy",
                    modifier = Modifier
                        .size(250.dp)

                )


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
                        nav.navigate("badgeWin")
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
                        "Next",
                        fontSize = 16.sp
                    )
                }

                Spacer(Modifier.height(10.dp))


            }
        }
    }
}
