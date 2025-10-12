package com.example.openhandmobile

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
fun RegisterScreen(nav: NavHostController){
    val context = LocalContext.current
    // needed for the text boxes
    var username by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}

    Surface(
        color = Color(0xFF1A1A1A),
        modifier = Modifier.fillMaxSize()
    ) {


        // column for login page
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp),
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
                "Sign up",
                fontFamily = Raleway,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    fontSize = 48.sp
                ),
                color = Color(0xFFFFFFFF),
                modifier = Modifier.padding(top = 26.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp),
                value = email,
                shape = RoundedCornerShape(25.dp),
                onValueChange = { email = it },
                label = { Text("Email") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF00A6FF),
                    unfocusedIndicatorColor = Color(0xFFFFFFFF),
                    focusedLabelColor = Color(0xFF00A6FF),
                    unfocusedLabelColor = Color(0xFFAAAAAA),
                    cursorColor = Color(0xFF00A6FF),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(25.dp),
                onValueChange = { username = it },
                label = { Text("Username") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF00A6FF),
                    unfocusedIndicatorColor = Color(0xFFFFFFFF),
                    focusedLabelColor = Color(0xFF00A6FF),
                    unfocusedLabelColor = Color(0xFFAAAAAA),
                    cursorColor = Color(0xFF00A6FF),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )

            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(25.dp),
                onValueChange = { password = it },
                label = { Text("Password") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF00A6FF),
                    unfocusedIndicatorColor = Color(0xFFFFFFFF),
                    focusedLabelColor = Color(0xFF00A6FF),
                    unfocusedLabelColor = Color(0xFFAAAAAA),
                    cursorColor = Color(0xFF00A6FF),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button to be able to create your account
            Button(
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp),
                border = BorderStroke(2.dp, Color(0xFFFFFFFF)),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFFFFF),
                    contentColor = Color(0xFF000000)
                ),
                onClick = {
                    if (email.isBlank() || password.isBlank() || username.isBlank()) {
                        Toast.makeText(context, "Fill in all the fields", Toast.LENGTH_SHORT).show()
                    }
                    // Password must be greater than 6 and less than 30 characters
                    else if (password.length < 6 || password.length > 30) {
                        Toast.makeText(
                            context,
                            "Password must be greater than 6 and less than 30 characters.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    // Email must be valid using regex.
                    else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex())) {
                        Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT)
                            .show()
                    }

                     else {
                        Toast.makeText(
                            context,
                            "Successful account creation, go back to log in!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text("Sign up",
                    fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to go back to the log in screen if you already have an account
            Button(
                shape = RoundedCornerShape(25.dp),

                modifier = Modifier.fillMaxWidth()
                    .height(56.dp),
                border = BorderStroke(2.dp, Color(0xFFFFFFFF)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                onClick = { nav.popBackStack() }
            ) {
                Text("Already have an account? Login",
                    fontSize = 14.sp)
            }

        }
    }
}
