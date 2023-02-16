package com.hana.umuljeong.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.hana.umuljeong.ui.theme.LineDBDBDB
import com.hana.umuljeong.ui.theme.Main356DF8
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
        R.string.company,
        Pair(R.drawable.ic_customer_filled, R.drawable.ic_customer_outlined),
        UmuljeongScreen.Company.name
    ),
    BUSINESS(
        R.string.business,
        Pair(R.drawable.ic_business_filled, R.drawable.ic_business_outlined),
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
        Sections.HOME, Sections.CUSTOMER, Sections.BUSINESS, Sections.PROFILE
    )

    Surface(
        modifier = modifier,
        color = Color.White,
        elevation = 20.dp,
        border = BorderStroke(width = 1.dp, color = LineDBDBDB),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Row(
            Modifier
                .fillMaxWidth()
                .height(72.dp)
                .selectableGroup(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route

                Spacer(modifier = Modifier.width(25.dp))

                UBottomNavigationItem(
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

                Spacer(modifier = Modifier.width(25.dp))
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
        if (selected) Main356DF8 else Color(0xFF656565)

    Box(
        modifier = Modifier
            .background(color = Color.Transparent)
            .padding(top = 10.dp, bottom = 14.dp)
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

@Preview
@Composable
fun PreviewBottomBar() {
    UmuljeongTheme {
        UBottomBar(navController = rememberNavController())
    }
}