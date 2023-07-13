package com.dem.spoyersoccer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.dem.spoyersoccer.R

@Composable
fun MenuView(isExpanded: MutableState<Boolean>, navigation: MutableState<MenuNavigation?>) {
    val primaryGradient = listOf(Color(0xFFEE9F00), Color(0xFF2C2C2C))
    Box(
        modifier = Modifier
            .width(if (isExpanded.value) 100.dp else 50.dp)
            .fillMaxHeight()
            .padding(bottom = 100.dp)
            .background(Brush.verticalGradient(primaryGradient))
            .zIndex(2f)
            .noRippleClickable {
                isExpanded.value = !isExpanded.value
            },
        contentAlignment = Alignment.Center
    ) {
//        Image(
//            modifier = Modifier
//                .padding(bottom = 100.dp)
//                .fillMaxSize(),
//            contentScale = ContentScale.FillBounds,
//            painter = painterResource(id = R.drawable.orange_gradient),
//            contentDescription = null
//        )

        Column {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = "Menu".uppercase(),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.inter_bold)),
                fontWeight = FontWeight(818),
                fontStyle = FontStyle.Italic,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
            MenuItem(
                iconDrawable = R.drawable.teams_icon,
                text = "Teams",
                isExpanded = isExpanded,
                onTap = { navigation.value = MenuNavigation.TEAMS }
            )
            MenuItem(
                iconDrawable = R.drawable.games_icon,
                text = "Games",
                isExpanded = isExpanded,
                onTap = { navigation.value = MenuNavigation.GAMES }
            )
            MenuItem(
                iconDrawable = R.drawable.statistics_icon,
                text = "Statistics",
                isExpanded = isExpanded,
                onTap = { navigation.value = MenuNavigation.STATISTICS }
            )
            MenuItem(
                iconDrawable = R.drawable.factor_icon,
                text = "Factor",
                isExpanded = isExpanded,
                onTap = { navigation.value = MenuNavigation.FACTOR }
            )
            MenuItem(
                iconDrawable = R.drawable.notes_icon,
                text = "Notes",
                isExpanded = isExpanded,
                onTap = { navigation.value = MenuNavigation.NOTES }
            )
            MenuItem(
                iconDrawable = R.drawable.wheel_icon,
                text = "Wheel of fortune",
                isExpanded = isExpanded,
                onTap = { navigation.value = MenuNavigation.WHEEL }
            )
            MenuItem(
                iconDrawable = R.drawable.history_icon,
                text = "History of luck",
                isExpanded = isExpanded,
                onTap = { navigation.value = MenuNavigation.HISTORY }
            )
            MenuItem(
                iconDrawable = R.drawable.shop_icon,
                text = "Shop",
                isExpanded = isExpanded,
                onTap = { navigation.value = MenuNavigation.SHOP }
            )
            MenuItem(
                iconDrawable = R.drawable.calendar_icon,
                text = "Calendar",
                isExpanded = isExpanded,
                onTap = { navigation.value = MenuNavigation.CALENDAR }
            )
            Spacer(modifier = Modifier.weight(3f))
        }
    }
}

@Composable
fun MenuItem(
    iconDrawable: Int,
    text: String,
    isExpanded: MutableState<Boolean>,
    onTap: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 10.dp)
            .clickable {
                if (!isExpanded.value) {
                    isExpanded.value = true
                } else {
                    onTap()
                    isExpanded.value = false
                }
            },
        horizontalArrangement = Arrangement.Center
    ) {

        Image(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = iconDrawable),
            contentDescription = null
        )
        if (isExpanded.value) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .padding(start = 2.dp),
                text = text.uppercase(),
                fontSize = 9.sp,
                fontFamily = FontFamily(Font(R.font.inter_bold)),
                fontWeight = FontWeight(818),
                fontStyle = FontStyle.Italic,
                color = Color.White,
                lineHeight = 12.sp
            )
        }
    }
}

enum class MenuNavigation {
    TEAMS,
    GAMES,
    STATISTICS,
    FACTOR,
    NOTES,
    WHEEL,
    HISTORY,
    SHOP,
    CALENDAR
}
