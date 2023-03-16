package com.hana.fieldmate.data.remote.repository

import ImageUtil
import android.content.Context
import android.net.Uri
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.datasource.TaskDataSource
import com.hana.fieldmate.data.remote.model.response.*
import com.hana.fieldmate.network.TaskTypeQuery
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class TaskRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val taskDataSource: TaskDataSource
) {
    fun createTask(
        businessId: Long,
        taskCategoryId: Long,
        date: String,
        title: String,
        description: String,
        imageUriList: List<Uri>
    ): Flow<ResultWrapper<CreateTaskRes>> {
        val images = ImageUtil.bitmapResize(context, "taskImageList", imageUriList)

        val data = HashMap<String, RequestBody>()
        data["businessId"] = businessId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        data["taskCategoryId"] =
            taskCategoryId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        data["date"] = date.toRequestBody("text/plain".toMediaTypeOrNull())
        data["title"] = title.toRequestBody("text/plain".toMediaTypeOrNull())
        data["description"] = description.toRequestBody("text/plain".toMediaTypeOrNull())

        return taskDataSource.createTask(data, images!!)
    }

    fun updateTask(
        taskId: Long,
        businessId: Long,
        taskCategoryId: Long,
        title: String,
        description: String,
        deleteImageIdList: List<Long>,
        addImageUriList: List<Uri>
    ): Flow<ResultWrapper<UpdateTaskRes>> {
        val addImageList = ImageUtil.bitmapResize(context, "addTaskImageList", addImageUriList)

        val data = HashMap<String, RequestBody>()
        data["businessId"] = businessId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        data["taskCategoryId"] =
            taskCategoryId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        data["title"] = title.toRequestBody("text/plain".toMediaTypeOrNull())
        data["description"] = description.toRequestBody("text/plain".toMediaTypeOrNull())
        data["deleteImageIdList"] =
            deleteImageIdList.joinToString(",").toRequestBody("text/plain".toMediaTypeOrNull())

        return taskDataSource.updateTask(taskId, data, addImageList!!)
    }

    fun deleteTask(taskId: Long): Flow<ResultWrapper<DeleteTaskRes>> =
        taskDataSource.deleteTaskBy(taskId)

    fun fetchTaskById(taskId: Long): Flow<ResultWrapper<TaskRes>> =
        taskDataSource.fetchTaskById(taskId)

    fun fetchTaskList(
        companyId: Long,
        date: String,
        type: TaskTypeQuery
    ): Flow<ResultWrapper<TaskListRes>> =
        taskDataSource.fetchTaskList(companyId, date, type)

    fun fetchTaskGraphByClientId(clientId: Long): Flow<ResultWrapper<TaskStatisticListRes>> =
        taskDataSource.fetchTaskGraphByClientId(clientId)

    fun fetchTaskGraphByBusinessId(businessId: Long): Flow<ResultWrapper<TaskStatisticListRes>> =
        taskDataSource.fetchTaskGraphByBusinessId(businessId)

    fun fetchTaskListByDate(
        businessId: Long,
        year: Int,
        month: Int,
        day: Int?,
        categoryId: Long?
    ): Flow<ResultWrapper<TaskListRes>> =
        taskDataSource.fetchTaskListByDate(businessId, year, month, day, categoryId)
}