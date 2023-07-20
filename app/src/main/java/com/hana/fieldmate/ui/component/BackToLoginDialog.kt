package com.hana.fieldmate.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.theme.Main356DF8
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.util.TOKEN_EXPIRED_OR_UNAUTHORIZED_MESSAGE

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BackToLoginDialog(
    onDismissRequest: () -> Unit = { },
    message: String = TOKEN_EXPIRED_OR_UNAUTHORIZED_MESSAGE,
    onClose: () -> Unit = { }
) {
    FDialog(
        onDismissRequest = onDismissRequest,
        content = {
            Text(
                modifier = Modifier.padding(all = 30.dp),
                text = message,
                textAlign = TextAlign.Center,
                style = Typography.body2
            )
        },
        button = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onClose() }
            ) {
                Text(
                    modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
                    text = stringResource(id = R.string.confirm),
                    style = Typography.body1,
                    textAlign = TextAlign.Center,
                    color = Main356DF8
                )
            }
        }
    )
}