package com.hana.fieldmate.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.theme.*

data class OnBoardingInfo(
    val image: Painter,
    val title: String,
    val description: String
)

@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val onBoardingData = listOf(
        OnBoardingInfo(
            image = painterResource(id = R.drawable.img_onboarding_1),
            title = stringResource(id = R.string.onboarding_title_one),
            description = stringResource(id = R.string.onboarding_info_one)
        ),
        OnBoardingInfo(
            image = painterResource(id = R.drawable.img_onboarding_2),
            title = stringResource(id = R.string.onboarding_title_two),
            description = stringResource(id = R.string.onboarding_info_two)
        ),
        OnBoardingInfo(
            image = painterResource(id = R.drawable.img_onboarding_3),
            title = stringResource(id = R.string.onboarding_title_three),
            description = stringResource(id = R.string.onboarding_info_three)
        ),
        OnBoardingInfo(
            image = painterResource(id = R.drawable.img_onboarding_4),
            title = stringResource(id = R.string.onboarding_title_four),
            description = stringResource(id = R.string.onboarding_info_four)
        )
    )

    OnBoardingPager(
        onBoardingInfo = onBoardingData,
        confirmButtonOnClick = {
            navController.navigate(FieldMateScreen.TaskList.name) {
                popUpTo(FieldMateScreen.OnBoarding.name) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun OnBoardingPager(
    modifier: Modifier = Modifier,
    onBoardingInfo: List<OnBoardingInfo>,
    confirmButtonOnClick: () -> Unit
) {
    val pagerState = rememberPagerState()

    Column {
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            count = onBoardingInfo.size,
            state = pagerState
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                )

                Text(
                    text = stringResource(id = R.string.onboarding),
                    style = Typography.title2,
                    fontSize = 20.sp,
                    color = Main356DF8
                )

                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    shape = RoundedCornerShape(size = 30.dp),
                    color = Main356DF8,
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 5.dp,
                            bottom = 5.dp
                        ),
                        text = "0${currentPage + 1}",
                        style = Typography.title2,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                Icon(
                    modifier = Modifier.size(301.dp, 294.dp),
                    painter = onBoardingInfo[currentPage].image,
                    tint = Color.Unspecified,
                    contentDescription = null
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(36.dp))

                    Text(
                        text = onBoardingInfo[currentPage].title,
                        style = Typography.title2,
                        textAlign = TextAlign.Center,
                        color = Main356DF8
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        text = onBoardingInfo[currentPage].description,
                        style = Typography.title2,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                    )

                    Spacer(modifier = Modifier.height(50.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            for (i in onBoardingInfo.indices) {
                                val selected = (i == pagerState.currentPage)

                                Spacer(
                                    modifier
                                        .size(10.dp)
                                        .background(
                                            color = if (selected) Main356DF8 else BgD3D3D3,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Main356DF8,
            onClick = confirmButtonOnClick
        ) {
            Text(
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                text = stringResource(id = R.string.start),
                textAlign = TextAlign.Center,
                style = Typography.body1,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun PreviewOnBoardingScreen() {
    FieldMateTheme {
        OnBoardingScreen(navController = rememberNavController())
    }
}