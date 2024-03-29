package com.hana.fieldmate.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.FDialog
import com.hana.fieldmate.ui.navigation.FieldMateScreen
import com.hana.fieldmate.ui.theme.*
import com.hana.fieldmate.util.LEADER
import kotlinx.coroutines.runBlocking

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val userInfo = App.getInstance().getUserInfo()

    var logoutDialogOpen by remember { mutableStateOf(false) }
    if (logoutDialogOpen) LogoutDialog(
        onClose = { logoutDialogOpen = false },
        onConfirm = {
            runBlocking {
                App.getInstance().getDataStore().deleteAccessToken()
                App.getInstance().getDataStore().deleteRefreshToken()
            }
            navController.navigate(FieldMateScreen.Login.name) {
                popUpTo(FieldMateScreen.TaskGraph.name) {
                    inclusive = true
                }
                launchSingleTop = true
            }

            logoutDialogOpen = false
        }
    )

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.setting),
                backBtnOnClick = {
                    navController.navigateUp()
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(13.dp))

            SettingItem(
                onClick = { navController.navigate(FieldMateScreen.Category.name) },
                icon = painterResource(id = R.drawable.ic_category),
                title = stringResource(id = R.string.change_category)
            )

            SettingItem(
                onClick = { navController.navigate(FieldMateScreen.ChangePassword.name) },
                icon = painterResource(id = R.drawable.ic_password),
                title = stringResource(id = R.string.change_password)
            )

            if (userInfo.userRole == LEADER) {
                SettingItem(
                    onClick = { navController.navigate(FieldMateScreen.ChangeLeader.name) },
                    icon = painterResource(id = R.drawable.ic_change_leader),
                    title = stringResource(id = R.string.change_leader)
                )
            }

            SettingItem(
                onClick = { logoutDialogOpen = true },
                icon = painterResource(id = R.drawable.ic_logout),
                title = stringResource(id = R.string.logout)
            )

            SettingItem(
                onClick = { navController.navigate(FieldMateScreen.Withdrawal.name) },
                icon = painterResource(id = R.drawable.ic_quit),
                title = stringResource(id = R.string.withdrawal)
            )

            SettingItem(
                onClick = { navController.navigate(FieldMateScreen.AppInfo.name) },
                icon = painterResource(id = R.drawable.ic_app_info),
                title = stringResource(id = R.string.app_info)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = Shapes.large,
    textStyle: TextStyle = Typography.body2,
    icon: Painter,
    title: String
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 17.dp, bottom = 17.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = icon,
                    tint = Color.Unspecified,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(10.dp))

                Text(text = title, style = textStyle)
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LogoutDialog(
    onClose: () -> Unit,
    onConfirm: () -> Unit
) {
    FDialog(
        onDismissRequest = { },
        content = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(top = 30.dp, bottom = 30.dp),
                    text = stringResource(id = R.string.app_name),
                    textAlign = TextAlign.Center,
                    style = Typography.body2,
                    color = Main356DF8
                )
                Text(
                    modifier = Modifier.padding(top = 30.dp, bottom = 30.dp),
                    text = stringResource(id = R.string.logout_message),
                    textAlign = TextAlign.Center,
                    style = Typography.body2
                )
            }
        },
        button = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    onClick = onClose
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.cancel),
                            style = Typography.body1,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(
                    Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(LineDBDBDB)
                )

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    onClick = onConfirm
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.logout),
                            style = Typography.body1,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}