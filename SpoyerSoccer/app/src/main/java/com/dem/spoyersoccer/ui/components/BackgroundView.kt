package com.dem.spoyersoccer.ui.components

import android.annotation.SuppressLint
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.dem.spoyersoccer.R
import com.dem.spoyersoccer.ui.theme.SpoyerSoccerTheme

@Composable
fun BackgroundView(
    isShowOrange: MutableState<Boolean>,
    isExpanded: MutableState<Boolean>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .noRippleClickable {
                isExpanded.value = false
            }
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.dark_background),
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
        if (isShowOrange.value) {
            Image(
                modifier = Modifier
                    .padding(top = 60.dp, start = 45.dp)
                    .padding(horizontal = 15.dp),
                painter = painterResource(id = R.drawable.orange_gradient),
                contentDescription = null
            )
        }
        Row(modifier = Modifier.padding(top = 80.dp)) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier
                    .padding(end = if (isShowOrange.value) 16.dp else 0.dp)
                    .size(200.dp),
                painter = painterResource(id = R.drawable.ball_top),
                contentDescription = null
            )
        }
        Column {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier
                    .padding(start = 50.dp)
                    .size(200.dp),
                painter = painterResource(id = R.drawable.ball_bottom),
                contentDescription = null
            )
        }
    }
}

