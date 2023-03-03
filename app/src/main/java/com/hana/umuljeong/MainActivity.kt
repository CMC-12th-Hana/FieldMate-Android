package com.hana.umuljeong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hana.umuljeong.ui.theme.UmuljeongTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UmuljeongTheme {
                UmuljeongApp()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    UmuljeongTheme {
        UmuljeongApp()
    }
}