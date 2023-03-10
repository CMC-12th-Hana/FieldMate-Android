package com.hana.fieldmate.data.remote.repository

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.datasource.TaskDataSource
import com.hana.fieldmate.data.remote.model.response.CreateTaskRes
import com.hana.fieldmate.data.remote.model.response.TaskListRes
import com.hana.fieldmate.data.remote.model.response.TaskRes
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDataSource: TaskDataSource
) {
    fun createTask(
        data: HashMap<String, RequestBody>,
        images: List<MultipartBody.Part>
    ): Flow<ResultWrapper<CreateTaskRes>> {
        val data = HashMap<String, RequestBody>()

        return taskDataSource.createTask(data, images)
    }

    fun fetchTaskById(taskId: Long): Flow<ResultWrapper<TaskRes>> =
        taskDataSource.fetchTaskById(taskId)

    fun fetchTaskList(companyId: Long): Flow<ResultWrapper<TaskListRes>> =
        taskDataSource.fetchTaskList(companyId)
}