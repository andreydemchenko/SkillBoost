package com.dem.spoyersoccer.ui.factor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.dem.spoyersoccer.R
import com.dem.spoyersoccer.models.Event
import com.dem.spoyersoccer.models.LiveMatch
import com.dem.spoyersoccer.models.LiveMatchOdds
import com.dem.spoyersoccer.models.Odds
import com.dem.spoyersoccer.utils.AppContext
import java.text.SimpleDateFormat

@Composable
fun FactorScreen() {

    val matchesState = remember { mutableStateOf(emptyList<LiveMatch>()) }
    val manager = AppContext.getInstance().networkManager

    LaunchedEffect(Unit) {
        val liveMatches = manager.getLiveMatches()
        matchesState.value = liveMatches
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(start = 40.dp, top = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Factor".uppercase(),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_bold)),
            fontWeight = FontWeight(818),
            fontStyle = FontStyle.Italic,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        LazyColumn {
            itemsIndexed(matchesState.value) { _, match ->
                LiveMatchItem(match)
            }
        }
    }
}

@Composable
fun LiveMatchItem(match: LiveMatch) {

    val manager = AppContext.getInstance().networkManager
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

    Column(
        modifier = Modifier
            .padding(start = 5.dp)
            .fillMaxWidth()
            .height(360.dp)
            .padding(vertical = 10.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.5f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    text = match.score,
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
                        rememberAsyncImagePainter(R.drawable.team_icon),
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(2f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .background(Color(0XFFD38F06))
                        .padding(vertical = 6.dp, horizontal = 10.dp),
                    text = "${oddsState.value.home.value}",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .background(Color(0XFFD38F06))
                        .padding(vertical = 6.dp, horizontal = 10.dp),
                    text = "${oddsState.value.draw.value}",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .background(Color(0XFFD38F06))
                        .padding(vertical = 6.dp, horizontal = 10.dp),
                    text = "${oddsState.value.away.value}",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
             Column(
                 modifier = Modifier.weight(1f),
                 horizontalAlignment = Alignment.CenterHorizontally
             ) {
                 Image(
                     rememberAsyncImagePainter(R.drawable.live_icon),
                     contentDescription = "Match live Image",
                     contentScale = ContentScale.Fit,
                     modifier = Modifier.size(40.dp)
                 )
                 Text(
                     modifier = Modifier.padding(6.dp),
                     text = "MatchLive".uppercase(),
                     fontSize = 12.sp,
                     fontFamily = FontFamily(Font(R.font.inter_regular)),
                     fontWeight = FontWeight(818),
                     color = Color.White,
                     textAlign = TextAlign.Center
                 )
             }
        }
    }
}