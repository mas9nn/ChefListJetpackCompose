package kz.cheflistcompose.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kz.cheflistcompose.presentation.main.pageController
import kz.cheflistcompose.presentation.ui.theme.ChefListTheme
import kz.cheflistcompose.presentation.util.Navigation
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @ExperimentalPagerApi
    var pagerState = PagerState(pageCount = 2)
    val viewmodel: MainActivityViewModel by viewModels()
    @ExperimentalMaterialNavigationApi
    lateinit var bottomSheetNavigator:BottomSheetNavigator
    @ExperimentalPagerApi
    @ExperimentalMaterialNavigationApi
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProvideWindowInsets {
                val systemUiController = rememberSystemUiController()
                systemUiController.setNavigationBarColor(Color.Black)
                systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
                WindowCompat.setDecorFitsSystemWindows(window, false)
                val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                    bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
                )
                val coroutineScope = rememberCoroutineScope()
                pagerState = rememberPagerState(
                    pageCount = 2,
                    // We increase the offscreen limit, to allow pre-loading of images
                    initialOffscreenLimit = 2,
                )
                viewmodel.currentPage.observe(this, {
                    Timber.wtf("clicked $it")
                    coroutineScope.launch {
                        pageController.animateScrollToPage(it)
                    }
                })
                bottomSheetNavigator = rememberBottomSheetNavigator()
                val navController = rememberNavController()
                navController.navigatorProvider += bottomSheetNavigator
                BottomSheetScaffold(
                    scaffoldState = bottomSheetScaffoldState,
                    sheetShape = RoundedCornerShape(
                        topStart = 27.dp,
                        topEnd = 27.dp
                    ),
                    modifier = Modifier.navigationBarsPadding(),
                    sheetContent = {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.94f)
                                .clickable {
                                    coroutineScope.launch {
                                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        } else {
                                            bottomSheetScaffoldState.bottomSheetState.collapse()
                                        }
                                    }
                                }
                        ) {
                            Text(text = "Hello from sheet")
                        }
                    }, sheetPeekHeight = 70.dp
                ) {
                    ChefListTheme {
                        Surface(color = MaterialTheme.colors.background) {
                            Navigation(pagerState, navController, viewmodel)
                        }
                    }
                }
            }
        }
    }

    @ExperimentalMaterialNavigationApi
    @ExperimentalPagerApi
    override fun onBackPressed() {
        if (pagerState.currentPage == 1 && !bottomSheetNavigator.navigatorSheetState.isVisible) {
            viewmodel.currentPage.value = 0
        } else {
            super.onBackPressed()
        }
    }
}
