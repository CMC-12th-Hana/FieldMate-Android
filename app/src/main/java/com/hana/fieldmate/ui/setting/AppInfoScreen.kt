package com.hana.fieldmate.ui.setting

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.theme.Main356DF8
import com.hana.fieldmate.ui.theme.Shapes
import com.hana.fieldmate.ui.theme.Typography

@Composable
fun AppInfoScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.app_info),
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
            AppInfoItem(
                onClick = { },
                content = {
                    Text(
                        text = stringResource(id = R.string.version_info),
                        style = Typography.body2
                    )

                    Text(
                        text = stringResource(id = R.string.app_version),
                        style = Typography.body2,
                        color = Main356DF8
                    )
                }
            )

            AppInfoItem(
                onClick = { navController.navigate(FieldMateScreen.TermsOfUse.name) },
                content = {
                    Text(
                        text = stringResource(id = R.string.terms_of_use),
                        style = Typography.body2
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                }
            )

            AppInfoItem(
                onClick = { navController.navigate(FieldMateScreen.PrivacyPolicy.name) },
                content = {
                    Text(
                        text = stringResource(id = R.string.privacy_policy),
                        style = Typography.body2
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppInfoItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = Shapes.large,
    content: @Composable () -> Unit
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
                .padding(top = 17.dp, bottom = 17.dp, start = 30.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            content()
        }
    }
}