package com.example.skillboost.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.skillboost.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillboost.models.CourseCard
import com.example.skillboost.repositories.AuthRepository
import com.example.skillboost.ui.theme.MyTheme
import com.example.skillboost.ui.theme.interFonts
import com.example.skillboost.viewmodels.CoursesViewModel
import com.example.skillboost.viewmodels.MainViewModel
import kotlin.math.roundToInt

@Composable
fun CustomTabRows(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabClick: (Int) -> Unit
) {
    val transition = updateTransition(targetState = selectedTabIndex, label = "Tab transition")
    val indicatorWidth by transition.animateDp(
        transitionSpec = { tween(durationMillis = 250) }, label = "Tab width transition"
    ) { index ->
        (tabs[index].length * 10).dp
    }
    val indicatorOffset by transition.animateDp(
        transitionSpec = { tween(durationMillis = 250) }, label = "Tab offset transition"
    ) { index ->
        (index * 100).dp
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp)
        ) {
            tabs.forEachIndexed { index, tab ->
                BoxWithConstraints(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable { onTabClick(index) }
                ) {
                    val tabWidth = this.maxWidth

                    Text(
                        modifier = Modifier.padding(bottom = 2.dp),
                        text = tab,
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = if (index == selectedTabIndex) FontWeight.SemiBold else FontWeight.Normal,
                    )

                    if (index == selectedTabIndex) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .width(indicatorWidth)
                                .height(2.dp)
                                .background(Color.Black)
                                .offset(x = (tabWidth - indicatorWidth) / 2)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CoursesScreen(viewModel: CoursesViewModel, mainViewModel: MainViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background_image),
                contentScale = ContentScale.FillWidth
            )
            //.tutorialBlur(mainViewModel, true)
    ) {
        var selectedTabIndex by remember { mutableStateOf(0) }

        Column {
            val tabItems = listOf("My Courses", "Popular Courses")
             if (mainViewModel.hasNotSeenTutorial.value && mainViewModel.currentTutorialStep.value == 1) {
                 selectedTabIndex = 1
            }
            CustomTabRows(
                tabs = tabItems,
                selectedTabIndex = selectedTabIndex
            ) { tabIndex ->
                selectedTabIndex = tabIndex
            }
            Crossfade(
                targetState = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> MyCoursesView(viewModel, mainViewModel)
                    1 -> PopularCoursesView(viewModel, mainViewModel)
                }
            }
        }
    }
}

fun Modifier.tutorialBlur(viewModel: MainViewModel, condition: Boolean): Modifier = composed {
    val blurValue = if (viewModel.hasNotSeenTutorial.value && condition) 5.dp else 0.dp
    val blurValue2 = if (viewModel.hasNotSeenTutorial.value && condition) 20f else 0f
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
        // Code to handle versions lower than 12
        this.blurOverlay(blurValue2)
    } else {
        // Code to handle versions 12 and above
        this.blur(blurValue)
    }
}

@Composable
fun Modifier.blurOverlay(radius: Float): Modifier {
    val blurColor = Color.White.copy(alpha = radius / 25f)  // Normalize alpha to [0, 1]
    return this.background(blurColor)
}

@Composable
fun Modifier.blurBackground(
    bitmap: Bitmap,
    blurRadius: Float,
    context: Context
): Modifier {
    val blurredBitmap = remember(bitmap) { bitmap.blur(context, blurRadius).asImageBitmap() }
    val blurModifier = this.then(
        drawWithContent {
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawBitmap(blurredBitmap.asAndroidBitmap(), Offset.Zero.x, Offset.Zero.y, Paint())
            }
            drawContent()
        }
    )
    return blurModifier
}

fun Bitmap.blur(context: Context, radius: Float): Bitmap {
    val bitmapScaled = Bitmap.createScaledBitmap(
        this,
        (width * 0.5f).roundToInt(),
        (height * 0.5f).roundToInt(),
        false
    )

    val bitmapOutput = Bitmap.createBitmap(
        bitmapScaled.width,
        bitmapScaled.height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmapOutput)
    val paint = Paint()
    paint.isAntiAlias = true
    paint.isFilterBitmap = true
    paint.maskFilter = BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)

    canvas.drawBitmap(bitmapScaled, 0f, 0f, paint)

    return bitmapOutput
}

@Composable
fun MyCoursesView(viewModel: CoursesViewModel, mainViewModel: MainViewModel) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "AI course",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    fontFamily = interFonts,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Write what you need to learn, and AI explains how to achieve this career goal with basic concepts, practical examples, and feedback. Also, it finds pictures for each tutorial *in paid plans.",
                    fontFamily = interFonts,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                    lineHeight = 18.sp
                )
                val messageState = remember { mutableStateOf("") }
                MyBasicTextField(messageState, focusRequester = focusRequester)
                Button(
                    shape = RoundedCornerShape(12.dp),
                    onClick = {
                        focusManager.clearFocus()
                        messageState.value = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Text(
                        "Create your first course for free",
                        fontSize = 16.sp,
                        fontFamily = interFonts,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        val cards = viewModel.myCoursesCards.collectAsState(initial = emptyList()).value
        if (cards.isEmpty()) {
            Column(modifier = Modifier.clickable { focusManager.clearFocus() }) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.weight(1f),
                    text = "You don't have your courses yet",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        } else {
            val selectedCardId = viewModel.selectedCardId.collectAsState(initial = null).value

            if (selectedCardId != null) {

            }
            CoursersCardList(
                cards = cards,
                mainViewModel = mainViewModel,
                onCardClicked = { cardId ->
                    focusManager.clearFocus()
                    viewModel.onCardClicked(cardId)
                })
        }
    }
}

@Composable
fun CoursersCardList(cards: List<CourseCard>, mainViewModel: MainViewModel, onCardClicked: (Int) -> Unit) {
    Column {
        cards.forEach { card ->
            CourseCard(
                card = card,
                modifier = Modifier.tutorialBlur(mainViewModel, cards.first().id != card.id),
                onCardClicked = { cardId ->
                   onCardClicked(cardId)
            })
        }
    }
}

@Composable
fun CourseCard(card: CourseCard, modifier: Modifier = Modifier, onCardClicked: (Int) -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onCardClicked(card.id) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 14.dp)
                        .padding(bottom = 4.dp)
                        .padding(horizontal = 14.dp),
                    text = card.title,
                    fontSize = 18.sp,
                    fontFamily = interFonts,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 10.dp)
                        .padding(horizontal = 10.dp),
                    text = card.category,
                    fontSize = 12.sp,
                    fontFamily = interFonts,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
            }
            Canvas(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(40.dp)
            ) {
                val path = Path().apply {
                    moveTo(0f, size.height)
                    lineTo(size.width, size.height)
                    lineTo(size.width, 0f)
                    close()
                }

                drawPath(
                    path = path,
                    color = Color(0xFF009F77),
                    style = Fill
                )
            }
        }
    }
}

@Composable
fun MyBasicTextField(
    message: MutableState<String>,
    placeholder: String = "I want to learn how to play guitar...",
    focusRequester: FocusRequester
) {
    BasicTextField(
        value = message.value,
        onValueChange = { newText ->
            message.value = newText
        },
        textStyle = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(30.dp, 100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(red = 246, green = 247, blue = 247))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .focusRequester(focusRequester),
            ) {
                if (message.value.isEmpty()) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = placeholder,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.LightGray
                    )
                }
                innerTextField()
            }
        }
    )
}

@Composable
fun PopularCoursesView(viewModel: CoursesViewModel, mainViewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val cards = viewModel.popularCoursesCards.collectAsState(initial = emptyList()).value
        val selectedCardId = viewModel.selectedCardId.collectAsState(initial = null).value

        if (selectedCardId != null) {

        }
        CoursersCardList(
            cards = cards,
            mainViewModel = mainViewModel,
            onCardClicked = { cardId ->
            viewModel.onCardClicked(cardId)
        })
    }
}

@Preview(showBackground = true)
@Composable
fun CoursersPreview() {
    MyTheme {
        CoursesScreen(mainViewModel = MainViewModel(AuthRepository()), viewModel = CoursesViewModel())
    }
}