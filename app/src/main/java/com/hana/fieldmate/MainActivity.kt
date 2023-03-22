package com.hana.fieldmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hana.fieldmate.ui.theme.FieldMateTheme
import com.hana.fieldmate.ui.theme.Main356DF8
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val systemUiController = rememberSystemUiController()

            systemUiController.setSystemBarsColor(
                color = Main356DF8
            )

            FieldMateTheme {
                FieldMateApp()
            }
        }
    }
}
