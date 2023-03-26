package com.hana.fieldmate.domain.usecase

import android.net.Uri
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.TaskTypeQuery
import com.hana.fieldmate.data.remote.model.response.*
import com.hana.fieldmate.data.remote.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(
        businessId: Long,
        taskCategoryId: Long,
        date: String,
        title: String,
        description: String,
        imageUriList: List<Uri>
    ): Flow<ResultWrapper<CreateTaskRes>> =
        taskRepository.createTask(
            businessId,
            taskCategoryId,
            date,
            title,
            description,
            imageUriList
        )
}

class UpdateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(
        taskId: Long,
        businessId: Long,
        taskCategoryId: Long,
        title: String,
        description: String,
        deleteImageIdList: List<Long>,
        addImageUriList: List<Uri>
    ): Flow<ResultWrapper<UpdateTaskRes>> =
        taskRepository.updateTask(
            taskId,
            businessId,
            taskCategoryId,
            title,
            description,
            deleteImageIdList,
            addImageUriList
        )
}

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(taskId: Long): Flow<ResultWrapper<DeleteTaskRes>> =
        taskRepository.deleteTask(taskId)
}

class FetchTaskByIdUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(taskId: Long): Flow<ResultWrapper<TaskRes>> =
        taskRepository.fetchTaskById(taskId)
}

class FetchTaskListUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(
        companyId: Long,
        date: String,
        type: TaskTypeQuery
    ): Flow<ResultWrapper<TaskListRes>> =
        taskRepository.fetchTaskList(companyId, date, type)
}

class FetchTaskGraphByClientIdUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(clientId: Long): Flow<ResultWrapper<TaskStatisticListRes>> =
        taskRepository.fetchTaskGraphByClientId(clientId)
}

class FetchTaskGraphByBusinessIdUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(businessId: Long): Flow<ResultWrapper<TaskStatisticListRes>> =
        taskRepository.fetchTaskGraphByBusinessId(businessId)
}

class FetchTaskListByDateUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(
        businessId: Long,
        year: Int,
        month: Int,
        day: Int? = null,
        categoryId: Long? = null
    ): Flow<ResultWrapper<TaskListRes>> =
        taskRepository.fetchTaskListByDate(businessId, year, month, day, categoryId)
}