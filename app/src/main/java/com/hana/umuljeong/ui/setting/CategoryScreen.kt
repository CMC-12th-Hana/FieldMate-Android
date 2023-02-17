package com.hana.umuljeong.ui.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hana.umuljeong.R
import com.hana.umuljeong.data.datasource.fakeCategorySelectionData
import com.hana.umuljeong.ui.component.UAppBarWithDeleteBtn
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.theme.BgF8F8FA
import com.hana.umuljeong.ui.theme.CategoryColor
import com.hana.umuljeong.ui.theme.Main356DF8
import com.hana.umuljeong.ui.theme.Shapes

enum class CategoryMode {
    VIEW, EDIT
}

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var mode by remember { mutableStateOf(CategoryMode.VIEW) }
    val selectedCategories = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            UAppBarWithDeleteBtn(
                title = stringResource(id = R.string.change_category),
                backBtnOnClick = {
                    navController.navigateUp()
                },
                deleteBtnOnClick = {
                    mode = CategoryMode.EDIT
                }
            )
        },
        bottomBar = {
            if (mode == CategoryMode.EDIT) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Transparent),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(30.dp))

                    UButton(
                        modifier = Modifier.width(335.dp),
                        onClick = { mode = CategoryMode.VIEW }
                    ) {
                        Text(
                            text = stringResource(id = R.string.delete)
                        )
                    }

                    Spacer(Modifier.height(50.dp))
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            items(fakeCategorySelectionData) { category ->
                CategoryItem(
                    modifier = Modifier.width(335.dp),
                    category = category,
                    categoryColor = CategoryColor[fakeCategorySelectionData.indexOf(category)],
                    selected = selectedCategories.contains(category),
                    selectCategory = { selectedCategories.add(category) },
                    unselectCategory = { selectedCategories.remove(category) },
                    mode = mode
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    category: String,
    categoryColor: Pair<Color, Color>,
    selected: Boolean,
    selectCategory: () -> Boolean,
    unselectCategory: () -> Boolean,
    mode: CategoryMode
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = BgF8F8FA,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (mode == CategoryMode.EDIT) {
                    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                        RadioButton(
                            modifier = Modifier.size(24.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Main356DF8
                            ),
                            selected = selected,
                            onClick = {
                                if (!selected) selectCategory() else unselectCategory()
                            }
                        )
                    }
                }

                Text(text = category)
            }

            Surface(
                shape = Shapes.large,
                color = Color.Transparent,
                border = BorderStroke(width = 1.dp, color = categoryColor.first),
                contentColor = Color.White,
                elevation = 0.dp
            ) {
                Text(
                    modifier = Modifier.padding(
                        top = 6.dp, bottom = 6.dp, start = 10.dp, end = 10.dp
                    ),
                    text = category,
                    color = categoryColor.second,
                    fontSize = 14.sp
                )
            }
        }
    }
}