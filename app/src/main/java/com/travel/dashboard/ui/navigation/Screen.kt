package com.travel.dashboard.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String? = null, val icon: ImageVector? = null) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Filled.Home)
    object Manage : Screen("manage", "Manage", Icons.Filled.List)
    object TripDetail : Screen("trip_detail/{tripId}") {
        fun createRoute(tripId: Int) = "trip_detail/$tripId"
    }
}
