package com.vant.tracker.ui.navigation

sealed class Screen(val route: String) {
    object Home     : Screen("home")
    object Library  : Screen("library")
    object Add      : Screen("add")
    object Settings : Screen("settings")
    object Detail   : Screen("detail/{itemId}") {
        fun createRoute(id: Long) = "detail/$id"
    }
    object Edit     : Screen("edit/{itemId}") {
        fun createRoute(id: Long) = "edit/$id"
    }
}
