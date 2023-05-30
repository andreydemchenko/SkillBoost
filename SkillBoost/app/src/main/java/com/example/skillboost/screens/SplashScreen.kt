package com.example.skillboost.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skillboost.MainNavScreen
import com.example.skillboost.R
import com.example.skillboost.ui.theme.interFonts
import com.example.skillboost.ui.theme.juryFonts
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, isUserLoggedIn: Boolean) {
    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    val startDestination = if (isUserLoggedIn) MainNavScreen.Main else MainNavScreen.Login

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.7f,
            // tween Animation
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                }))
        delay(1000L)
        navController.popBackStack()
        navController.navigate(startDestination.route)
    }

    // Image
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background_image),
                contentScale = ContentScale.FillWidth
            )
    ) {
        Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
            Column {
                Spacer(modifier = Modifier.weight(1f))
                val titleText = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("SKILL")
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("BOOST")
                    }
                }
                Text(
                    text = titleText,
                    color = Color.White,
                    fontFamily = juryFonts,
                    fontWeight = FontWeight.Normal,
                    fontSize = 54.sp,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Column {
                Spacer(modifier = Modifier.weight(1f))
                val bottomText = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontSize = 38.sp)) {
                        append("AI")
                    }
                    withStyle(style = SpanStyle(color = Color.Black, fontSize = 18.sp)) {
                        append("technology")
                    }
                }
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier
                            .padding(end = 20.dp, bottom = 30.dp),
                        text = bottomText,
                        fontFamily = interFonts,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}