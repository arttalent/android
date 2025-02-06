package com.example.talenta.presentation.ui.onboarding


import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.talenta.R
import kotlinx.coroutines.launch
import kotlin.math.abs


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {

    val pages = remember {
        listOf(
            OnboardingPage(
                image = R.drawable.img,
                title = "Welcome to TalentA",
                description = "Discover amazing features tailored just for you",
                backgroundColor = Color(0xFFFFFFFF)
            ),
            OnboardingPage(
                image = R.drawable.img,
                title = "Welcome to TalentA",
                description = "Discover amazing features tailored just for you",
                backgroundColor = Color(0xFFFFFFFF)
            ),
            OnboardingPage(
                image = R.drawable.img,
                title = "Welcome to TalentA",
                description = "Discover amazing features tailored just for you",
                backgroundColor = Color(0xFFFFFFFF)
            )
        )
    }


    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pages.size }
    )
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            pageSpacing = 0.dp,
            pageContent = { page ->
                val pageOffset =
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                val scale = lerp(
                    start = 0.85f,
                    stop = 1f,
                    fraction = 1f - abs(pageOffset)
                )
                val alpha = lerp(
                    start = 0.5f,
                    stop = 1f,
                    fraction = 1f - abs(pageOffset)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        }
                        .animateContentSize()
                ) {
                    OnboardingPageContent(
                        page = pages[page],
                        offset = pageOffset
                    )
                }
            }
        )

        // Navigation controls with animated visibility
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Animated page indicators
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(pages.size) { iteration ->
                        val isSelected = pagerState.currentPage == iteration
                        val width by animateDpAsState(
                            targetValue = if (isSelected) 24.dp else 8.dp,
                            animationSpec = spring(
                                dampingRatio = 0.8f,
                                stiffness = Spring.StiffnessMediumLow
                            ),
                            label = "width"
                        )

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (isSelected)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                )
                                .width(width)
                                .height(8.dp)
                                .bounceClick()
                                .clickable {
                                    scope.launch {
                                        pagerState.animateScrollToPage(
                                            iteration,
                                            animationSpec = spring(
                                                dampingRatio = 0.8f,
                                                stiffness = Spring.StiffnessLow
                                            )
                                        )
                                    }
                                }
                        )
                    }
                }

                // Animated Next/Complete button
                val buttonScale by animateFloatAsState(
                    targetValue = if (pagerState.currentPage == pages.size - 1) 1.1f else 1f,
                    animationSpec = spring(
                        dampingRatio = 0.5f,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "buttonScale"
                )

                Button(
                    onClick = {
                        if (pagerState.currentPage == pages.size - 1) {
                            onComplete()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    pagerState.currentPage + 1,
                                    animationSpec = spring(
                                        dampingRatio = 0.8f,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.royal_blue)
                    ),
                    modifier = Modifier
                        .bounceClick()
                        .graphicsLayer {
                            scaleX = buttonScale
                            scaleY = buttonScale
                        }
                ) {
                    Text(
                        color = Color.White,
                        text = if (pagerState.currentPage == pages.size - 1) "Get Started" else "Next",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}


fun Modifier.bounceClick() = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        if (isPressed) 0.95f else 1f,
        label = "scale"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = { offset ->
                    isPressed = true
                    tryAwaitRelease()
                    isPressed = false
                }
            )
        }
}