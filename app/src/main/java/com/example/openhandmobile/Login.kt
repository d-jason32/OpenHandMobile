package com.example.openhandmobile

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextDecoration
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

    @Composable
    fun OrDivider() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                color = Color(0xFFFFFFFF),
                thickness = 1.dp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "OR",
                color = Color.White,
                fontFamily = Raleway,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            HorizontalDivider(
                color = Color(0xFFFFFFFF),
                thickness = 1.dp,
                modifier = Modifier.weight(1f)
            )
        }
    }


    Surface(
        color = Color(0xFF1A1A1A),
        modifier = Modifier.fillMaxSize()

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp)
        ) {
        // Column for the login page
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp),
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
                onValueChange = { email = it},
                label = { Text("Email")},

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
                ),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.White
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Email Icon",
                        tint = Color(0xFF00A6FF)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp),
                value = password,
                shape = RoundedCornerShape(25.dp),
                onValueChange = { password = it},
                label = { Text("Password")},
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
                ),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.White),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Lock Icon",
                        tint = Color(0xFF00A6FF)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button for login
            Button(
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier.fillMaxWidth()
                    .heightIn(min = 56.dp),
                border = BorderStroke(2.dp, Color(0xFFFFFFFF)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFFFFF),
                    contentColor = Color(0xFF000000)
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
                Text("Sign in",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold)
            }

            OrDivider()

            Button(
                onClick = {  },
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier.fillMaxWidth()
                    .heightIn(min = 56.dp),
                border = BorderStroke(2.dp, Color(0xFFFFFFFF)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFFFFF),
                )
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_google),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(28.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text("Continue with Google",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {  },
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier.fillMaxWidth()
                    .heightIn(min = 56.dp),
                border = BorderStroke(2.dp, Color(0xFFFFFFFF)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFFFFF))
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_github2),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(34.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Continue with GitHub",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to go to the register account page
            Text("Don't have an account? Register",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { nav.navigate("register")
            })

        }



    }
}
    }