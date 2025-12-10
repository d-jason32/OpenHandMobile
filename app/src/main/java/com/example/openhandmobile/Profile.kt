package com.example.openhandmobile

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

data class LearnedItem(
    val name: String,
    val drawableRes: Int?
)

data class Achievement(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val icon: ImageVector = Icons.Outlined.MilitaryTech,
    val achieved: Boolean = true
)

private fun resolveLearnedDrawable(context: Context, learnedName: String): Int? {
    val drawableName = learnedToDrawableCandidate(learnedName) ?: return null
    if (drawableName.isBlank()) return null
    val pkg = context.packageName
    val id = context.resources.getIdentifier(drawableName, "drawable", pkg)
    return if (id != 0) id else null
}

private fun learnedToDrawableCandidate(learnedName: String): String? {
    val normalized = learnedName.trim().lowercase().replace(" ", "_")
    if (normalized.isBlank()) return null
    return when {
        normalized == "letter_o" -> "do_" // resource for O has an underscore suffix
        normalized.startsWith("letter_") -> "d${normalized.removePrefix("letter_")}"
        normalized.startsWith("number_") -> "d${normalized.removePrefix("number_")}"
        normalized.startsWith("word_") -> normalized.removePrefix("word_")
        normalized.startsWith("phrase_") -> normalized.removePrefix("phrase_")
        else -> normalized
    }
}


@Composable
fun Profile(nav: NavHostController, modifier: Modifier = Modifier) {

    // Pull username from Firebase Auth (fallback to email prefix or "User")
    var username by remember { mutableStateOf("User") }
    var currentXp by remember { mutableStateOf(0L) }
    var friendsCount by remember { mutableIntStateOf(0) }
    var badgeDrawables by remember { mutableStateOf<List<Int>>(emptyList()) }
    var learnedItems by remember { mutableStateOf<List<LearnedItem>>(emptyList()) }
    var skillLevel by remember { mutableStateOf("beginner") }
    var practiceFrequency by remember { mutableStateOf("daily") }
    val currentUser = Firebase.auth.currentUser
    val context = LocalContext.current

    LaunchedEffect(currentUser?.uid) {
        username = currentUser?.displayName
            ?: currentUser?.email?.substringBefore("@")
            ?: "User"

        val uid = currentUser?.uid ?: return@LaunchedEffect
        val db = Firebase.firestore
        try {
            val doc = db.collection("users").document(uid).get().await()
            if (doc.exists()) {
                currentXp = when (val raw = doc.get("xp")) {
                    is Number -> raw.toLong()
                    is String -> raw.toLongOrNull() ?: 0L
                    else -> 0L
                }
                val friends = doc.get("friends") as? List<*>
                friendsCount = friends?.size ?: 0
                val badgeNames = doc.get("badges") as? List<*>
                badgeDrawables = badgeNames?.mapNotNull { name ->
                    when (name as? String) {
                        "badge1" -> R.drawable.badge1
                        "badge2" -> R.drawable.badge2
                        "badge3" -> R.drawable.badge3
                        "badge4" -> R.drawable.badge4
                        "badge5" -> R.drawable.badge5
                        "badge6" -> R.drawable.badge6
                        "badge7" -> R.drawable.badge7
                        "badge8" -> R.drawable.badge8
                        else -> null
                    }
                } ?: emptyList()

                val learnedNames = doc.get("learned") as? List<*>
                learnedItems = learnedNames
                    ?.mapNotNull { name ->
                        val raw = name as? String ?: return@mapNotNull null
                        val drawable = resolveLearnedDrawable(context, raw)
                        LearnedItem(raw, drawable)
                    }
                    ?: emptyList()
                skillLevel = (doc.get("skill_level") as? String).orEmpty().ifBlank { "beginner" }
                practiceFrequency = (doc.get("practice_frequency") as? String).orEmpty().ifBlank { "daily" }
            }
        } catch (e: Exception) {
            // Handle error
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

                ProfilePage(
                    nav = nav,
                    username = username,
                    xp = currentXp,
                    xpTarget = 5000,
                    friendsCount = friendsCount,
                    badges = badgeDrawables,
                    learned = learnedItems,
                    skillLevel = skillLevel,
                    practiceFrequency = practiceFrequency
                )

            }

            }
        }
    }

@Composable
fun ProfilePage(
    nav: NavHostController,
    username: String,
    xp: Long,
    xpTarget: Int,
    friendsCount: Int,
    achievements: List<Achievement> = emptyList(), // deprecated; kept for signature compatibility
    badges: List<Int>,
    learned: List<LearnedItem>,
    skillLevel: String,
    practiceFrequency: String,
    modifier: Modifier = Modifier
) {
    val accent = Color(0xFF00A6FF)

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFF2A2A2A), CircleShape)
                        .border(2.dp, accent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = username.take(1).uppercase(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "@$username",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Skill: ${skillLevel.replaceFirstChar { it.uppercase() }} • Practice: ${practiceFrequency.replaceFirstChar { it.uppercase() }}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(Modifier.height(12.dp))

            val progress = (xp.toFloat() / xpTarget.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = accent,
                trackColor = Color.White.copy(alpha = 0.15f),
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "%,d / %,d XP".format(xp, xpTarget),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp
            )
            Spacer(Modifier.height(16.dp))
        }

        // Stats
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                StatChip(label = "Friends", value = friendsCount.toString(), accent = accent) {
                }
                // Streak and Accuracy chips removed
            }
            Spacer(Modifier.height(16.dp))
        }

        // Add Friend
        item {
            OutlinedButton(
                onClick = {
                    nav.navigate("addfriends")
                },
                border = BorderStroke(2.dp, accent),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = accent
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.Outlined.PersonAdd, contentDescription = "Add Friend")
                Spacer(Modifier.width(8.dp))
                Text("Add Friend")
            }
            Spacer(Modifier.height(20.dp))
        }

        if (badges.isNotEmpty()) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MilitaryTech,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Badges",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
                Spacer(Modifier.height(10.dp))
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(badges.size) { i ->
                        BadgeImageCard(badgeRes = badges[i])
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
        }

        if (learned.isNotEmpty()) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MilitaryTech,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Learned",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
                Spacer(Modifier.height(10.dp))
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(learned.size) { i ->
                        LearnedCard(item = learned[i])
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }

}


@Composable
private fun StatChip(
    label: String,
    value: String,
    accent: Color,
    onClick: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(14.dp)
    val content = @Composable {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }

    if (onClick != null) {
        OutlinedButton(
            onClick = onClick,
            border = BorderStroke(2.dp, accent),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
            shape = shape,
            contentPadding = PaddingValues(0.dp)
        ) { content() }
    } else {
        Box(
            modifier = Modifier
                .border(2.dp, accent, shape)
                .clip(shape)
        ) { content() }
    }
}


@Composable
private fun BadgeImageCard(badgeRes: Int) {
    val accent = Color(0xFF00A6FF)
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = Modifier
            .width(180.dp)
            .height(160.dp)
            .border(2.dp, accent, shape)
            .clip(shape)
            .background(Color(0xFF2A2A2A).copy(alpha = 0.5f))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = badgeRes),
            contentDescription = "Badge",
            modifier = Modifier.size(120.dp)
        )
    }
}

@Composable
private fun LearnedCard(item: LearnedItem) {
    val accent = Color(0xFF00A6FF)
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = Modifier
            .width(180.dp)
            .height(160.dp)
            .border(2.dp, accent, shape)
            .clip(shape)
            .background(Color(0xFF2A2A2A).copy(alpha = 0.5f))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (item.drawableRes != null) {
            Image(
                painter = painterResource(id = item.drawableRes),
                contentDescription = item.name,
                modifier = Modifier.size(120.dp)
            )
        } else {
            Text(
                text = item.name.replace("_", " ").replaceFirstChar { it.uppercase() },
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
