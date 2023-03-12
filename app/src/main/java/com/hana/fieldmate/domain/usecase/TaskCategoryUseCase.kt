package com.hana.fieldmate.domain.usecase

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.response.CreateTaskCategoryRes
import com.hana.fieldmate.data.remote.model.response.DeleteTaskCategoryListRes
import com.hana.fieldmate.data.remote.model.response.TaskCategoryListRes
import com.hana.fieldmate.data.remote.model.response.UpdateTaskCategoryRes
import com.hana.fieldmate.data.remote.repository.TaskCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateTaskCategoryUseCase @Inject constructor(
    private val taskCategoryRepository: TaskCategoryRepository
) {
    operator fun invoke(
        companyId: Long,
        name: String,
        color: String
    ): Flow<ResultWrapper<CreateTaskCategoryRes>> =
        taskCategoryRepository.createTaskCategory(companyId, name, color)
}

class UpdateTaskCategoryUseCase @Inject constructor(
    private val taskCategoryRepository: TaskCategoryRepository
) {
    operator fun invoke(
        categoryId: Long,
        name: String,
        color: String
    ): Flow<ResultWrapper<UpdateTaskCategoryRes>> =
        taskCategoryRepository.updateTaskCategory(categoryId, name, color)
}

class FetchTaskCategoryListUseCase @Inject constructor(
    private val taskCategoryRepository: TaskCategoryRepository
) {
    operator fun invoke(
        companyId: Long
    ): Flow<ResultWrapper<TaskCategoryListRes>> =
        taskCategoryRepository.fetchTaskCategoryList(companyId)
}

class DeleteTaskCategoryUseCase @Inject constructor(
    private val taskCategoryRepository: TaskCategoryRepository
) {
    operator fun invoke(
        categoryIdList: List<Long>
    ): Flow<ResultWrapper<DeleteTaskCategoryListRes>> =
        taskCategoryRepository.deleteTaskCategory(categoryIdList)
}