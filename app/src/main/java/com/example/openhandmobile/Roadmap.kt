package com.example.openhandmobile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavHostController
import com.example.openhandmobile.ui.theme.Raleway
import com.example.squares.Squares

@Composable
fun Roadmap(nav: NavHostController, modifier: Modifier = Modifier) {

    Scaffold(
        containerColor = Color(0xFF1A1A1A),
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(nav) },


        ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Squares(
                modifier = Modifier.matchParentSize()
            )


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CarouselExample(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 50.dp, vertical = 20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CarouselExample(modifier: Modifier = Modifier) {
    data class CarouselItem(
        val id: Int,
        @DrawableRes val imageResId: Int,
        val contentDescription: String,
        val locked: Boolean = false
    )

    val carouselItems = remember {
        listOf(
            CarouselItem(0, R.drawable.handy_white_logo, "Letters - Easy", locked = false),
            CarouselItem(1, R.drawable.handy_closed_eyes, "Letters - Medium", locked = false),
            CarouselItem(2, R.drawable.handy_smart_crop_fix, "Letters - Hard", locked = false),
            CarouselItem(3, R.drawable.handy_white_logo, "Letters and Numbers - Easy", locked = false),
            CarouselItem(4, R.drawable.handy_closed_eyes, "Letters and Numbers - Medium", locked = false),
            CarouselItem(5, R.drawable.handy_smart_crop_fix, "Letters and Numbers - Hard", locked = false),
            CarouselItem(6, R.drawable.handy_smart_crop_fix, "Common Words - Hard", locked = false),
        )
    }

    Box(modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(pageCount = { carouselItems.size })

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { i ->
            val item = carouselItems[i]
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(4.dp, if (item.locked) Color(0xFF5A5A5A) else Color(0xFF00A6FF))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 30.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val parts = item.contentDescription.split(" - ", limit = 2)
                    val title = parts.getOrNull(0) ?: ""
                    val difficulty = parts.getOrNull(1)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = title,
                            color = Color.White,
                            style = TextStyle(
                                fontFamily = Raleway,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                        if (item.locked) {
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = "Locked",
                                tint = Color(0xFF9AA0A6)
                            )
                        }
                    }

                    Image(
                        painter = painterResource(id = item.imageResId),
                        contentDescription = item.contentDescription,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .size(180.dp)
                            .alpha(if (item.locked) 0.5f else 1f),
                        contentScale = ContentScale.Fit
                    )

                    if (!difficulty.isNullOrBlank()) {
                        Text(
                            text = "Difficulty: $difficulty",
                            color = if (item.locked) Color(0xFF9AA0A6) else Color(0xFF00A6FF),
                            style = TextStyle(
                                fontFamily = Raleway,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    } else {
                        Spacer(Modifier.height(20.dp))
                    }
                    Button(
                        onClick = { /* navigate when unlocked */ },
                        enabled = !item.locked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (item.locked) Color(0xFF3A3A3A) else Color(0xFF00A6FF),
                            contentColor = if (item.locked) Color(0xFF9AA0A6) else Color.Black,
                            disabledContainerColor = Color(0xFF3A3A3A),
                            disabledContentColor = Color(0xFF9AA0A6)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = if (item.locked) "Locked" else "Start class",
                            style = TextStyle(
                                fontFamily = Raleway,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                        )
                    }
                }
            }
        }
    }
}


// Numbers

// Letters

// Numbers + letters

// Common words

// Letters and words