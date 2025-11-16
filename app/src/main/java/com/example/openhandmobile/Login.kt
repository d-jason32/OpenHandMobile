package com.example.openhandmobile

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CredentialManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.openhandmobile.ui.theme.Raleway
import com.example.squares.Squares
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import androidx.credentials.CustomCredential
import kotlinx.coroutines.launch
import android.app.Activity
import androidx.compose.runtime.rememberCoroutineScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.tasks.await

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException




@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun Login(nav: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    // needed for the text boxes
    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    var visible by remember { mutableStateOf(false) }
    val auth = Firebase.auth
    val activity = context as Activity
    val scope = rememberCoroutineScope()

    val googleLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val token = account.idToken
            if (token != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(token, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            Log.d(TAG, "Fallback GoogleSignIn success: ${auth.currentUser?.uid}")
                            nav.navigate("home") {
                                popUpTo("intro") { inclusive = true }
                            }
                        } else {
                            val msg = task2.exception?.localizedMessage ?: "Authentication failed."
                            Log.e(TAG, "Firebase auth (fallback) failed", task2.exception)
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "No ID token from Google account.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Fallback GoogleSignIn error", e)
            Toast.makeText(context, e.localizedMessage ?: "Google sign-in failed.", Toast.LENGTH_SHORT).show()
        }
    }

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
            Squares(

            )

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

                    visualTransformation =
                        if (visible) VisualTransformation.None

                        else PasswordVisualTransformation(),

                    trailingIcon = {
                        IconButton(onClick = { visible = !visible }) {
                            Icon(
                                imageVector = if (visible) Icons.Outlined.Visibility
                                else Icons.Outlined.VisibilityOff,
                                contentDescription = if (visible) "Hide password" else "Show password",
                                tint = Color(0xFF00A6FF)
                            )
                        }
                    },
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
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {

                                        nav.navigate("home") {
                                            popUpTo("intro") { inclusive = true }
                                        }
                                    } else {
                                        val msg = task.exception?.localizedMessage ?: "Authentication failed."
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                                }

                        }
                    }
                ) {
                    Text("Sign in",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold)
                }

                OrDivider()

                Button(
                    onClick = {
                        val clientId = context.getString(R.string.default_web_client_id)

                        val googleIdOption = GetGoogleIdOption.Builder()
                            .setServerClientId(clientId)
                            .setFilterByAuthorizedAccounts(false)
                            .build()

                        val request = GetCredentialRequest.Builder()
                            .addCredentialOption(googleIdOption)
                            .build()

                        SoundManager.play("click")

                        scope.launch {
                            try {
                                val credentialManager = CredentialManager.create(context)
                                val result = credentialManager.getCredential(
                                    request = request,
                                    context = activity
                                )

                                when (val cred = result.credential) {
                                    is CustomCredential -> {
                                        if (cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                            val googleCred = GoogleIdTokenCredential.createFrom(cred.data)
                                            val idToken = googleCred.idToken
                                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                                            auth.signInWithCredential(firebaseCredential)
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        Log.d(TAG, "Google sign-in success: ${auth.currentUser?.uid}")
                                                        nav.navigate("home") {
                                                            popUpTo("intro") { inclusive = true }
                                                        }
                                                    } else {
                                                        val msg = task.exception?.localizedMessage ?: "Authentication failed."
                                                        Log.e(TAG, "Firebase auth with Google failed", task.exception)
                                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                        } else {
                                            Log.e(TAG, "Unexpected credential type: ${cred.type}")
                                            Toast.makeText(context, "Unsupported credential type", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else -> {
                                        Log.w(TAG, "No GoogleCredential from CredentialManager. Falling back to GoogleSignInClient.")
                                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                            .requestIdToken(clientId)
                                            .requestEmail()
                                            .build()
                                        val signInClient = GoogleSignIn.getClient(context, gso)
                                        googleLauncher.launch(signInClient.signInIntent)
                                    }
                                }
                            } catch (e: Exception) {
                                Log.w(TAG, "Credential Manager flow failed, trying GoogleSignIn fallback", e)
                                Toast.makeText(context, "Trying Google account picker…", Toast.LENGTH_SHORT).show()
                                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(clientId)
                                    .requestEmail()
                                    .build()
                                val signInClient = GoogleSignIn.getClient(context, gso)
                                googleLauncher.launch(signInClient.signInIntent)
                            }
                        }
                    },
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

                /*
                GitHub sign in
                 */
                Button(
                    onClick = {
                        val provider = OAuthProvider.newBuilder("github.com")

                        val pendingResultTask = auth.pendingAuthResult
                        if (pendingResultTask != null) {
                            // There's something already here! Finish the sign-in for your user.
                            pendingResultTask
                                .addOnSuccessListener {
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                                    // The OAuth secret can be retrieved by calling:
                                    // ((OAuthCredential)authResult.getCredential()).getSecret().
                                    Log.d(TAG, "GitHub sign-in success:")

                                    nav.navigate("home") {
                                        popUpTo("intro") { inclusive = true }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    // Handle failure.
                                    Toast.makeText(context, "Sign-in failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            auth
                                .startActivityForSignInWithProvider(activity, provider.build())
                                .addOnSuccessListener { authResult ->
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                                    // The OAuth secret can be retrieved by calling:
                                    // ((OAuthCredential)authResult.getCredential()).getSecret().
                                    Log.d(TAG, "GitHub sign-in success: ${authResult.user?.uid}")
                                    nav.navigate("home") {
                                        popUpTo("intro") { inclusive = true }
                                    }

                                }
                                .addOnFailureListener { e ->
                                    // Handle failure.
                                    Log.e(TAG, "GitHub sign-in failed (start flow)", e)
                                    Toast.makeText(context, "Sign-in failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    },
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
                    modifier = Modifier.clickable {
                        SoundManager.play("click")
                        nav.navigate("register") })

            }

        }

    }



}



