package com.example.openhandmobile.classes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.openhandmobile.BottomNavBar
import com.example.openhandmobile.R
import com.example.squares.Squares

@Composable
fun PracticeScreen(
    nav: NavHostController,
    modifier: Modifier = Modifier,
    title: String,
    gifContent: @Composable () -> Unit,
    gradingId: String
) {
    Scaffold(
        containerColor = Color(0xFF1A1A1A),
        modifier = modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(nav) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(22.dp)
        ) {

            Squares(modifier = Modifier.matchParentSize())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(22.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Image(
                    painter = painterResource(id = R.drawable.handy_smart_crop_fix),
                    contentDescription = "Handy",
                    modifier = Modifier.size(150.dp)
                )

                Text(
                    text = title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 36.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                gifContent()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { nav.navigate("grading/$gradingId") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    border = BorderStroke(2.dp, Color(0xFF00A6FF)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Text(
                        "Test your sign!",
                        color = Color(0xFF00A6FF),
                        fontSize = 16.sp
                    )
                }
                OutlinedButton(
                    onClick = { nav.navigate("classes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    border = BorderStroke(2.dp, Color(0xFF00A6FF)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Text(
                        "Go Back",
                        color = Color(0xFF00A6FF),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------
//  LETTER CLASSES (A–Z)
//  IDs: letter_A, letter_B, ...
// ----------------------------------------------------
@Composable fun classA(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter A!", { gifA() }, "letter_A")

@Composable fun classB(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter B!", { gifB() }, "letter_B")

@Composable fun classC(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter C!", { gifC() }, "letter_C")

@Composable fun classD(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter D!", { gifD() }, "letter_D")

@Composable fun classE(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter E!", { gifE() }, "letter_E")

@Composable fun classF(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter F!", { gifF() }, "letter_F")

@Composable fun classG(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter G!", { gifG() }, "letter_G")

@Composable fun classH(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter H!", { gifH() }, "letter_H")

@Composable fun classI(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter I!", { gifI() }, "letter_I")

@Composable fun classJ(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter J!", { gifJ() }, "letter_J")

@Composable fun classK(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter K!", { gifK() }, "letter_K")

@Composable fun classL(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter L!", { gifL() }, "letter_L")

@Composable fun classM(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter M!", { gifM() }, "letter_M")

@Composable fun classN(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter N!", { gifN() }, "letter_N")

@Composable fun classO(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter O!", { gifO() }, "letter_O")

@Composable fun classP(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter P!", { gifP() }, "letter_P")

@Composable fun classQ(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter Q!", { gifQ() }, "letter_Q")

@Composable fun classR(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter R!", { gifR() }, "letter_R")

@Composable fun classS(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter S!", { gifS() }, "letter_S")

@Composable fun classT(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter T!", { gifT() }, "letter_T")

@Composable fun classU(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter U!", { gifU() }, "letter_U")

@Composable fun classV(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter V!", { gifV() }, "letter_V")

@Composable fun classW(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter W!", { gifW() }, "letter_W")

@Composable fun classX(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter X!", { gifX() }, "letter_X")

@Composable fun classY(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter Y!", { gifY() }, "letter_Y")

@Composable fun classZ(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Letter Z!", { gifZ() }, "letter_Z")

// ----------------------------------------------------
//  NUMBER CLASSES (0–9)
//  IDs: number_0, number_1, ...
// ----------------------------------------------------
@Composable fun class0(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Number 0!", { gif0() }, "number_0")

@Composable fun class1(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Number 1!", { gif1() }, "number_1")

@Composable fun class2(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Number 2!", { gif2() }, "number_2")

@Composable fun class3(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Number 3!", { gif3() }, "number_3")

@Composable fun class4(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Number 4!", { gif4() }, "number_4")

@Composable fun class5(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Number 5!", { gif5() }, "number_5")

@Composable fun class6(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Number 6!", { gif6() }, "number_6")

@Composable fun class7(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Number 7!", { gif7() }, "number_7")

@Composable fun class8(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Number 8!", { gif8() }, "number_8")

@Composable fun class9(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Number 9!", { gif9() }, "number_9")

// ----------------------------------------------------
//  WORD CLASSES
//  IDs: word_MILK, word_MORE, ...
// ----------------------------------------------------
@Composable fun classMilk(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word MILK!", { gifMilk() }, "word_MILK")

@Composable fun classMore(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word MORE!", { gifMore() }, "word_MORE")

@Composable fun classAllDone(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word ALL DONE!", { gifAllDone() }, "word_ALL_DONE")

@Composable fun classEat(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word EAT!", { gifEat() }, "word_EAT")

@Composable fun classDrink(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word DRINK!", { gifDrink() }, "word_DRINK")

@Composable fun classSleep(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word SLEEP!", { gifSleep() }, "word_SLEEP")

@Composable fun classDiaper(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word DIAPER!", { gifDiaper() }, "word_DIAPER")

@Composable fun classBath(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word BATH!", { gifBath() }, "word_BATH")

@Composable fun classMom(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word MOM!", { gifMom() }, "word_MOM")

@Composable fun classDad(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word DAD!", { gifDad() }, "word_DAD")

@Composable fun classPlease(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word PLEASE!", { gifPlease() }, "word_PLEASE")

@Composable fun classThankYou(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word THANK YOU!", { gifThankYou() }, "word_THANK_YOU")

@Composable fun classHelp(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word HELP!", { gifHelp() }, "word_HELP")

@Composable fun classLove(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word LOVE!", { gifLove() }, "word_LOVE")

@Composable fun classSorry(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word SORRY!", { gifSorry() }, "word_SORRY")

@Composable fun classPlay(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word PLAY!", { gifPlay() }, "word_PLAY")

@Composable fun classBook(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word BOOK!", { gifBook() }, "word_BOOK")

@Composable fun classBall(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word BALL!", { gifBall() }, "word_BALL")

@Composable fun classDog(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word DOG!", { gifDog() }, "word_DOG")

@Composable fun classMusic(nav: NavHostController, modifier: Modifier = Modifier) =
    PracticeScreen(nav, modifier, "Practice Word MUSIC!", { gifMusic() }, "word_MUSIC")