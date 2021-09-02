package kz.cheflistcompose.presentation.util

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object RestaurantScreen : Screen("restaurant_screen")
    object BottomSheet : Screen("bottomsheet_screen")
}
