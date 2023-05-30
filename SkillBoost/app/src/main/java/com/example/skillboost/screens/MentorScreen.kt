package com.example.skillboost.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillboost.R
import com.example.skillboost.models.ChatMessage
import com.example.skillboost.models.Sender
import com.example.skillboost.repositories.AuthRepository
import com.example.skillboost.ui.theme.MyTheme
import com.example.skillboost.ui.theme.interFonts
import com.example.skillboost.viewmodels.MainViewModel
import com.example.skillboost.viewmodels.MentorViewModel
import com.example.skillboost.viewmodels.ProfileViewModel
import java.util.Timer
import java.util.UUID
import kotlin.concurrent.timerTask

@Composable
fun MentorScreen(
    mainViewModel: MainViewModel,
    viewModel: MentorViewModel,
    profileViewModel: ProfileViewModel
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background_image),
                contentScale = ContentScale.FillWidth
            )
    ) {
        Column(modifier = Modifier.padding(bottom = 30.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(modifier = Modifier.padding(top = 10.dp)) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier.tutorialBlur(mainViewModel, mainViewModel.currentTutorialStep.value != 3),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.openHistoryScreen()
                    }
                ) {
                    Text(
                        text = "History",
                        fontFamily = interFonts,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp)
                }
            }
            Row {
                Text(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .tutorialBlur(mainViewModel, mainViewModel.currentTutorialStep.value != 2),
                    text = "Ask mentor",
                    fontFamily = interFonts,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            val chatMessages by viewModel.chatMessages.collectAsState()

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                MessageList(mainViewModel, Modifier.weight(1f), chatMessages)
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 2.dp)
                    .height(2.dp)
                    .background(Color.LightGray)
                    )
                Box(
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    val messageState = remember { mutableStateOf("") }
                    Box(modifier = Modifier.padding(end = 70.dp)) {
                        MyBasicTextField(message = messageState, focusRequester = focusRequester)
                    }
                    Button(
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.CenterEnd),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            focusManager.clearFocus()
                            if (messageState.value.isNotEmpty()) {
                                val chatMessage = ChatMessage(
                                    id = UUID.randomUUID().toString(),
                                    content = messageState.value,
                                    sender = Sender.Me
                                )
                                val chatMessage2 = ChatMessage(
                                    id = UUID.randomUUID().toString(),
                                    content = messageState.value,
                                    sender = Sender.Bot
                                )
                                viewModel.addChatMessage(chatMessage)
                                Timer().schedule(timerTask {
                                    viewModel.addChatMessage(chatMessage2)
                                }, 1000)

                                messageState.value = ""
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.scale(3f),
                            imageVector = Icons.Default.Send,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
        if (mainViewModel.hasNotSeenTutorial.value && !mainViewModel.isUserHaveAccess.value) {
            Column(verticalArrangement = Arrangement.Center) {
                Spacer(modifier = Modifier.weight(1f))
                Card(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .height(160.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier
                                .height(120.dp)
                                .padding(top = 30.dp)
                                .padding(8.dp),
                            textAlign = TextAlign.Center,
                            text = "Нужен доступ, чтобы \nвоспользоваться наставником",
                            fontFamily = interFonts,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RectangleShape,
                            onClick = {
                                profileViewModel.openUpgradeScreen()
                                mainViewModel.isUserHaveAccess.value = true
                            }
                        ) {
                            Text(
                                text = "Получить доступ",
                                fontFamily = interFonts,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun MessageList(mainViewModel: MainViewModel, modifier: Modifier = Modifier, messages: List<ChatMessage>) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val scrollState = rememberLazyListState()

        if (!mainViewModel.hasNotSeenTutorial.value) {
            LaunchedEffect(key1 = messages.last()) {
                scrollState.scrollToItem(messages.lastIndex)
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            state = scrollState
        ) {
            items(messages) { chatMessage ->
                if (mainViewModel.hasNotSeenTutorial.value) {
                    val condition =
                        mainViewModel.currentTutorialStep.value != 3 &&
                                messages.first().id != chatMessage.id &&
                                messages[1].id != chatMessage.id
                                ||
                                mainViewModel.currentTutorialStep.value != 2 &&
                                messages[2].id != chatMessage.id &&
                                messages[3].id != chatMessage.id
                    val modifier = Modifier.tutorialBlur(
                        mainViewModel,
                        condition
                    )
                    ChatMessageItem(chatMessage, modifier)
                } else {
                    ChatMessageItem(chatMessage)
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(chatMessage: ChatMessage, modifier: Modifier = Modifier) {
    val backgroundColor = if (chatMessage.sender == Sender.Me) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
    val contentAlignment = if (chatMessage.sender == Sender.Me) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 22.dp,
                bottomStart = 22.dp,
                bottomEnd = 0.dp
            ),
            modifier = modifier
                .align(contentAlignment)
                .widthIn(60.dp, 280.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Text(
                text = chatMessage.content,
                fontFamily = interFonts,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MentorPreview() {
    MyTheme {
        MentorScreen(
            mainViewModel = MainViewModel(AuthRepository()),
            viewModel = MentorViewModel(),
            profileViewModel = ProfileViewModel()
        )
    }
}
