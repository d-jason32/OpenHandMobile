package com.example.openhandmobile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.openhandmobile.ui.theme.Raleway
import com.example.squares.Squares

@Composable
fun Classes(nav: NavHostController, modifier: Modifier = Modifier) {
    val lessons = ('A'..'Z').map { "$it" }

    Scaffold(
        containerColor = Color(0xFF1A1A1A),
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(nav) },


        ) { innerPadding ->
        Squares(

        )


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Lessons A–Z",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Raleway,

                )
            }

            items(lessons) { lesson ->
                LessonBox(
                    title = "$lesson",
                    onClick = {

                    }
                )
            }

        }

    }
}


@Composable
fun LessonBox(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .background(Color.Transparent, RoundedCornerShape(12.dp))
            .border(4.dp, Color(0xFF00A6FF), RoundedCornerShape(20.dp))
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = getLessonImage(title)),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
        )
    }
}

fun getLessonImage(letter: String): Int {
    return when (letter.uppercase()) {
        "A" -> R.drawable.da
        "B" -> R.drawable.db
        "C" -> R.drawable.dc
        "D" -> R.drawable.dd
        "E" -> R.drawable.de
        "F" -> R.drawable.df
        "G" -> R.drawable.dg
        "H" -> R.drawable.dh
        "I" -> R.drawable.di
        "J" -> R.drawable.dj
        "K" -> R.drawable.dk
        "L" -> R.drawable.dl
        "M" -> R.drawable.dm
        "N" -> R.drawable.dn
        "O" -> R.drawable.do_
        "P" -> R.drawable.dp
        "Q" -> R.drawable.dq
        "R" -> R.drawable.dr
        "S" -> R.drawable.ds
        "T" -> R.drawable.dt
        "U" -> R.drawable.du
        "V" -> R.drawable.dv
        "W" -> R.drawable.dw
        "X" -> R.drawable.dx
        "Y" -> R.drawable.dy
        "Z" -> R.drawable.dz
        else -> R.drawable.da
    }
}