package com.dem.spoyersoccer.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dem.spoyersoccer.R
import com.dem.spoyersoccer.ui.statistics.CourtView
import com.dem.spoyersoccer.ui.theme.SpoyerSoccerTheme
import java.time.DayOfWeek
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen() {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(start = 40.dp, top = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Text(
            text = "Calendar".uppercase(),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_bold)),
            fontWeight = FontWeight(818),
            fontStyle = FontStyle.Italic,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        val selectedDates = List(40) {
            LocalDate.now().plusDays((1..20).random().toLong() * it)
        }
        CustomCalendarView(selectedDates = selectedDates)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomCalendarView(selectedDates: List<LocalDate>) {
    val currentMonth = remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth.value = currentMonth.value.minusMonths(1) }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous month", tint = Color.White)
            }
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = currentMonth.value.month.name,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.inter_bold)),
                fontWeight = FontWeight(800),
                fontStyle = FontStyle.Italic,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { currentMonth.value = currentMonth.value.plusMonths(1) }) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next month", tint = Color.White)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            DayOfWeek.values().forEach {
                Text(
                    modifier = Modifier.weight(1f),
                    text = it.name.take(1),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.inter_bold)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFADADAD),
                    textAlign = TextAlign.Center
                )
            }
        }

        val daysInMonth = currentMonth.value.month.length(currentMonth.value.isLeapYear)
        val firstDayOfWeek = currentMonth.value.withDayOfMonth(1).dayOfWeek.ordinal

        for (i in 0 until ((daysInMonth + firstDayOfWeek + 6) / 7) * 7) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (j in 0 until 7) {
                    val day = i * 7 + j - firstDayOfWeek + 1

                    if (day in 1..daysInMonth) {
                        val date = currentMonth.value.withDayOfMonth(day)

                        if (selectedDates.contains(date)) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(Color(0XFFDE9603), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                                    fontWeight = FontWeight(500),
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            Box(modifier = Modifier.size(30.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = day.toString(),
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                                    fontWeight = FontWeight(500),
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        Box(modifier = Modifier.size(30.dp))
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CalendarViewPreview() {
    SpoyerSoccerTheme {
        val selectedDates = List(20) {
            LocalDate.now().plusDays((1..15).random().toLong() * it)
        }
        CustomCalendarView(selectedDates = selectedDates)
    }
}
