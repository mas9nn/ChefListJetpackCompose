package kz.cheflistcompose.presentation.util

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.material.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kz.cheflistcompose.presentation.MainActivityViewModel
import kz.cheflistcompose.presentation.bottomsheets.BottomSheet
import kz.cheflistcompose.presentation.main.MainScreen
import timber.log.Timber


@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalMaterialNavigationApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun Navigation(
    pagerState: PagerState,
    navController: NavHostController,
    viewModel: MainActivityViewModel = hiltViewModel()
) {


    ModalBottomSheetLayout(
        bottomSheetNavigator = navController.navigatorProvider.getNavigator(BottomSheetNavigator::class.java),
        sheetShape = RoundedCornerShape(topEnd = 27.dp, topStart = 27.dp),
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.MainScreen.route
        ) {

            composable(Screen.MainScreen.route) {
                MainScreen(navController, pagerState, viewModel = viewModel)
            }
            bottomSheet(Screen.BottomSheet.route) { backstackEntry ->
                BottomSheet()
            }
        }
    }
}