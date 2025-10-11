package com.example.openhandmobile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.openhandmobile.ui.theme.Raleway

@Composable
fun IntroductionScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp)
                .background(Color(0xFF1A1A1A))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(32.dp)
                    .background(Color(0xFF1A1A1A)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.handy_white_logo),
                    contentDescription = "Handy",
                    modifier = Modifier
                        .size(170.dp)

                )
                Text(
                    text = "OpenHand",
                    fontFamily = Raleway,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        fontSize = 48.sp
                    ),
                    color = Color(0xFF00A6FF)

                )

                Text(
                    text = "Empowering Communication for All.",
                    style = TextStyle(
                        color = Color(0xFFFFFFFF),
                        fontSize = 16.sp
                    ),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center
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
                Button(
                    onClick = { /* Add */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00A6FF)
                    )
                ) {
                    Text("GET STARTED")
                }


                Button(
                    onClick = { /* I ALREADY HAVE AN ACCOUNT */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFFFFF)
                    )

                ) {
                    Text("I ALREADY HAVE AN ACCOUNT",
                    color = Color(0xFF00A6FF))
                }
            }
        }
    }
}

