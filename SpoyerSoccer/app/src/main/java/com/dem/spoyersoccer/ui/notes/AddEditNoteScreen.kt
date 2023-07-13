package com.dem.spoyersoccer.ui.notes

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dem.spoyersoccer.MyApplication
import com.dem.spoyersoccer.R
import com.dem.spoyersoccer.models.Note
import com.dem.spoyersoccer.models.NoteEntity
import com.dem.spoyersoccer.ui.theme.SpoyerSoccerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(navController: NavController) {

    val title = remember { mutableStateOf("") }
    val text = remember { mutableStateOf("") }

    val noteDao = MyApplication.database.noteDao()
    val coroutineScope = rememberCoroutineScope()

    val data = remember {
        mutableStateOf(navController.previousBackStackEntry?.savedStateHandle?.get<Note>("editNote"))
    }

    LaunchedEffect(Unit) {
        val note = data.value
        if (note != null) {
            title.value = note.title
            text.value = note.text
        }
    }


    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(start = 40.dp, top = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = (if (data.value != null) "Edit note" else "Create note").uppercase(),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_bold)),
            fontWeight = FontWeight(818),
            fontStyle = FontStyle.Italic,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        TextField(
            value = title.value,
            onValueChange = { newText -> title.value = newText } ,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
            shape = RoundedCornerShape(7.dp),
            placeholder = {
                Text(
                    text = "Title".uppercase(),
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.inter_bold)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF536575),
                    textAlign = TextAlign.Center
                )
            }
        )
        TextField(
            modifier = Modifier.height(300.dp),
            value = text.value,
            onValueChange = { newText -> text.value = newText } ,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
            shape = RoundedCornerShape(7.dp),
            placeholder = {
                Text(
                    text = "Type your text here",
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFF536575),
                    textAlign = TextAlign.Center
                )
            }
        )

        Button(
            modifier = Modifier.padding(top = 100.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            shape = RoundedCornerShape(7.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFFDC306)),
            onClick = {
                val note = NoteEntity(
                    id = if (data.value != null) data.value!!.id else (1000..100000).random(),
                    title = title.value,
                    text = text.value
                )
                coroutineScope.launch(Dispatchers.IO) {
                    if (data.value != null) {
                        noteDao.update(note)
                    } else {
                        noteDao.insert(note)
                    }
                }
                navController.previousBackStackEntry?.savedStateHandle?.apply {
                    set("editNote", null)
                }
                navController.popBackStack()
            }) {
            Text(
                text = (if (data.value != null) "Edit note" else "Create a note").uppercase(),
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.inter_bold)),
                fontWeight = FontWeight(700),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddNoteScreenPreview() {
    SpoyerSoccerTheme {
        AddEditNoteScreen(rememberNavController())
    }
}