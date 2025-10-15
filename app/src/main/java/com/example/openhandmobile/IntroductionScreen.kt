package com.example.openhandmobile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.openhandmobile.ui.theme.Raleway

@Composable
fun IntroductionScreen(
    onContinueClicked: () -> Unit = {},
    onLoginClicked: () -> Unit = {}
) {
        Scaffold(
            containerColor = Color(0xFF1A1A1A),
            modifier = Modifier.fillMaxSize()

        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(22.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(22.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.scholar_smiley),
                        contentDescription = "Handy",
                        modifier = Modifier
                            .size(250.dp)

                    )

                    Text(
                        text = "OpenHand",
                        fontFamily = Raleway,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            fontSize = 48.sp
                        ),
                        color = Color(0xFF00A6FF),
                        modifier = Modifier.padding(top = 26.dp)

                    )

                    Text(
                        text = "Empowering Communication for All.",
                        style = TextStyle(
                            color = Color(0xFFFFFFFF),
                            fontSize = 16.sp
                        ),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 16.dp)

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
                        onClick = { onContinueClicked() },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(2.dp, Color(0xFF00A6FF)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFF00A6FF)
                        )
                    ) {
                        Text("GET STARTED")
                    }

                    OutlinedButton(
                        onClick = { onLoginClicked() },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(2.dp, Color(0xFFFFFFFF)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )

                    ) {
                        Text(
                            "I ALREADY HAVE AN ACCOUNT",
                            color = Color(0xFFFFFFFF)
                        )
                    }

                }
            }
        }

}
