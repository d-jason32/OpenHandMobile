package com.example.openhandmobile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.openhandmobile.ui.theme.Raleway

@Composable
fun Login(nav: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    // needed for the text boxes
    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}

    Surface(
        color = Color(0xFF1A1A1A),
        modifier = Modifier.fillMaxSize()

    ) {
        // Column for the login page
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Image(
                painter = painterResource(id = R.drawable.handy_closed_eyes),
                contentDescription = "Handy",
                modifier = Modifier
                    .size(170.dp)

            )


            Text("Sign in",
                fontFamily = Raleway,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    fontSize = 48.sp
                ),
                color = Color(0xFF00A6FF),
                modifier = Modifier.padding(top = 26.dp)
                )

            TextField(
                value = email,
                shape = RoundedCornerShape(12.dp),
                onValueChange = { email = it},
                label = { Text("Email")}
            )

            TextField(
                value = password,
                shape = RoundedCornerShape(12.dp),
                onValueChange = { password = it},
                label = { Text("Password")}
            )
            // Button for login
            ElevatedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                onClick = {
                    // All fields must be filled in
                    if (email.isBlank() || password.isBlank()){
                        Toast.makeText(context, "Fill in all the fields", Toast.LENGTH_SHORT).show()
                    }
                    // Password must be greater than 6 and less than 30 characters
                    else if (password.length < 6 || password.length > 30){
                        Toast.makeText(context, "Password must be greater than 6 and less than 30 characters.", Toast.LENGTH_SHORT).show()
                    }
                    // Email must be valid using regex.
                    else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex())){
                        Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context, "Successful sign on", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Login")
            }
            // Button to go to the register account page
            ElevatedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                onClick = { nav.navigate("register") }
            ) {
                Text("Don't have an account? Register")
            }

        }

    }
}