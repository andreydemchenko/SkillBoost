package com.dem.spoyersoccer.ui.leagues

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dem.spoyersoccer.R
import com.dem.spoyersoccer.ui.components.noRippleClickable
import com.dem.spoyersoccer.models.League
import com.dem.spoyersoccer.ui.theme.SpoyerSoccerTheme
import com.dem.spoyersoccer.utils.AppContext

@Composable
fun LeaguesScreen(selectedLeague: MutableState<League?>) {

    val leaguesState = remember { mutableStateOf(emptyList<League>()) }
    val manager = AppContext.getInstance().networkManager

    LaunchedEffect(Unit) {
        val leagues = manager.getLeagues()
        leaguesState.value = leagues
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 60.dp)
            .padding(start = 40.dp, end = 10.dp)
            .fillMaxSize()
            .noRippleClickable { selectedLeague.value = null },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "League".uppercase(),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_bold)),
            fontWeight = FontWeight(818),
            fontStyle = FontStyle.Italic,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        LazyColumn {
            itemsIndexed(leaguesState.value) { _, league ->
                LeagueItem(league.name) {
                    selectedLeague.value = league
                }
            }
        }
    }
}

@Composable
fun LeagueItem(name: String, onTap: () -> Unit) {
    Button(colors = ButtonDefaults.buttonColors(Color.Transparent), onClick = onTap) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = name,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontWeight = FontWeight(818),
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}
