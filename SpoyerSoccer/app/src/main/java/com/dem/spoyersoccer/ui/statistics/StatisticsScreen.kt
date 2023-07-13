package com.dem.spoyersoccer.ui.statistics

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.dem.spoyersoccer.R
import com.dem.spoyersoccer.models.Event
import com.dem.spoyersoccer.models.League
import com.dem.spoyersoccer.ui.theme.SpoyerSoccerTheme
import com.dem.spoyersoccer.utils.AppContext
import java.text.SimpleDateFormat

@Composable
fun StatisticsScreen(gameId: Int) {

    val eventState = remember { mutableStateOf<Event?>(null) }
    val manager = AppContext.getInstance().networkManager

    LaunchedEffect(Unit) {
        Log.d("id ===== ", gameId.toString())
        val event = manager.getEvent(gameId)
        eventState.value = event
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(start = 40.dp, top = 20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Statistics".uppercase(),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_bold)),
            fontWeight = FontWeight(818),
            fontStyle = FontStyle.Italic,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        eventState.value?.let { match ->
            val dateFormatted = SimpleDateFormat("dd.MM.yyyy").format(match.time)
            val isHomeImageError = remember { mutableStateOf(false) }
            val isAwayImageError = remember { mutableStateOf(false) }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Column(
                    modifier = Modifier.width(100.dp),
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
                            .heightIn(max = 160.dp)
                            .padding(16.dp),
                        text = match.home.name.uppercase(),
                        fontSize = 14.sp,
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
                        text = match.score,
                        fontSize = 32.sp,
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        fontWeight = FontWeight(500),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    modifier = Modifier.width(100.dp),
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
                            .heightIn(max = 160.dp)
                            .padding(6.dp),
                        text = match.away.name.uppercase(),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        fontWeight = FontWeight(818),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            val statistics = match.getStatisticsModel()
            statistics.corners?.let {
                StatsItem(name = "corners", value = it)
            }
            statistics.goals?.let {
                StatsItem(name = "goals", value = it)
            }
            statistics.penalties?.let {
                StatsItem(name = "penalties", value = it)
            }
            statistics.redCards?.let {
                StatsItem(name = "red cards", value = it)
            }
            statistics.yellowCards?.let {
                StatsItem(name = "yellow cards", value = it)
            }
            statistics.substitutions?.let {
                StatsItem(name = "substitutions", value = it)
            }
            CourtView()
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun StatsItem(name: String, value: Pair<Int, Int>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${value.first}",
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontWeight = FontWeight(500),
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = name,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontWeight = FontWeight(818),
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = "${value.second}",
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontWeight = FontWeight(500),
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CourtView() {
    val randomValue = (30..70).random()
    val heightValue = (271 * randomValue / 100).dp
    Box(modifier = Modifier
        .width(177.dp)
        .height(271.dp)) {
        Image(painter = painterResource(R.drawable.soccer_court), contentDescription = "")
        Column {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(heightValue)
                .background(Color(0XFFD9D9D9).copy(0.5f)),
            contentAlignment = Alignment.Center) {
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0XFF424242).copy(0.5f))
                        .padding(horizontal = 6.dp),
                    text = "$randomValue%",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter_bold)),
                    fontWeight = FontWeight(818),
                    color = Color.White,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourtViewPreview() {
    SpoyerSoccerTheme {
        CourtView()
    }
}