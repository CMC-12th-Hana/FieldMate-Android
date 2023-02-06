package com.hana.umuljeong.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hana.umuljeong.R
import com.hana.umuljeong.UmuljeongScreen
import com.hana.umuljeong.ui.theme.ButtonSkyBlue
import com.hana.umuljeong.ui.theme.LineLightGray
import com.hana.umuljeong.ui.theme.UmuljeongTheme

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
        R.string.business,
        Pair(R.drawable.ic_bookmark_filled, R.drawable.ic_bookmark_outlined),
        UmuljeongScreen.Business.name
    ),
    PROFILE(
        R.string.member,
        Pair(R.drawable.ic_member_filled, R.drawable.ic_member_outlined),
        UmuljeongScreen.Member.name
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
        modifier = modifier
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

@Preview
@Composable
fun PreviewBottomBar() {
    UmuljeongTheme {
        UBottomBar(navController = rememberNavController())
    }
}