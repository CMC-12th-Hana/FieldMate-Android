package com.hana.umuljeong.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.component.UImageButton
import com.hana.umuljeong.ui.theme.UmuljeongTheme

@Composable
fun SelectMyCompanyScreen(
    modifier: Modifier = Modifier,
    joinCompanyBtnOnClick: () -> Unit,
    addCompanyBtnOnClick: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UImageButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.45f, true),
                imageModifier = modifier.size(width = 110.dp, height = 100.dp),
                onClick = addCompanyBtnOnClick,
                title = stringResource(id = R.string.add_my_company),
                description = stringResource(id = R.string.add_my_company_info_one),
                image = R.drawable.img_add_company
            )

            Spacer(modifier = Modifier.height(10.dp))

            UImageButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.45f, true),
                imageModifier = modifier.size(width = 115.dp, height = 115.dp),
                onClick = joinCompanyBtnOnClick,
                title = stringResource(id = R.string.join_my_company),
                description = stringResource(R.string.join_my_company_info),
                image = R.drawable.img_join_company
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelectMyCompanyScreen() {
    UmuljeongTheme() {
        SelectMyCompanyScreen(joinCompanyBtnOnClick = { /*TODO*/ }) {

        }
    }
}