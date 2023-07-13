package com.dem.spoyersoccer.ui.games

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.dem.spoyersoccer.R
import com.dem.spoyersoccer.models.PastMatch
import com.dem.spoyersoccer.models.UpcomingMatch
import com.dem.spoyersoccer.ui.theme.SpoyerSoccerTheme
import com.dem.spoyersoccer.utils.AppContext
import java.text.SimpleDateFormat

@Composable
fun GamesScreen() {

    val isPast = remember { mutableStateOf(true) }
    val pastMatchesState = remember { mutableStateOf(emptyList<PastMatch>()) }
    val upcomingMatchesState = remember { mutableStateOf(emptyList<UpcomingMatch>()) }
    val manager = AppContext.getInstance().networkManager

    LaunchedEffect(Unit) {
        val pastMatches = manager.getFinishedMatches()
        pastMatchesState.value = pastMatches
        val upMatches = manager.getUpcomingMatches()
        upcomingMatchesState.value = upMatches
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(start = 40.dp)
            .fillMaxSize(),
            //.background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 5.dp)
                .padding(horizontal = 10.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                onClick = { isPast.value = true }
            ) {
                Text(
                    color = if (isPast.value) Color.White else Color.Black,
                    text = "Past",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.inter_bold)),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                onClick = { isPast.value = false }
            ) {
                Text(
                    color = if (isPast.value) Color.Black else Color.White,
                    text = "Upcoming",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.inter_bold)),
                )
            }
        }
        Text(
            text = "Games".uppercase(),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_bold)),
            fontWeight = FontWeight(818),
            fontStyle = FontStyle.Italic,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        if (isPast.value) {
            LazyColumn {
                itemsIndexed(pastMatchesState.value) { _, match ->
                    PastMatchItem(match)
                }
            }
        } else {
            LazyColumn {
                itemsIndexed(upcomingMatchesState.value) { _, match ->
                    UpcomingMatchItem(match)
                }
            }
        }
    }
}

@Composable
fun PastMatchItem(pastMatch: PastMatch) {
    val dateFormatted = SimpleDateFormat("dd.MM.yyyy").format(pastMatch.time)
    val isHomeImageError = remember { mutableStateOf(false) }
    val isAwayImageError = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(start = 5.dp)
            .fillMaxWidth()
            .height(180.dp)
            .padding(vertical = 10.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.5f)),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.width(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                rememberAsyncImagePainter(pastMatch.home.imageUrl, onError = { isHomeImageError.value = true }),
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
                modifier = Modifier.padding(16.dp),
                text = pastMatch.home.name.uppercase(),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontWeight = FontWeight(818),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
               // modifier = Modifier.padding(16.dp),
                text = dateFormatted,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(6.dp),
                text = pastMatch.score,
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
                rememberAsyncImagePainter(pastMatch.away.imageUrl, onError = { isAwayImageError.value = true }),
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
                modifier = Modifier.padding(6.dp),
                text = pastMatch.away.name.uppercase(),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontWeight = FontWeight(818),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun UpcomingMatchItem(match: UpcomingMatch) {
    val dateFormatted = SimpleDateFormat("dd.MM.yyyy").format(match.time)
    val isHomeImageError = remember { mutableStateOf(false) }
    val isAwayImageError = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(start = 5.dp)
            .fillMaxWidth()
            .height(180.dp)
            .padding(vertical = 10.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.5f)),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.width(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                rememberAsyncImagePainter(match.home.imageUrl, onError = { isHomeImageError.value = true }),
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
                modifier = Modifier.padding(16.dp),
                text = match.home.name.uppercase(),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontWeight = FontWeight(818),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
        Text(
            // modifier = Modifier.padding(16.dp),
            text = dateFormatted,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier.width(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                rememberAsyncImagePainter(match.away.imageUrl, onError = { isAwayImageError.value = true }),
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
                modifier = Modifier.padding(6.dp),
                text = match.away.name.uppercase(),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontWeight = FontWeight(818),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamesScreenPreview() {
    SpoyerSoccerTheme {
        GamesScreen()
    }
}