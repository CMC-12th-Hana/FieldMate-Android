package com.hana.fieldmate.data.remote.api

import com.hana.fieldmate.data.remote.model.response.CreateTaskRes
import com.hana.fieldmate.data.remote.model.response.TaskListRes
import com.hana.fieldmate.data.remote.model.response.TaskRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface TaskService {
    @POST("/company/client/business/task")
    suspend fun createTask(
        @PartMap data: HashMap<String, RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Result<CreateTaskRes>

    @GET("/company/client/business/task/{taskId}")
    suspend fun fetchTaskById(@Path("taskId") taskId: Long): Result<TaskRes>

    @GET("/company/{companyId}/client/business/tasks")
    suspend fun fetchTaskList(@Path("companyId") taskId: Long): Result<TaskListRes>
}