package com.eynnzerr.cpbookkeeping_compose.ui.basic

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.eynnzerr.cpbookkeeping_compose.R
import com.eynnzerr.cpbookkeeping_compose.model.Screen
import com.eynnzerr.cpbookkeeping_compose.ui.navigation.Destinations
import com.eynnzerr.cpbookkeeping_compose.ui.navigation.NavGraph
import com.eynnzerr.cpbookkeeping_compose.ui.navigation.navigateTo
import com.eynnzerr.cpbookkeeping_compose.ui.theme.Blue_Sky
import kotlin.math.roundToInt

private const val TAG = "BasicScreen"

@ExperimentalAnimationApi
@Composable
fun BasicScreen(
    navController: NavController
) {
    //items for BottomNavigation. Changes to the size of list is not recommended.
    val items = listOf(Screen.Home, Screen.Record, Screen.Setting)
    val listState = rememberLazyListState()
    val currentScreen = navController.currentScreen()
    Log.d(TAG, "BasicScreen: currentScreen is:${currentScreen.value}")
    Scaffold(
        topBar = {
            //Change according to currentScreen in composable.
            CPTopBar(currentScreen)
        },
        floatingActionButton = {
            //only show up when it's HOME screen.
            if(currentScreen.value == Destinations.HOME_ROUTE) {
                DraggableFloatingButton(
                    onClick = {
                        navController.navigateTo(Destinations.NEW_ROUTE)
                    }
                )
            } else Unit
        },
        bottomBar = {
            //only show up when it's HOME, RECORD, SETTING screens.
            AnimatedVisibility(listState.isScrollingUp()) {
                when(currentScreen.value) {
                    Destinations.HOME_ROUTE,
                    Destinations.RECORD_ROUTE,
                    Destinations.SETTING_ROUTE -> FlutterNavigation(navController = navController, items)
                    else -> Unit
                }
            }
        }
    ) {
        NavGraph(
            navController = navController as NavHostController,
            listState = listState
        )
    }
}

/**
 * Change topBar according to different screens.
 */
@Composable
private fun CPTopBar(currentScreen: State<String>) {
    when (currentScreen.value) {
        Destinations.HOME_ROUTE -> TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.More,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = { /*TODO open search*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                }
            }
        )
        Destinations.NEW_ROUTE -> TopAppBar(
            title = {
                Text(text = "test")
            },
            navigationIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )
        else -> Unit
    }
}

@Composable
private fun DraggableFloatingButton(
    onClick: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = Blue_Sky,
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consumeAllChanges()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun FlutterNavigation(navController: NavController, items: List<Screen>){
    //记录点击选择的索引
    val animalCenterIndex = remember { mutableStateOf(0) }
    val animalBoolean = remember { mutableStateOf(true) }
    val animalBooleanState: Float by animateFloatAsState(
        if (animalBoolean.value) {
            0f
        } else {
            1f
        }, animationSpec = TweenSpec(durationMillis = 600)
    )
    //点击选择的状态变化,下发到animateFloatAsState里面动画执行开始
    val indexValue: Float by animateFloatAsState(
        //动画的目标值。当animalCenterIndex.value触发向下时候动画执行开始
        when (animalCenterIndex.value) {
            0 -> {
                0f
            }
            1 -> {
                1f
            }
            else -> {
                2f
            }
        },
        //设置动画的格式
        animationSpec = TweenSpec(durationMillis = 500)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomEnd,
    ) {
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp), onDraw = {
                drawIntoCanvas { canvas ->
                    val paint = Paint()
                    paint.color = Color(0xFF00BFFF)
                    paint.style = PaintingStyle.Fill

                    val path = Path()
                    //先固定分为三等分
                    val widthOfOne = size.width / 3
                    //每一个弧度的中心控制点
                    val centerWidthOfOneX = widthOfOne / 2
                    //弧度端口到两遍ONewidth距离
                    val marginLeftAndRigth = centerWidthOfOneX / 1.6f

                    val controllerX = centerWidthOfOneX / 6f
                    //⭐️⭐️⭐️最重要的更新所有的坐标点就看这里
                    val keyAnimal = widthOfOne * indexValue
                    canvas.save()
                    canvas.drawCircle(Offset(centerWidthOfOneX + keyAnimal, 0f), 60f, paint)

                    path.moveTo(0f, 0f)
                    path.lineTo(marginLeftAndRigth / 2 + keyAnimal, 0f)
                    path.cubicTo(
                        marginLeftAndRigth + keyAnimal,
                        0f,
                        centerWidthOfOneX - (centerWidthOfOneX - controllerX) / 2f + keyAnimal,
                        size.height / 3f,
                        centerWidthOfOneX + keyAnimal,
                        size.height / 2.6f
                    )
                    path.cubicTo(
                        centerWidthOfOneX + (centerWidthOfOneX - controllerX) / 2f + keyAnimal,
                        size.height / 2.6f,
                        widthOfOne - (marginLeftAndRigth) + keyAnimal,
                        0f,
                        widthOfOne - marginLeftAndRigth / 2 + keyAnimal,
                        0f
                    )
                    path.lineTo(size.width, 0f)
                    path.lineTo(size.width, size.height)
                    path.lineTo(0f, size.height)
                    path.close()
                    canvas.clipPath(path)
                    canvas.nativeCanvas.drawColor(Color(0xFF00BFFF).toArgb())
                }
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEachIndexed { index, screen ->
                Icon(
                    tint = Color.White,
                    imageVector = when(index) {
                        0 -> Icons.Filled.Home
                        1 -> Icons.Filled.Menu
                        else -> Icons.Filled.Settings
                    },
                    contentDescription = screen.label,
                    modifier = Modifier
                        .modifier(animalCenterIndex, index, animalBooleanState)
                        .clickable {
                            animalBoolean.value = !animalBoolean.value
                            animalCenterIndex.value = index
                            navController.navigateTo(screen.route)
                        }
                )
            }
        }
    }
}

//将点击选择进行封装起来。单独处理返回按钮位置和是否旋转。
@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.modifier(
    animalCenterIndex: MutableState<Int>,
    i: Int,
    animalBooleanState: Float
): Modifier {
    return if (animalCenterIndex.value == i) {
        Modifier
            .padding(bottom = 57.dp)
            .width(25.dp)
            .height(25.dp)
            .rotate(animalBooleanState * 360)
    } else {
        Modifier
            .padding(top = 20.dp)
            .width(25.dp)
            .height(25.dp)
    }
}

//Add flag for screens to distinguish if it has unique topBar or bottomBar
@Composable
private fun NavController.currentScreen(): State<String> {
    val currentScreen = remember { mutableStateOf(Destinations.HOME_ROUTE)}
    DisposableEffect(key1 = this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Destinations.NEW_ROUTE } -> {
                    currentScreen.value = Destinations.NEW_ROUTE
                } else -> currentScreen.value = Destinations.HOME_ROUTE
            }
        }
        addOnDestinationChangedListener(listener)
        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    return currentScreen
}

