package com.example.skillboost.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skillboost.R
import com.example.skillboost.repositories.AuthRepository
import com.example.skillboost.ui.theme.MyTheme
import com.example.skillboost.viewmodels.MainViewModel
import com.example.skillboost.viewmodels.MentorViewModel

@Composable
fun HistoryScreen(viewModel: MentorViewModel, mainViewModel: MainViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background_image),
                contentScale = ContentScale.FillWidth
            )
    ) {
        val chatMessages by viewModel.chatMessages.collectAsState()
        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = 20.dp)
        ) {
            MessageList(mainViewModel, Modifier.weight(1f), chatMessages)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryPreview() {
    MyTheme {
        HistoryScreen(viewModel = MentorViewModel(), mainViewModel = MainViewModel(AuthRepository()))
    }
}