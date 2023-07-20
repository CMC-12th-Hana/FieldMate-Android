package com.hana.fieldmate.ui.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.domain.model.CategoryEntity
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.EditMode
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.setting.viewmodel.CategoryViewModel
import com.hana.fieldmate.ui.theme.*
import com.hana.fieldmate.util.LEADER
import com.hana.fieldmate.util.StringUtil.toColor

enum class CategoryMode {
    VIEW, EDIT
}

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userInfo = App.getInstance().getUserInfo()

    var viewMode by remember { mutableStateOf(CategoryMode.VIEW) }
    var editMode by remember { mutableStateOf(EditMode.Add) }
    var categoryEntity: CategoryEntity? by remember { mutableStateOf(null) }
    val selectedCategories = remember { mutableStateListOf<CategoryEntity>() }

    when (uiState.dialog) {
        is DialogEvent.AddEdit -> {
            AddEditCategoryDialog(
                editMode = editMode,
                categoryEntity = categoryEntity,
                userInfo = userInfo,
                onCreate = viewModel::createTaskCategory,
                onUpdate = viewModel::updateTaskCategory,
                onClose = { viewModel.onDialogClosed() }
            )
        }
        is DialogEvent.Delete -> {
            DeleteCategoryDialog(
                userInfo = userInfo,
                selectedCategoryList = selectedCategories,
                onClose = {
                    viewModel.onDialogClosed()
                    viewMode = CategoryMode.VIEW
                },
                onDelete = viewModel::deleteTaskCategory
            )
        }
        is DialogEvent.Error -> {
            when (val error = (uiState.dialog as DialogEvent.Error).errorType) {
                is ErrorType.JwtExpired -> {
                    BackToLoginDialog(onClose = { viewModel.backToLogin() })
                }
                is ErrorType.General -> {
                    ErrorDialog(
                        errorMessage = error.errorMessage,
                        onClose = { viewModel.onDialogClosed() }
                    )
                }
            }
        }
        else -> {}
    }

    LaunchedEffect(true) {
        viewModel.loadCategories(userInfo.companyId)
    }

    Scaffold(
        topBar = {
            if (userInfo.userRole == LEADER) {
                FAppBarWithDeleteBtn(
                    title = stringResource(id = R.string.change_category),
                    backBtnOnClick = {
                        viewModel.navigateTo(NavigateActions.navigateUp())
                    },
                    deleteBtnOnClick = {
                        viewMode = CategoryMode.EDIT
                    }
                )
            } else {
                FAppBarWithBackBtn(
                    title = stringResource(id = R.string.change_category),
                    backBtnOnClick = {
                        viewModel.navigateTo(NavigateActions.navigateUp())
                    }
                )
            }
        }
    ) { innerPadding ->
        LoadingContent(loadingState = uiState.categoryListLoadingState) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .weight(1f)
                ) {
                    item {
                        if (userInfo.userRole == LEADER) {
                            Spacer(modifier = Modifier.height(30.dp))

                            FAddButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    editMode = EditMode.Add
                                    categoryEntity = null
                                    viewModel.openAddEditDialog()
                                },
                                text = stringResource(id = R.string.add_category)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    items(uiState.categoryList) { category ->
                        CategoryItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                if (userInfo.userRole == LEADER) {
                                    categoryEntity = it
                                    editMode = EditMode.Edit
                                    viewModel.openAddEditDialog()
                                }
                            },
                            categoryEntity = category,
                            selected = selectedCategories.contains(category),
                            selectCategory = { selectedCategories.add(category) },
                            unselectCategory = { selectedCategories.remove(category) },
                            mode = viewMode
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                Spacer(modifier = Modifier.fillMaxHeight())

                Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = Shapes.large,
                        color = Font191919.copy(alpha = 0.7f),
                        elevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 15.dp,
                                    bottom = 15.dp,
                                    start = 20.dp,
                                    end = 20.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_info),
                                tint = Color.White,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = stringResource(id = R.string.leader_permission_info),
                                style = Typography.body3,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    if (viewMode == CategoryMode.EDIT) {
                        FButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.delete),
                            onClick = {
                                viewModel.openDeleteDialog()
                            }
                        )

                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    onClick: (CategoryEntity) -> Unit,
    categoryEntity: CategoryEntity,
    selected: Boolean,
    selectCategory: () -> Boolean,
    unselectCategory: () -> Boolean,
    mode: CategoryMode
) {
    Surface(
        onClick = { onClick(categoryEntity) },
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
                    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
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

                Text(text = categoryEntity.name, style = Typography.body2)
            }

            CategoryTag(text = categoryEntity.name, color = categoryEntity.color.toColor())
        }
    }
}

@Composable
fun CategoryTag(
    text: String,
    color: Color
) {
    Row(
        modifier = Modifier.width(81.dp),
        horizontalArrangement = Arrangement.End
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
}

