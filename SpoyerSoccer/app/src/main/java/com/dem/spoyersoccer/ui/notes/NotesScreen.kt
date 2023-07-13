package com.dem.spoyersoccer.ui.notes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dem.spoyersoccer.MainNavigation
import com.dem.spoyersoccer.MyApplication
import com.dem.spoyersoccer.R
import com.dem.spoyersoccer.models.Note
import com.dem.spoyersoccer.models.NoteEntity
import com.dem.spoyersoccer.ui.theme.SpoyerSoccerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(navController: NavController) {

    val noteDao = MyApplication.database.noteDao()
    val notesState: MutableState<List<NoteEntity>> = remember { mutableStateOf(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        val notesObserver = Observer<List<NoteEntity>> { notes ->
            notesState.value = notes
        }

        val liveData = noteDao.getAllNotes()
        liveData.observeForever(notesObserver)

        onDispose {
            liveData.removeObserver(notesObserver)
        }
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(start = 40.dp, top = 20.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Notes".uppercase(),
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.inter_bold)),
                fontWeight = FontWeight(818),
                fontStyle = FontStyle.Italic,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            LazyColumn {
                itemsIndexed(notesState.value) { _, note ->
                    NoteItem(note, onDelete = { coroutineScope.launch(Dispatchers.IO) {
                        noteDao.delete(note)
                    } }, onEdit = {
                        val edNote = Note(id = note.id, title = note.title, text = note.text)
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set("editNote", edNote)
                        }
                        navController.navigate(MainNavigation.AddNote.route)
                    })
                }
            }
        }
        Column(
            modifier = Modifier.padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                shape = RoundedCornerShape(7.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFFDC306)),
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        set("editNote", null)
                    }
                    navController.navigate(MainNavigation.AddNote.route)
                }) {
                Text(
                    text = "Create a note".uppercase(),
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.inter_bold)),
                    fontWeight = FontWeight(700),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun NoteItem(note: NoteEntity, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = note.title.uppercase(),
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.inter_bold)),
                fontWeight = FontWeight(700),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = note.text,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    modifier = Modifier.size(28.dp),
                    onClick = onEdit,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.edit_icon),
                        contentDescription = "edit",
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    modifier = Modifier.size(28.dp),
                    onClick = onDelete,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.delete_icon),
                        contentDescription = "delete",
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    SpoyerSoccerTheme {
        NotesScreen(rememberNavController())
    }
}