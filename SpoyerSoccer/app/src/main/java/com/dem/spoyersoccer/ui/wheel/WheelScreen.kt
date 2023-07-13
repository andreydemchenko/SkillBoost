package com.dem.spoyersoccer.ui.wheel

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.dem.spoyersoccer.MyApplication
import com.dem.spoyersoccer.R
import com.dem.spoyersoccer.models.Bet
import com.dem.spoyersoccer.models.LiveMatch
import com.dem.spoyersoccer.models.LiveMatchOdds
import com.dem.spoyersoccer.models.NoteEntity
import com.dem.spoyersoccer.models.Odds
import com.dem.spoyersoccer.utils.AppContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelScreen() {

    val matchState = remember { mutableStateOf<LiveMatch?>(null) }
    val manager = AppContext.getInstance().networkManager

    val betField = remember { mutableStateOf("") }

    val context = LocalContext.current
    val betsDao = MyApplication.database.noteDao()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val liveMatches = manager.getLiveMatches()
        val randomMatchIndex = (0..liveMatches.size).random()
        matchState.value = liveMatches[randomMatchIndex]
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(start = 40.dp, top = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Wheel of fortune".uppercase(),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_bold)),
            fontWeight = FontWeight(818),
            fontStyle = FontStyle.Italic,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        matchState.value?.let { match ->
            val oddsState = remember {
                mutableStateOf(
                    LiveMatchOdds(
                        id = 0,
                        home = Odds(0, 1.1, ""),
                        draw = Odds(0, 1.8, ""),
                        away = Odds(0, 1.18, "")
                    )
                )
            }
            //val eventState = remember { mutableStateOf<Event?>(null) }

            LaunchedEffect(Unit) {
                val odds = manager.getMatchOdds(match.bet365Id)
                oddsState.value = odds
//        val event = manager.getEvent(match.id)
//        eventState.value = event
            }

            val dateFormatted = SimpleDateFormat("dd.MM.yyyy").format(match.time)
            val isHomeImageError = remember { mutableStateOf(false) }
            val isAwayImageError = remember { mutableStateOf(false) }
            val selectedBet = remember { mutableStateOf(0) }

            Column(
                modifier = Modifier
                    .padding(start = 5.dp)
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Column(
                        modifier = Modifier.width(80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            rememberAsyncImagePainter(
                                match.home.imageUrl,
                                onError = { isHomeImageError.value = true }),
                            contentDescription = "Team Image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(40.dp)
                        )
                        if (isHomeImageError.value) {
                            Image(
                                rememberAsyncImagePainter(R.drawable.team_icon),
                                contentDescription = "Team Image",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Text(
                            modifier = Modifier
                                .heightIn(max = 140.dp)
                                .padding(16.dp),
                            text = match.home.name.uppercase(),
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            fontWeight = FontWeight(818),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                    Column(
                        modifier = Modifier.width(80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            // modifier = Modifier.padding(16.dp),
                            text = dateFormatted,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier.padding(6.dp),
                            text = "VS",
                            fontSize = 32.sp,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            fontWeight = FontWeight(500),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                    Column(
                        modifier = Modifier.width(80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            rememberAsyncImagePainter(
                                match.away.imageUrl,
                                onError = { isAwayImageError.value = true }),
                            contentDescription = "Team Image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(40.dp)
                        )
                        if (isAwayImageError.value) {
                            Image(
                                rememberAsyncImagePainter(R.drawable.team2_icon),
                                contentDescription = "Team Image",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Text(
                            modifier = Modifier
                                .heightIn(max = 140.dp)
                                .padding(6.dp),
                            text = match.away.name.uppercase(),
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            fontWeight = FontWeight(818),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .width(60.dp)
                            .run {
                                if (selectedBet.value == 1) {
                                    border(
                                        BorderStroke(3.dp, Color.White),
                                    )
                                } else {
                                    this
                                }
                            }
                            .clickable { selectedBet.value = 1 },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0XFFEA9C01))
                                .padding(vertical = 6.dp, horizontal = 10.dp),
                            text = "1",
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0XFFE5857F))
                                .padding(vertical = 6.dp, horizontal = 10.dp),
                            text = "${oddsState.value.home.value}",
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .width(60.dp)
                            .run {
                                if (selectedBet.value == 2) {
                                    border(
                                        BorderStroke(3.dp, Color.White),
                                    )
                                } else {
                                    this
                                }
                            }
                            //.border(2.dp, Color.White, RoundedCornerShape(4.dp))
                            .clickable { selectedBet.value = 2 },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0XFFEA9C01))
                                .padding(vertical = 6.dp, horizontal = 10.dp),
                            text = "x",
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0XFFE5857F))
                                .padding(vertical = 6.dp, horizontal = 10.dp),
                            text = "${oddsState.value.draw.value}",
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .width(60.dp)
                            .run {
                                if (selectedBet.value == 3) {
                                    border(
                                        BorderStroke(3.dp, Color.White)
                                    )
                                } else {
                                    this
                                }
                            }
                            .clickable { selectedBet.value = 3 },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0XFFEA9C01))
                                .padding(vertical = 6.dp, horizontal = 10.dp),
                            text = "2",
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0XFFE5857F))
                                .padding(vertical = 6.dp, horizontal = 10.dp),
                            text = "${oddsState.value.away.value}",
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                TextField(
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    value = betField.value,
                    onValueChange = { newText -> betField.value = newText } ,
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                    shape = RoundedCornerShape(7.dp),
                    placeholder = {
                        Text(
                            text = "Your predictions",
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.inter_bold)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFF536575),
                            textAlign = TextAlign.Center
                        )
                    }
                )
                Button(
                    modifier = Modifier.padding(top = 100.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(7.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFEA9C01)),
                    onClick = {
                        if (selectedBet.value == 0) {
                            Toast.makeText(context, "Choose a bet!", Toast.LENGTH_SHORT).show()
                        } else if (betField.value.trim().isBlank()) {
                            Toast.makeText(context, "Type amount of virtual money!", Toast.LENGTH_SHORT).show()
                        } else {
                           val bet = Bet(
                               id = (100..10000).random(),
                               match = match,
                               odds = oddsState.value,
                               betOn = selectedBet.value,
                               amount = betField.value.toDouble()
                           )
                            coroutineScope.launch(Dispatchers.IO) {
                                betsDao.insert(bet)
                            }
                            Toast.makeText(context, "You've made a bet!", Toast.LENGTH_SHORT).show()
                            betField.value = ""
                            selectedBet.value = 0
                        }
                    }) {
                    Text(
                        text = "Make a bet".uppercase(),
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.inter_bold)),
                        fontWeight = FontWeight(700),
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}