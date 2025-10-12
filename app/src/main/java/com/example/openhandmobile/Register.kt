package com.example.openhandmobile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun RegisterScreen(nav: NavHostController){
    val context = LocalContext.current
    // needed for the text boxes
    var name by remember { mutableStateOf("")}
    var dob by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}

    Surface(
        color = Color(0xFF1A1A1A),
        modifier = Modifier.fillMaxSize()
    ) {


        // column for login page
        Column(
            // Add scrolling state
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),

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
                color = Color(0xFF00A6FF),
                modifier = Modifier.padding(top = 26.dp)
            )

            TextField(
                value = name,
                shape = RoundedCornerShape(12.dp),
                onValueChange = { name = it },
                label = { Text("First Name") }
            )

            TextField(
                value = dob,
                shape = RoundedCornerShape(12.dp),
                onValueChange = { dob = it },
                label = { Text("Date of Birth") }
            )

            TextField(
                value = email,
                shape = RoundedCornerShape(12.dp),
                onValueChange = { email = it },
                label = { Text("Email") }
            )
            TextField(
                value = password,
                shape = RoundedCornerShape(12.dp),
                onValueChange = { password = it },
                label = { Text("Password") }
            )

            // Button to be able to create your account
            ElevatedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                onClick = {
                    if (email.isBlank() || password.isBlank() || dob.isBlank() || name.isBlank()) {
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

                    // First name must be greater than 3 and less than 30 characters
                    else if (name.length < 3 || name.length > 30) {
                        Toast.makeText(
                            context,
                            "First name must be greater than 3 and less than 30 characters.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    // Date of birth must be valid using regex.
                    else if (!dob.matches("^(0[1-9]|1[0-2])/([0][1-9]|[12][0-9]|3[01])/\\d{4}$".toRegex())) {
                        Toast.makeText(
                            context,
                            "Please enter a valid date of birth MM/DD/YYYY",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Successful account creation, go back to log in!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text("Create account")
            }

            // Button to go back to the log in screen if you already have an account
            ElevatedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                onClick = { nav.popBackStack() }
            ) {
                Text("Already have an account? Login")
            }

        }
    }
}
