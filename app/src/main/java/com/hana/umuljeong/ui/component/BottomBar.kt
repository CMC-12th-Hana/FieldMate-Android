package com.hana.umuljeong.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hana.umuljeong.R
import com.hana.umuljeong.UmuljeongScreen
import com.hana.umuljeong.ui.theme.ButtonSkyBlue
import com.hana.umuljeong.ui.theme.LineLightGray

enum class Sections(
    @StringRes val title: Int,
    @DrawableRes val icon: Pair<Int, Int>,
    val route: String
) {
    HOME(
        R.string.home,
        Pair(R.drawable.ic_home_filled, R.drawable.ic_home_outlined),
        UmuljeongScreen.Home.name
    ),
    CUSTOMER(
        R.string.customer,
        Pair(R.drawable.ic_customer_filled, R.drawable.ic_customer_outlined),
        UmuljeongScreen.Customer.name
    ),
    BOOKMARK(
        R.string.bookmark,
        Pair(R.drawable.ic_bookmark_filled, R.drawable.ic_bookmark_outlined),
        UmuljeongScreen.Bookmark.name
    ),
    PROFILE(
        R.string.profile,
        Pair(R.drawable.ic_profile_filled, R.drawable.ic_profile_outlined),
        UmuljeongScreen.Profile.name
    )
}

@Composable
fun UBottomBar(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val items = listOf(
        Sections.HOME, Sections.CUSTOMER, Sections.BOOKMARK, Sections.PROFILE
    )

    Surface(
        color = Color.White,
        modifier = Modifier
            .border(width = 1.dp, color = LineLightGray)
    ) {
        BottomNavigation(
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->
                val selected = currentRoute == item.route

                /*
                UmuljeongBottomNavigationItem(
                    tab = item,
                    selected = selected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                 */


                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = if (selected) item.icon.first else item.icon.second),
                            contentDescription = stringResource(id = item.title),
                            tint = Color.Unspecified
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = item.title),
                            fontSize = 12.sp,
                        )
                    },
                    selected = selected,
                    selectedContentColor = ButtonSkyBlue,
                    unselectedContentColor = Color(0xFF656565),
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun UBottomNavigationItem(
    tab: Sections,
    selected: Boolean,
    onClick: () -> Unit
) {
    val contentColor =
        if (selected) ButtonSkyBlue else Color(0xFF656565)

    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = if (selected) tab.icon.first else tab.icon.second),
                contentDescription = stringResource(id = tab.title),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(id = tab.title),
                fontSize = 12.sp,
                color = contentColor
            )
        }
    }
}