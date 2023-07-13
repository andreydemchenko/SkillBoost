package com.dem.spoyersoccer.ui.teams

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.dem.spoyersoccer.R
import com.dem.spoyersoccer.models.League
import com.dem.spoyersoccer.ui.components.noRippleClickable
import com.dem.spoyersoccer.ui.leagues.LeagueItem
import com.dem.spoyersoccer.models.Team
import com.dem.spoyersoccer.utils.AppContext
import kotlinx.coroutines.runBlocking

@Composable
fun TeamsScreen(league: League) {

    val teamsState = remember { mutableStateOf(emptyList<Team>()) }
    val manager = AppContext.getInstance().networkManager

    LaunchedEffect(Unit) {
        val teams = manager.getTeams(league.id)
        teamsState.value = teams
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .fillMaxSize()
            .padding(vertical = 60.dp)
            .zIndex(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Teams".uppercase(),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_bold)),
            fontWeight = FontWeight(818),
            fontStyle = FontStyle.Italic,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        LazyColumn {
            itemsIndexed(teamsState.value) { _, team ->
                TeamItem(team)
            }
        }
    }
}

@Composable
fun TeamItem(team: Team) {
    Log.d("image === ", team.imageUrl)
    rememberAsyncImagePainter(R.drawable.team_icon)
    val isImageError = remember { mutableStateOf(false) }
   Row(
       verticalAlignment = Alignment.CenterVertically
   ) {
       Image(
           rememberAsyncImagePainter(team.imageUrl, onError = { isImageError.value = true }),
           contentDescription = "Team Image",
           contentScale = ContentScale.Fit,
           modifier = Modifier.size(30.dp)
       )
       if (isImageError.value) {
           Image(
               rememberAsyncImagePainter(R.drawable.team_icon),
               contentDescription = "Team Image",
               contentScale = ContentScale.Fit,
               modifier = Modifier.size(30.dp)
           )
       }
       Text(
           modifier = Modifier.align(CenterVertically).padding(16.dp),
           text = team.name,
           fontSize = 16.sp,
           fontFamily = FontFamily(Font(R.font.inter_regular)),
           fontWeight = FontWeight(818),
           color = Color.White,
           textAlign = TextAlign.Center
       )
   }
}