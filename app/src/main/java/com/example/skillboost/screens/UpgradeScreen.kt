package com.example.skillboost.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillboost.R
import com.example.skillboost.models.Plan
import com.example.skillboost.ui.theme.MyTheme
import com.example.skillboost.ui.theme.interFonts
import com.example.skillboost.ui.theme.neueMachineFonts
import com.example.skillboost.viewmodels.ProfileViewModel

@Composable
fun UpgradeScreen(viewModel: ProfileViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background_image),
                contentScale = ContentScale.FillWidth
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Upgrade your membership",
                fontFamily = neueMachineFonts,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 36.sp,
                textAlign = TextAlign.Start,
                lineHeight = 38.sp
            )

            viewModel.plans.forEach { plan ->
                PlanCard(plan)
            }

            Text(
                text = "Finding your passion sooner dramatically influence where you will be in 15 years. It can cost you thousands of dollars every year, health and mental issues, or become a happy journey full of success and eagerness.",
                fontFamily = interFonts,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                color = Color.Black,
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
fun PlanCard(plan: Plan) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = plan.title,
                fontFamily = interFonts,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = plan.subtitle,
                fontFamily = interFonts,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Divider(color = Color.LightGray)

            Text(
                text = plan.price,
                fontFamily = interFonts,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
            )

            Button(
                onClick = { /* Handle click here */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Upgrade to ${plan.title.lowercase()}",
                    fontFamily = interFonts,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Text(
                text = "money back guarantee",
                fontFamily = interFonts,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )

            Divider(color = Color.LightGray)

            Text(
                text = AnnotatedString(plan.description),
                fontFamily = interFonts,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpgradePreview() {
    MyTheme {
        UpgradeScreen(viewModel = ProfileViewModel())
    }
}