package com.example.openhandmobile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun Settings(nav: NavHostController, modifier: Modifier = Modifier) {

    Scaffold(
        containerColor = Color(0xFF1A1A1A),
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(nav) },


        ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
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

                PreferencesPanel()

                Spacer(Modifier.height(10.dp))

                NotificationsPanel()

                Spacer(Modifier.height(10.dp))

                SupportPanel()

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesPanel(
    modifier: Modifier = Modifier,
) {
    var front by rememberSaveable { mutableStateOf(true) }
    var fingerOutlines by rememberSaveable { mutableStateOf(true) }
    var matchPercent by rememberSaveable { mutableStateOf(true) }
    var otherLesson by rememberSaveable { mutableStateOf(true) }

    val appearanceOptions = listOf("System", "Dark Mode", "Light Mode")
    var appearance by rememberSaveable { mutableStateOf("Dark Mode") }
    var appearanceExpanded by remember { mutableStateOf(false) }

    val accent = Color(0xFF00A6FF)

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color(0xFFFFFFFF)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Preferences",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(Modifier.height(12.dp))

            ToggleRow(
                title = "Front Camera by Default",
                checked = front,
                onCheckedChange = { front = it },
                accent = accent
            )

            ToggleRow(
                title = "Show Finger Outline",
                checked = fingerOutlines,
                onCheckedChange = { fingerOutlines = it },
                accent = accent
            )
            ToggleRow(
                title = "Show Match Percentage",
                checked = matchPercent,
                onCheckedChange = { matchPercent = it },
                accent = accent
            )
            ToggleRow(
                title = "Model Diagnostics",
                checked = otherLesson,
                onCheckedChange = { otherLesson = it },
                accent = accent
            )

            Spacer(Modifier.height(16.dp))
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.labelLarge.copy(color = Color.White.copy(alpha = 0.6f))
            )
            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = appearanceExpanded,
                onExpandedChange = { appearanceExpanded = !appearanceExpanded }
            ) {
                TextField(
                    value = appearance,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.White.copy(alpha = 0.35f),
                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.2f),
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedTrailingIconColor = Color.White,
                        unfocusedTrailingIconColor = Color.White
                    ),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = appearanceExpanded) },
                    placeholder = {
                        Text("Choose theme", color = Color.White.copy(alpha = 0.6f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                )
                ExposedDropdownMenu(
                    expanded = appearanceExpanded,
                    onDismissRequest = { appearanceExpanded = false }
                ) {
                    appearanceOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                appearance = option
                                appearanceExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    accent: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = accent,
                uncheckedThumbColor = Color.White.copy(alpha = 0.9f),
                uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
            )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsPanel(
    modifier: Modifier = Modifier,
) {
    var push by rememberSaveable { mutableStateOf(true) }
    var weekly by rememberSaveable { mutableStateOf(true) }
    var daily by rememberSaveable { mutableStateOf(true) }
    var friend by rememberSaveable { mutableStateOf(true) }
    var product by rememberSaveable { mutableStateOf(true) }

    val accent = Color(0xFF00A6FF)

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
                text = "Notifications",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(Modifier.height(12.dp))

            ToggleRow(
                title = "Push Notifications",
                checked = push,
                onCheckedChange = { push = it },
                accent = accent
            )

            ToggleRow(
                title = "Weekly Progress",
                checked = weekly,
                onCheckedChange = { weekly = it },
                accent = accent
            )
            ToggleRow(
                title = "Daily Practice Reminder",
                checked = daily,
                onCheckedChange = { daily = it },
                accent = accent
            )
            ToggleRow(
                title = "Friend Activity",
                checked = friend,
                onCheckedChange = { friend = it },
                accent = accent
            )
            ToggleRow(
                title = "Product Updates",
                checked = product,
                onCheckedChange = { product = it },
                accent = accent
            )


        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportPanel(
    modifier: Modifier = Modifier,
) {
    var push by rememberSaveable { mutableStateOf(true) }
    var weekly by rememberSaveable { mutableStateOf(true) }
    var daily by rememberSaveable { mutableStateOf(true) }
    var friend by rememberSaveable { mutableStateOf(true) }
    var product by rememberSaveable { mutableStateOf(true) }

    val accent = Color(0xFF00A6FF)

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
                onClick = {  },
                border = BorderStroke(1.dp, Color(0xFF00A6FF)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Open FAQ",
                    color = Color(0xFF00A6FF),
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                )
            }

            OutlinedButton(
                onClick = { /*  */ },
                border = BorderStroke(1.dp, Color(0xFF00FF7F)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Contact Support",
                    color = Color(0xFF00FF7F),
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                )
            }

            OutlinedButton(
                onClick = {  },
                border = BorderStroke(1.dp, Color.Red),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Report a Bug",
                    color = Color.Red,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                )
            }




        }
    }
}
