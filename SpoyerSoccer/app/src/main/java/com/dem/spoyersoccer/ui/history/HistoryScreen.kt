package com.dem.spoyersoccer.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import coil.compose.rememberAsyncImagePainter
import com.dem.spoyersoccer.MyApplication
import com.dem.spoyersoccer.R
import com.dem.spoyersoccer.models.Bet
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

@Composable
fun HistoryScreen() {

    val dao = MyApplication.database.noteDao()
    val betsState: MutableState<List<Bet>> = remember { mutableStateOf(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    val allEarnedMoney = remember { mutableStateOf(0) }
    val allLostMoney = remember { mutableStateOf(0) }

    DisposableEffect(Unit) {
        val betsObserver = Observer<List<Bet>> { bets ->
            betsState.value = bets
        }

        val liveData = dao.getAllBets()
        liveData.observeForever(betsObserver)

        onDispose {
            liveData.removeObserver(betsObserver)
        }
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(start = 40.dp, top = 20.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "History of luck".uppercase(),
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.inter_bold)),
                fontWeight = FontWeight(818),
                fontStyle = FontStyle.Italic,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            LazyColumn {
                itemsIndexed(betsState.value) { _, bet ->
                    HistoryItem(bet) { result ->
                        if (result > 0) {
                            allEarnedMoney.value += result
                        } else {
                            allLostMoney.value += result
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier.padding(bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                colors = CardDefaults.cardColors(Color.Black.copy(0.7f)),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Text(
                            text = "Virtual money earned:",
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            fontWeight = FontWeight(600),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            modifier = Modifier
                                .width(80.dp)
                                //.padding(horizontal = 40.dp)
                                .background(Color.White),
                            text = "${allEarnedMoney.value}",
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            fontWeight = FontWeight(600),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                    Row {
                        Text(
                            text = "Virtual money lost:",
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            fontWeight = FontWeight(600),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            modifier = Modifier
                                .width(80.dp)
                                //.padding(horizontal = 40.dp)
                                .background(Color(0XFFFDC306)),
                            text = "${allLostMoney.value}",
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            fontWeight = FontWeight(600),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun HistoryItem(bet: Bet, resultValue: (Int) -> Unit) {
    val dateFormatted = SimpleDateFormat("dd.MM.yyyy").format(bet.match.time)
    val isHomeImageError = remember { mutableStateOf(false) }
    val isAwayImageError = remember { mutableStateOf(false) }
    val matchResult = remember { mutableStateOf(0) }
    val betResult = remember { mutableStateOf("") }
    val betResultValue = remember { mutableStateOf(0) }

    val betValue = when (bet.betOn) {
        1 -> (bet.amount * bet.odds.home.value).roundToInt()
        2 -> (bet.amount * bet.odds.draw.value).roundToInt()
        3 -> (bet.amount * bet.odds.away.value).roundToInt()
        else -> 0
    }
    LaunchedEffect(Unit) {
        if (bet.match.timeStatus == 1) {
            val homeScore = bet.match.score[0].toInt()
            val awayScore = bet.match.score[2].toInt()
            if (homeScore == awayScore) {
                matchResult.value = 2
                if (bet.betOn == 2) {
                    betResultValue.value = betValue
                    betResult.value = "+$betValue"
                } else {
                    betResultValue.value = -1 * (bet.amount.roundToInt())
                    betResult.value = "-${bet.amount}"
                }
            } else if (homeScore > awayScore) {
                matchResult.value = 1
                if (bet.betOn == 1) {
                    betResultValue.value = betValue
                    betResult.value = "+$betValue"
                } else {
                    betResultValue.value = -1 * (bet.amount.roundToInt())
                    betResult.value = "-${bet.amount}"
                }
            } else {
                matchResult.value = 3
                if (bet.betOn == 3) {
                    betResultValue.value = betValue
                    betResult.value = "+$betValue"
                } else {
                    betResultValue.value = -1 * (bet.amount.roundToInt())
                    betResult.value = "-${bet.amount}"
                }
            }
        }

        val res = betResultValue.value
        resultValue(res)
    }

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
                        bet.match.home.imageUrl,
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
                    text = bet.match.home.name.uppercase(),
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
                    text = bet.match.score,
                    fontSize = 32.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    fontWeight = FontWeight(500),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .padding(vertical = 6.dp, horizontal = 12.dp)
                        .background(
                            when (matchResult.value) {
                                1 -> Color.Green
                                2 -> Color(0XFFFDC306)
                                else -> Color.Gray
                            }
                        ),
                    text = if (matchResult.value == 0) "is going" else betResult.value,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    fontWeight = FontWeight(500),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
            Column(
                modifier = Modifier.width(80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    rememberAsyncImagePainter(
                        bet.match.away.imageUrl,
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
                    text = bet.match.away.name.uppercase(),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    fontWeight = FontWeight(818),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}