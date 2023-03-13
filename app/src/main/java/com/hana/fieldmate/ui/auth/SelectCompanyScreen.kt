package com.hana.fieldmate.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.component.FImageButton

@Composable
fun SelectCompanyScreen(
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
            FImageButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.45f, true),
                imageModifier = modifier.size(width = 110.dp, height = 100.dp),
                onClick = addCompanyBtnOnClick,
                title = stringResource(id = R.string.add_company),
                description = stringResource(id = R.string.add_company_info_one),
                image = R.drawable.img_add_company
            )

            Spacer(modifier = Modifier.height(10.dp))

            FImageButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.45f, true),
                imageModifier = modifier.size(width = 115.dp, height = 115.dp),
                onClick = joinCompanyBtnOnClick,
                title = stringResource(id = R.string.join_company),
                description = stringResource(R.string.join_company_info),
                image = R.drawable.img_join_company
            )
        }
    }
}
