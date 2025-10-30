package com.example.openhandmobile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.openhandmobile.ui.theme.Raleway
import com.example.squares.Squares


@Composable
fun Profile(nav: NavHostController, modifier: Modifier = Modifier) {

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
                    username = "your_username",
                    level = 10,
                    xp = 20000,
                    xpTarget = 50000,
                    friendsCount = 12,
                    streakDays = 7,
                    accuracyPercent = 90,
                    achievements = listOf(
                        Achievement("a1", "Level 10 Reached", "Hit level 10"),
                        Achievement("a2", "7-Day Streak", "Learned for 1 weeek straight"),
                        Achievement("a3", "Perfect ‘A-Z’", "Finished alphabet course")
                    ),
                    badges = listOf(
                        Achievement("b1", "Smart Hand", "Finish all the lessons."),
                        Achievement("b2", "Speedster", "Finish a lesson in under a minute."),
                        Achievement("b3", "Early Bird", "Finish a lesson before 9 A.M.")
                    )
                )

            }

            }
        }
    }

@Composable
fun ProfilePage(
    nav: NavHostController,
    username: String,
    level: Int,
    xp: Int,
    xpTarget: Int,
    friendsCount: Int,
    streakDays: Int,
    accuracyPercent: Int,
    achievements: List<Achievement>,
    badges: List<Achievement>,
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.EmojiEvents,
                            contentDescription = "Level",
                            tint = Color.White
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Level $level",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatChip(label = "Friends", value = friendsCount.toString(), accent = accent) {
                }
                StatChip(label = "Streak", value = "${streakDays}d", accent = accent)
                StatChip(label = "Accuracy", value = "$accuracyPercent%", accent = accent)
            }
            Spacer(Modifier.height(16.dp))
        }

        // Add Friend
        item {
            OutlinedButton(
                onClick = {  },
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

        if (achievements.isNotEmpty()) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Achievements",
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
                    items(achievements.size) { i ->
                        AchievementCard(achievements[i])
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
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
                        BadgesCard(badges[i])
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
                .widthIn(min = 96.dp)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.Start
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

data class Achievement(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val icon: ImageVector = Icons.Outlined.MilitaryTech,
    val achieved: Boolean = true
)

@Composable
private fun AchievementCard(a: Achievement) {
    val accent = Color(0xFF00A6FF)
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .width(180.dp)
            .height(160.dp)
            .border(2.dp, if (a.achieved) accent else Color.White.copy(alpha = 0.35f), shape)
            .clip(shape)
            .background(Color(0xFF2A2A2A).copy(alpha = 0.5f))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = a.icon,
                contentDescription = a.title,
                tint = if (a.achieved) accent else Color.White.copy(alpha = 0.6f)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                a.title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (a.subtitle.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(
                a.subtitle,
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
private fun BadgesCard(a: Achievement) {
    val accent = Color(0xFF00A6FF)
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .width(180.dp)
            .height(160.dp)
            .border(2.dp, if (a.achieved) accent else Color.White.copy(alpha = 0.35f), shape)
            .clip(shape)
            .background(Color(0xFF2A2A2A).copy(alpha = 0.5f))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = a.icon,
                contentDescription = a.title,
                tint = if (a.achieved) accent else Color.White.copy(alpha = 0.6f)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                a.title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (a.subtitle.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(
                a.subtitle,
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
