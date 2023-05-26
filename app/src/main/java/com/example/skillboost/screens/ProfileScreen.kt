package com.example.skillboost.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillboost.R
import com.example.skillboost.models.User
import com.example.skillboost.repositories.AuthRepository
import com.example.skillboost.ui.theme.MyTheme
import com.example.skillboost.ui.theme.interFonts
import com.example.skillboost.viewmodels.LoginViewModel
import com.example.skillboost.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(loginViewModel: LoginViewModel, viewModel: ProfileViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background_image),
                contentScale = ContentScale.FillWidth
            )
    ) {
        val user = viewModel.user.collectAsState(
            initial = User(
                id = "id123",
                email = "ivan33school@gmail.ru",
                name = "Valera Pen",
                photoUrl = ""
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(214.dp)
                    .background(MaterialTheme.colorScheme.onPrimary)
            ) {
                user.value?.let {
                    it
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .padding(horizontal = 10.dp)
                            .height(130.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(red = 81, green = 44, blue = 187))
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(102.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    painter = painterResource(id = R.drawable.mock_profile_image),
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop
                                )
                            }

                        }
                        Column {
                            Text(
                                text = it.name,
                                fontSize = 24.sp,
                                fontFamily = interFonts,
                                fontWeight = FontWeight.SemiBold)
                            Text(
                                text = it.email,
                                color = Color.DarkGray,
                                fontFamily = interFonts,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                            Image(
                                modifier = Modifier
                                    .padding(top = 18.dp)
                                    .size(30.dp, 30.dp)
                                    .clickable { },
                                painter = painterResource(id = R.drawable.settings_icon),
                                contentDescription = ""
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clickable { },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Служба поддержкки",
                        color = Color.DarkGray,
                        fontFamily = interFonts,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        modifier = Modifier.size(30.dp, 40.dp),
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clickable { },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Политика конфинденциальности",
                        color = Color.DarkGray,
                        fontFamily = interFonts,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        modifier = Modifier.size(30.dp, 40.dp),
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
            }

            Button(
                modifier = Modifier.padding(vertical = 12.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = { /*TODO*/ }) {
                Text(
                    text = "Add your youtube account",
                    fontSize = 18.sp,
                    fontFamily = interFonts,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = "Пройденные курсы:",
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = interFonts,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 8.dp)
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val cards =
                        viewModel.completedCoursesCards.collectAsState(initial = emptyList()).value
                    if (cards.isEmpty()) {
                        Column {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                modifier = Modifier.weight(1f),
                                text = "You haven't completed any courses yet",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    } else {
                        val selectedCardId =
                            viewModel.selectedCardId.collectAsState(initial = null).value

                        if (selectedCardId != null) {

                        }
                        CoursersCardList(cards = cards, onCardClicked = { cardId ->
                            viewModel.onCardClicked(cardId)
                        })
                    }
                }
                Column {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 30.dp),
                        shape = RoundedCornerShape(12.dp),
                        onClick = { /*TODO*/ }) {
                        Text(
                            text = "Upgrade plan",
                            fontSize = 18.sp,
                            fontFamily = interFonts,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
//            Button(
//                onClick =
//                loginViewModel::signOut
//            ) {
//                Text(text = "Log out")
//            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    MyTheme {
       ProfileScreen(loginViewModel = LoginViewModel(AuthRepository()), viewModel = ProfileViewModel())
    }
}