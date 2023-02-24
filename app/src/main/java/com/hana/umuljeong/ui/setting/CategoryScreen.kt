package com.hana.umuljeong.ui.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.umuljeong.R
import com.hana.umuljeong.data.datasource.fakeCategorySelectionData
import com.hana.umuljeong.ui.component.UAddButton
import com.hana.umuljeong.ui.component.UAppBarWithDeleteBtn
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.theme.*

enum class CategoryMode {
    VIEW, EDIT
}

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var mode by rememberSaveable { mutableStateOf(CategoryMode.VIEW) }
    val selectedCategories = remember { mutableStateListOf<String>() }

    var addCategoryOpen by rememberSaveable { mutableStateOf(false) }

    if (addCategoryOpen) AddCategoryDialog(
        addBtnOnClick = { addCategoryOpen = false },
        cancelBtnOnClick = { addCategoryOpen = false }
    )

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
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    UAddButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { addCategoryOpen = true },
                        text = stringResource(id = R.string.add_category)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(fakeCategorySelectionData) { category ->
                    CategoryItem(
                        modifier = Modifier.fillMaxWidth(),
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

        if (mode == CategoryMode.EDIT) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f))

                Spacer(modifier = Modifier.height(30.dp))

                UButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    text = stringResource(id = R.string.delete),
                    onClick = { mode = CategoryMode.VIEW }
                )

                Spacer(modifier = Modifier.height(50.dp))
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
    categoryColor: Color,
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

                Text(text = category, style = Typography.body2)
            }

            CategoryTag(text = category, color = categoryColor)
        }
    }
}

@Composable
fun CategoryTag(
    text: String,
    color: Color
) {
    Surface(
        shape = Shapes.large,
        color = Color.Transparent,
        border = BorderStroke(width = 1.dp, color = color.copy(alpha = 0.4f)),
        contentColor = Color.White,
        elevation = 0.dp
    ) {
        Text(
            modifier = Modifier.padding(
                top = 6.dp, bottom = 6.dp, start = 10.dp, end = 10.dp
            ),
            text = text,
            style = Typography.body3,
            color = color
        )
    }
}