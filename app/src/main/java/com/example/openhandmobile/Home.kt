package com.example.openhandmobile

import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.openhandmobile.ui.theme.Raleway
import com.example.squares.Squares


@Composable
fun Home(nav: NavHostController, modifier: Modifier = Modifier) {

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

                Spacer(Modifier.height(24.dp))

                Image(
                    painter = painterResource(id = R.drawable.hi_transparent_copy),
                    contentDescription = "Handy",
                    modifier = Modifier
                        .width(250.dp)

                )

                Spacer(Modifier.height(10.dp))


                Image(
                    painter = painterResource(id = R.drawable.handy_smart_crop_fix),
                    contentDescription = "Handy",
                    modifier = Modifier
                        .size(250.dp)

                )

                Text(
                    text = "OpenHand",
                    fontFamily = Raleway,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        fontSize = 48.sp
                    ),
                    color = Color(0xFF00A6FF),
                    modifier = Modifier.padding(top = 26.dp)

                )

                Text(
                    text = "Empowering Communication for All.",
                    style = TextStyle(
                        color = Color(0xFFFFFFFF),
                        fontSize = 16.sp
                    ),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)

                )


            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            )
            {
                OutlinedButton(
                    onClick = { nav.navigate("classZ") },
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    border = BorderStroke(2.dp, Color(0xFF00A6FF)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF00A6FF)
                    )
                ) {
                    Text("Continue lesson",
                        fontSize = 16.sp)
                }

                OutlinedButton(
                    onClick = { nav.navigate("modeltest") },
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    border = BorderStroke(2.dp, Color(0xFFFFFFFF)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )

                ) {
                    Text(
                        "Try the model",
                        color = Color(0xFFFFFFFF),
                            fontSize = 16.sp)

                }

                Spacer(Modifier.height(10.dp))


            }
            }
        }
    }


@Composable
fun SingleChoiceSegmentedButtonFriends(
    nav: NavHostController,
    modifier: Modifier = Modifier
) {
    val labels = listOf("Profile", "Friends")
    val routes = listOf("profile", "friends")

    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val selectedIndex = when (currentRoute) {
        "profile" -> 0
        "friends" -> 1
        else -> 0
    }

    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth().padding(horizontal = 10.dp)
    ) {
        labels.forEachIndexed { index, label ->
            SegmentedButton(
                modifier = Modifier
                    .weight(2f),

                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = labels.size
                ),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = Color.Transparent,
                    inactiveContainerColor = Color.Transparent,
                    activeContentColor = Color.White,
                    inactiveContentColor = Color.White,
                    activeBorderColor = Color(0xFF00A6FF),
                    inactiveBorderColor = Color.White
                ),
                onClick = {
                    val targetRoute = routes[index]
                    if (currentRoute != targetRoute) {
                        nav.navigate(targetRoute) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(nav.graph.startDestinationId) { saveState = true }
                        }
                    }
                },
                selected = index == selectedIndex,
                label = { Text(label) }
            )
        }
    }
}
