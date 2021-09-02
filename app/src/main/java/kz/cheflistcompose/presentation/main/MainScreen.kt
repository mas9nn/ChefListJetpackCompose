package kz.cheflistcompose.presentation.main

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import kz.cheflistcompose.R
import kz.cheflistcompose.data.funcitonsModel.Function
import kz.cheflistcompose.presentation.MainActivityViewModel
import kz.cheflistcompose.presentation.toolbar.CollapsingToolbarScaffold
import kz.cheflistcompose.presentation.toolbar.CollapsingToolbarState
import kz.cheflistcompose.presentation.toolbar.ScrollStrategy
import kz.cheflistcompose.presentation.toolbar.rememberCollapsingToolbarScaffoldState
import kz.cheflistcompose.presentation.util.Screen
import timber.log.Timber

var open = mutableStateOf(false)

@ExperimentalPagerApi
var pageController = PagerState(pageCount = 2)

@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@Composable
fun MainScreen(
    navController: NavController,
    pagerState: PagerState,
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    pageController = pagerState
    HorizontalPager(state = pagerState, dragEnabled = false) { page ->
        when (page) {
            0 -> content(navController, viewModel)
            1 -> content(navController, viewModel)
        }
    }
}

@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun content(navController: NavController, viewModel: MainActivityViewModel = hiltViewModel()) {
    val state = rememberCollapsingToolbarScaffoldState(CollapsingToolbarState())
    val swipeRefreshState = rememberSwipeRefreshState(false)

    SwipeRefresh(state = swipeRefreshState, onRefresh = { /*TODO*/ }) {
        CollapsingToolbarScaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xffFAFAFA))
                .padding(top = 10.dp),
            state = state,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {
                val textSize = (18 + (30 - 18) * state.toolbarState.progress).sp
                val size = (100 - (36 * (1f - state.toolbarState.progress))).dp
                open.value = false
                //Timber.wtf("${state.toolbarState.progress} ${state.toolbarState.minHeight}")
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .height(190.dp)
                        .pin()
                )
                Column() {
                    OptionButtons()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        IconButton(
                            modifier = Modifier
                                .width(size)
                                .height(size),
                            onClick = {
                                navController.navigate(Screen.BottomSheet.route)
                            }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_background),
                                contentDescription = "Button Search",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.clip(shape = RoundedCornerShape(percent = 50))
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Hello, Koldur Shaldungaliev",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }

                }
            }
        ) {
            Box {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 130.dp, bottom = 70.dp)
                ) {
                    item {
                        Subscriptions(viewModel)
                    }
                }
                FunctionsList(navController)
            }
        }
    }
}

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun Subscriptions(viewModel: MainActivityViewModel = hiltViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(text = "Subscriptions", style = TextStyle(color = Color.Black, fontSize = 16.sp))
            Text(
                text = "View All",
                style = TextStyle(color = Color(0xffFA9C32), fontSize = 16.sp),
                modifier = Modifier.align(Alignment.Bottom)
            )
        }
        LazyRow(contentPadding = PaddingValues(16.dp)) {
            item {
                Card(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(Color.White)
                        .width(300.dp),
                    elevation = 0.4.dp,
                    onClick = {
                        viewModel.currentPage.value = 1
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                top = 16.dp,
                                bottom = 16.dp
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = "Button Search",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(11.dp))
                                .size(80.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .height(80.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Cooking Master",
                                    style = TextStyle(color = Color.Black, fontSize = 15.sp),
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Cooking Master",
                                    style = TextStyle(color = Color.Black, fontSize = 15.sp),
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Cooking Master",
                                    style = TextStyle(color = Color.Black, fontSize = 15.sp),
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun FunctionsList(navController: NavController) {
    val list = listOf(
        Function(1, "Orders", R.drawable.order_create_1),
        Function(1, "Orders", R.drawable.order_create_1),
        Function(1, "Orders", R.drawable.order_create_1),
        Function(1, "Orders", R.drawable.order_create_1),
        Function(1, "Orders", R.drawable.order_create_1),
        Function(1, "Orders", R.drawable.order_create_1)
    )

    Column(
        modifier = Modifier
            .clip(
                shape = RoundedCornerShape(
                    bottomEnd = 12.dp,
                    bottomStart = 12.dp
                )
            )
            .background(Color.White)
    ) {

        if (open.value) {
            LazyVerticalGrid(
                cells = GridCells.Fixed(3),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
            ) {
                items(list, itemContent = { item ->
                    FunctionItem(item, navController)
                })
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
            ) {
                items(list, itemContent = { item ->
                    FunctionItem(item, navController)
                })
            }
        }
        Box(
            modifier = Modifier
                .padding(10.dp)
                .height(10.dp)
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    open.value = !open.value
                }, modifier = Modifier
                    .align(Alignment.Center)
                    .height(10.dp)
                    .width(40.dp)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.ic_drag),
                    contentDescription = "Drag",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun FunctionItem(item: Function, navController: NavController) {
    Column() {
        IconButton(
            onClick = {

            },
            modifier = Modifier
                .size(68.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = item.logo),
                contentDescription = item.title,
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = item.title,
            style = TextStyle(
                color = Color.Black,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun OptionButtons(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, end = 16.dp)
    ) {
        IconButton(
            onClick = {}) {
            Image(
                painter = painterResource(id = R.drawable.button_search),
                contentDescription = "Button Search",
                contentScale = ContentScale.Crop
            )
        }
        //Spacer(modifier = Modifier.width(16.dp))
        IconButton(onClick = {}) {
            Image(
                painter = painterResource(id = R.drawable.button_no_notification),
                contentDescription = "Button Search",
                contentScale = ContentScale.Crop,
            )
        }
    }
}