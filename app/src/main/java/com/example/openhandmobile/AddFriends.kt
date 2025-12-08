package com.example.openhandmobile


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.openhandmobile.ui.theme.Raleway
import com.example.squares.Squares
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

@Composable
fun AddFriends(nav: NavHostController, modifier: Modifier = Modifier) {

    var username by remember { mutableStateOf("User") }
    var allUsers by remember { mutableStateOf<List<FriendCandidate>>(emptyList()) }
    val currentUser = Firebase.auth.currentUser
    val context = LocalContext.current
    LaunchedEffect(currentUser?.uid) {
        username = currentUser?.displayName
            ?: currentUser?.email?.substringBefore("@")
                    ?: "User"

        val uid = currentUser?.uid ?: return@LaunchedEffect
        val db = Firebase.firestore
        try {
            val snaps = db.collection("users").get().await()
            val list = snaps.documents.mapNotNull { doc ->
                val id = doc.id
                if (id == uid) return@mapNotNull null
                val name = (doc.getString("userName") ?: doc.getString("nickname") ?: "User").ifBlank { "User" }
                val xpVal = when (val raw = doc.get("xp")) {
                    is Number -> raw.toLong()
                    is String -> raw.toLongOrNull() ?: 0L
                    else -> 0L
                }
                FriendCandidate(id, name, xpVal)
            }.sortedByDescending { it.xp }
            allUsers = list
            if (list.isEmpty()) {
                Toast.makeText(context, "No other users found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            allUsers = emptyList()
            Toast.makeText(context, e.localizedMessage ?: "Failed to load users", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        containerColor = Color(0xFF1A1A1A),
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(nav) },


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
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SingleChoiceSegmentedButtonFriends(
                    nav = nav,
                    modifier = modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "@$username",
                    fontSize = 36.sp,
                    color = Color.White
                )

                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Group,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Add Friends",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }


                Spacer(Modifier.height(8.dp))


                HorizontalDivider(Modifier, DividerDefaults.Thickness, color = Color(0xFFFFFFFF))

                Spacer(Modifier.height(16.dp))


                Spacer(Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    itemsIndexed(allUsers) { index, candidate ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2A2A2A)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = candidate.name.firstOrNull()?.uppercase() ?: "?",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = candidate.name,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                            }

                            OutlinedButton(
                                onClick = {
                                    val uid = currentUser?.uid
                                    if (uid == null) {
                                        Toast.makeText(context, "Not signed in", Toast.LENGTH_SHORT).show()
                                        return@OutlinedButton
                                    }
                                    val db = Firebase.firestore
                                    db.collection("users").document(uid)
                                        .update("friends", FieldValue.arrayUnion(candidate.id))
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(context, "Added ${candidate.name}", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, task.exception?.localizedMessage ?: "Failed to add friend", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                },
                                border = BorderStroke(2.dp, Color.White),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 40.dp, vertical = 4.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Add Friend", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class FriendCandidate(val id: String, val name: String, val xp: Long)