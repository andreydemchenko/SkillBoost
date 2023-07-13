package com.dem.spoyersoccer.ui.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dem.spoyersoccer.R
import com.dem.spoyersoccer.ui.theme.SpoyerSoccerTheme

@Composable
fun ShopScreen() {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(start = 40.dp, top = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Text(
            text = "Shop".uppercase(),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_bold)),
            fontWeight = FontWeight(818),
            fontStyle = FontStyle.Italic,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .border(width = 5.dp, color = Color(0xFFC1860D), shape = RoundedCornerShape(size = 20.dp)),
        contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ad_icon),
                contentDescription = "ad",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopScreenPreview() {
    SpoyerSoccerTheme {
        ShopScreen()
    }
}