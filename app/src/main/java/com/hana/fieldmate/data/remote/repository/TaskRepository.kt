package com.hana.fieldmate.data.remote.repository

import android.content.Context
import android.net.Uri
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.datasource.TaskDataSource
import com.hana.fieldmate.data.remote.model.response.CreateTaskRes
import com.hana.fieldmate.data.remote.model.response.TaskListRes
import com.hana.fieldmate.data.remote.model.response.TaskRes
import com.hana.fieldmate.getRealPathFromURI
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
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
        val images = ArrayList<MultipartBody.Part>()

        for (imageUri in imageUriList) {
            val image = File(getRealPathFromURI(context, imageUri))
            val requestBody = image.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("taskImageList", image.name, requestBody)
            images.add(part)
        }

        val data = HashMap<String, RequestBody>()
        data["businessId"] = businessId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        data["taskCategoryId"] =
            taskCategoryId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        data["date"] = date.toRequestBody("text/plain".toMediaTypeOrNull())
        data["title"] = title.toRequestBody("text/plain".toMediaTypeOrNull())
        data["description"] = description.toRequestBody("text/plain".toMediaTypeOrNull())

        return taskDataSource.createTask(data, images)
    }

    fun fetchTaskById(taskId: Long): Flow<ResultWrapper<TaskRes>> =
        taskDataSource.fetchTaskById(taskId)

    fun fetchTaskList(
        companyId: Long,
        date: String,
        type: String
    ): Flow<ResultWrapper<TaskListRes>> =
        taskDataSource.fetchTaskList(companyId, date, type)
}