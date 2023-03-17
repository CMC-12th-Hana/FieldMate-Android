package com.hana.fieldmate.ui.setting

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
import androidx.navigation.NavController
import com.hana.fieldmate.EditMode
import com.hana.fieldmate.R
import com.hana.fieldmate.StringUtil.toColor
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.domain.model.CategoryEntity
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.setting.viewmodel.CategoryUiState
import com.hana.fieldmate.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

enum class CategoryMode {
    VIEW, EDIT
}

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadCategories: (Long) -> Unit,
    uiState: CategoryUiState,
    userInfo: UserInfo,
    addCategory: (Long, String, String) -> Unit,
    updateCategory: (Long, Long, String, String) -> Unit,
    deleteCategory: (Long, List<Long>) -> Unit,
    navController: NavController
) {
    var viewMode by remember { mutableStateOf(CategoryMode.VIEW) }
    var editMode by remember { mutableStateOf(EditMode.Add) }
    var categoryEntity: CategoryEntity? by remember { mutableStateOf(null) }
    val selectedCategories = remember { mutableStateListOf<CategoryEntity>() }

    var addEditCategoryOpen by remember { mutableStateOf(false) }
    if (addEditCategoryOpen) AddEditCategoryDialog(
        editMode = editMode,
        categoryEntity = categoryEntity,
        userInfo = userInfo,
        onCreate = addCategory,
        onUpdate = updateCategory,
        onClose = { sendEvent(Event.Dialog(DialogState.AddEdit, DialogAction.Close)) }
    )

    var deleteCategoryDialogOpen by remember { mutableStateOf(false) }
    if (deleteCategoryDialogOpen) DeleteCategoryDialog(
        userInfo = userInfo,
        selectedCategoryList = selectedCategories,
        onClose = {
            sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Close))
            viewMode = CategoryMode.VIEW
        },
        onDelete = deleteCategory
    )

    var errorDialogOpen by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Error, DialogAction.Close)) }
    )

    var jwtExpiredDialogOpen by remember { mutableStateOf(false) }
    if (jwtExpiredDialogOpen) JwtExpiredDialog(sendEvent = sendEvent)

    LaunchedEffect(true) {
        loadCategories(userInfo.companyId)

        eventsFlow.collectLatest { event ->
            when (event) {
                is Event.NavigateTo -> navController.navigate(event.destination)
                is Event.NavigatePopUpTo -> navController.navigate(event.destination) {
                    popUpTo(event.popUpDestination) {
                        inclusive = event.inclusive
                    }
                    launchSingleTop = event.launchOnSingleTop
                }
                is Event.NavigateUp -> navController.navigateUp()
                is Event.Dialog -> if (event.dialog == DialogState.AddEdit) {
                    addEditCategoryOpen = event.action == DialogAction.Open
                } else if (event.dialog == DialogState.Delete) {
                    deleteCategoryDialogOpen = event.action == DialogAction.Open
                } else if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                } else if (event.dialog == DialogState.JwtExpired) {
                    jwtExpiredDialogOpen = event.action == DialogAction.Open
                }
            }
        }
    }

    Scaffold(
        topBar = {
            FAppBarWithDeleteBtn(
                title = stringResource(id = R.string.change_category),
                backBtnOnClick = {
                    navController.navigateUp()
                },
                deleteBtnOnClick = {
                    viewMode = CategoryMode.EDIT
                }
            )
        }
    ) { innerPadding ->
        LoadingContent(loadingState = uiState.categoryListLoadingState) {
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
                        FAddButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                editMode = EditMode.Add
                                sendEvent(Event.Dialog(DialogState.AddEdit, DialogAction.Open))
                            },
                            text = stringResource(id = R.string.add_category)
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    items(uiState.categoryList) { category ->
                        CategoryItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                categoryEntity = it
                                editMode = EditMode.Edit
                                addEditCategoryOpen = true
                            },
                            categoryEntity = category,
                            selected = selectedCategories.contains(category),
                            selectCategory = { selectedCategories.add(category) },
                            unselectCategory = { selectedCategories.remove(category) },
                            mode = viewMode
                        )
                    }
                }
            }
        }

        if (viewMode == CategoryMode.EDIT) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                )

                Spacer(modifier = Modifier.height(30.dp))

                FButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    text = stringResource(id = R.string.delete),
                    onClick = {
                        sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Open))
                    }
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

