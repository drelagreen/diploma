package ru.sfedu.zhalnin.oborona.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun BeautifulLoading(
    modifier: Modifier = Modifier,
    loading: Boolean = true,
    isError: Boolean = true,
    surfaceHeight: @Composable () -> Dp,
    backContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val transition = rememberInfiniteTransition()

    val height2 by transition.animateFloat(
        initialValue = 64.dp.value,
        targetValue = 84.dp.value,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        )
    )
    val t = updateTransition(targetState = loading, label = "test")

    val contentHeight by t.animateFloat(
        label = "sas",
    ) {
        if (it) {
            height2
        } else {
            surfaceHeight().value
        }
    }

    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (loadingSurfaceRef, contentRef, backContentRef, loadingScreenRef, noConnectionSnackbarRef) = createRefs()

        if (loading) {
            AndroidView(
                modifier = Modifier.constrainAs(
                    loadingScreenRef
                ) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                },
                factory = { context ->
                    LayoutInflater.from(context).inflate(R.layout.fragment_startscreen, null, false)
                }
            )
        }

        AnimatedVisibility(
            modifier = Modifier.constrainAs(backContentRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            },
            visible = !loading,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        ) {
            backContent()
        }

        Surface(
            modifier = Modifier
                .constrainAs(loadingSurfaceRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            color = AppTheme.colors.transparent
        ) {
            Column(verticalArrangement = Arrangement.Bottom) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(contentHeight.dp),
                    shape = AppTheme.shapes.backgroundShape
                ) {
                    Column(
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (loading) {
                            DotsTyping()
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier.constrainAs(contentRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)

                height = Dimension.fillToConstraints
            },
            visible = !loading,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        ) {
            content()
        }

        AnimatedVisibility(
            modifier = Modifier.constrainAs(noConnectionSnackbarRef) {
              bottom.linkTo(parent.bottom, 80.dp)
              start.linkTo(parent.start)
              end.linkTo(parent.end)
            },
            visible = isError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Snackbar(
                modifier = Modifier,
                elevation = 0.dp,
                shape = RoundedCornerShape(0.dp),
                action = null
            ) {
                Text(modifier = Modifier.fillMaxWidth(), text = "Отсутсвует подключение к серверу", textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun DotsTyping() {
    val maxOffset = 10f
    val dotSize = 12.dp // made it bigger for demo
    val delayUnit = 300 // you can change delay to change animation speed

    @Composable
    fun Dot(
        offset: Float
    ) = Spacer(
        Modifier
            .size(dotSize)
            .offset(y = -offset.dp)
            .background(
                color = AppTheme.colors.primary,
                shape = CircleShape
            )
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateOffsetWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                0f at delay with LinearEasing
                maxOffset at delay + delayUnit with LinearEasing
                0f at delay + delayUnit * 2
            }
        )
    )

    val offset1 by animateOffsetWithDelay(0)
    val offset2 by animateOffsetWithDelay(delayUnit)
    val offset3 by animateOffsetWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = maxOffset.dp)
    ) {
        val spaceSize = 16.dp

        Dot(offset1)
        Spacer(Modifier.width(spaceSize))
        Dot(offset2)
        Spacer(Modifier.width(spaceSize))
        Dot(offset3)
    }
}
