package com.example.skillboost.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillboost.R
import com.example.skillboost.models.Language
import com.example.skillboost.repositories.AuthRepository
import com.example.skillboost.ui.theme.MyTheme
import com.example.skillboost.ui.theme.interFonts
import com.example.skillboost.ui.theme.juryFonts
import com.example.skillboost.viewmodels.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    var isChecked by remember { mutableStateOf(false) }

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
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(228.dp)
                .shadow(6.dp, ambientColor = Color.Black)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Select Language & Login".uppercase(),
                    textAlign = TextAlign.Center,
                    fontFamily = interFonts,
                    fontWeight = FontWeight.SemiBold
                )
                // Language selection
                LanguageSelection(viewModel = viewModel)

                // Google Sign In
                OutlinedButton(
                    onClick = viewModel::signInWithGoogle,
                    shape = RoundedCornerShape(30),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            modifier = Modifier.size(20.dp).padding(end = 4.dp, start = 0.dp),
                            painter = painterResource(id = R.drawable.google_icon),
                            contentDescription = null
                        )
                        Text(
                            text = "Sign in with Google",
                            color = Color.Black,
                            modifier = Modifier.padding(2.dp),
                            fontFamily = interFonts,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                TextWithLinks(
                    onTermsClicked = { /* Handle terms clicked */ },
                    onPrivacyPolicyClicked = { /* Handle privacy policy clicked */ }
                )

                Row(
                    modifier = Modifier.clickable { isChecked = !isChecked },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )
                    Text(
                        text = "Get notifications about way of upskilling",
                        color = Color.DarkGray,
                        fontFamily = interFonts,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                }
            }
        }
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            Spacer(modifier = Modifier.height(100.dp))
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
                fontSize = 54.sp
            )
        }
        val bottomText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontSize = 38.sp)) {
                append("AI")
            }
            withStyle(style = SpanStyle(color = Color.Black, fontSize = 18.sp)) {
                append("technology")
            }
        }

        Text(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 30.dp),
            text = bottomText,
            fontFamily = interFonts,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun LanguageSelection(viewModel: LoginViewModel) {
    val languages = listOf(
        Language(R.drawable.england_icon, "English", "GBP"),
        Language(R.drawable.russia_icon, "Русский", "RUB"),
        Language(R.drawable.spain_icon, "Español", "EUR"),
        Language(R.drawable.portugal_icon, "Portugués", "EUR")
    )

    val expanded = remember { mutableStateOf(false) }
    val selectedLanguage = viewModel.selectedLanguage.collectAsState()
    val boxWidth = 180.dp

    Column(modifier = Modifier
        .padding(horizontal = 30.dp)
        .width(boxWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .width(boxWidth)
        ) {
            Box(
                modifier = Modifier
                    .width(boxWidth)
                    .height(40.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable {
                        expanded.value = !expanded.value
                    }
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = selectedLanguage.value.name.uppercase(),
                    color = Color.White
                )
            }
            CustomDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                width = boxWidth
            ) {
                languages.forEach { language ->
                    DropdownMenuItem(
                        onClick = {
                            viewModel.selectLanguage(language)
                            expanded.value = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .padding(end = 2.dp),
                                    painter = painterResource(id = language.iconResId),
                                    contentDescription = "",
                                    contentScale = ContentScale.Inside
                                )
                                Text(
                                    text = language.name.uppercase()
                                )
                                Spacer(modifier = Modifier
                                    .widthIn(min = 16.dp)
                                    .weight(1f))
                                Text(text = language.currency)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CustomDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    width: Dp,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val surfaceColor = MaterialTheme.colorScheme.onPrimary
    val dropdownMenuBackgroundColor = MaterialTheme.colorScheme.surface
    val elevation = 8.dp

    val offsetX = 6.dp
    val offsetY = 40.dp

    Box(
        modifier = modifier
            .width(width)
            .shadow(elevation, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            offset = DpOffset(offsetX, offsetY),
            modifier = Modifier.background(dropdownMenuBackgroundColor)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = surfaceColor
            ) {
                Column {
                    content()
                }
            }
        }
    }
}

@Composable
fun TextWithLinks(onTermsClicked: () -> Unit, onPrivacyPolicyClicked: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        // Normal text
        withStyle(
            style = SpanStyle(
                color = Color.DarkGray,
                fontFamily = interFonts,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp
            )
        ) {
            append("How we process data: ")
        }

        // Terms link
        pushStringAnnotation(
            tag = "Terms",
            annotation = "https://example.com/terms"
        )
        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Terms")
        }
        pop()

        append(" and ")

        // Privacy Policy link
        pushStringAnnotation(
            tag = "PrivacyPolicy",
            annotation = "https://example.com/privacy_policy"
        )
        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Privacy Policy")
        }
        pop()
    }

    Text(
        text = annotatedString,
        modifier = Modifier.clickable { },
        color = Color.DarkGray,
        fontFamily = interFonts,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    MyTheme {
        LoginScreen(viewModel = LoginViewModel(AuthRepository()))
    }
}