package com.example.openhandmobile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.openhandmobile.ui.theme.Raleway
import com.example.squares.Squares
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth

@Composable
fun Settings(nav: NavHostController, modifier: Modifier = Modifier) {

    Scaffold(
        containerColor = Color(0xFF1A1A1A),
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(nav) },


        ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
        ) {
            Squares(
                modifier = Modifier.matchParentSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(10.dp))

                ProfilePanel();

                Spacer(Modifier.height(10.dp))




                SupportPanel()

                Spacer(Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        com.google.firebase.Firebase.auth.signOut()
                        nav.navigate("intro") {
                            popUpTo(nav.graph.id) { inclusive = true }
                        }
                    },
                    border = BorderStroke(2.dp, Color.White),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Sign Out",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Spacer(Modifier.height(10.dp))

            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportPanel(
    modifier: Modifier = Modifier,
) {

    val uriHandler = LocalUriHandler.current



    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color(0xFFFFFFFF)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Support",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    uriHandler.openUri("https://github.com/SandalCodez/OpenHand")
                },
                border = BorderStroke(1.dp, Color.Red),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Visit Github",
                    color = Color.Red,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePanel(
    modifier: Modifier = Modifier
) {
    val outline = Color(0xFFD1D3D5)
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var currentPass by rememberSaveable { mutableStateOf("") }
    var newPass by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    LaunchedEffect(currentUser?.uid) {
        username = currentUser?.displayName ?: currentUser?.email?.substringBefore("@") ?: ""
        email = currentUser?.email ?: ""
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color(0xFFFFFFFF)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(Modifier.height(12.dp))

            ProfileField(label = "Username", text = username, onTextChange = { username = it }, outline)
            ProfileField(label = "Email", text = email, onTextChange = { email = it }, outline)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ProfileField(
                        label = "Current Password",
                        text = currentPass,
                        onTextChange = { currentPass = it },
                        outline,
                        isPassword = true
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    ProfileField(
                        label = "New Password",
                        text = newPass,
                        onTextChange = { newPass = it },
                        outline,
                        isPassword = true
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = {
                    val user = auth.currentUser
                    if (user == null) {
                        Toast.makeText(context, "No authenticated user", Toast.LENGTH_SHORT).show()
                        return@OutlinedButton
                    }

                    val displayName = username.trim().ifEmpty {
                        user.email?.substringBefore("@") ?: "User"
                    }
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()

                    user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Profile saved", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, task.exception?.localizedMessage ?: "Failed to save profile", Toast.LENGTH_SHORT).show()
                        }
                    }

                    if (email.isNotBlank() && email != user.email) {
                        user.updateEmail(email).addOnCompleteListener { emailTask ->
                            if (!emailTask.isSuccessful) {
                                Toast.makeText(context, emailTask.exception?.localizedMessage ?: "Failed to update email", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    // Password change: requires re-auth
                    if (newPass.isNotBlank()) {
                        if (newPass.length < 6) {
                            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                            return@OutlinedButton
                        }
                        if (currentPass.isBlank()) {
                            Toast.makeText(context, "Enter your current password to change it", Toast.LENGTH_SHORT).show()
                            return@OutlinedButton
                        }
                        val userEmail = user.email
                        if (userEmail.isNullOrBlank()) {
                            Toast.makeText(context, "No email on this account; cannot change password", Toast.LENGTH_SHORT).show()
                            return@OutlinedButton
                        }
                        val credential = EmailAuthProvider.getCredential(userEmail, currentPass)
                        user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                            if (reauthTask.isSuccessful) {
                                user.updatePassword(newPass).addOnCompleteListener { pwTask ->
                                    if (pwTask.isSuccessful) {
                                        Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, pwTask.exception?.localizedMessage ?: "Failed to update password", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, reauthTask.exception?.localizedMessage ?: "Re-authentication failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                border = BorderStroke(2.dp, Color(0xFF00A6FF)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Save Changes",
                    color = Color(0xFF00A6FF),
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    text: String,
    onTextChange: (String) -> Unit,
    outlineColor: Color,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text(label, color = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = outlineColor,
            unfocusedBorderColor = outlineColor.copy(alpha = 0.6f),
            focusedLabelColor = outlineColor,
            cursorColor = outlineColor,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        ),
        textStyle = LocalTextStyle.current.copy(color = Color.White),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}

