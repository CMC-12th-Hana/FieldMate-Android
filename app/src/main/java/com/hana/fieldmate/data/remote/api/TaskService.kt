package com.hana.fieldmate.data.remote.api

import com.hana.fieldmate.data.remote.model.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface TaskService {
    @Multipart
    @POST("/company/client/business/task")
    suspend fun createTask(
        @PartMap data: HashMap<String, RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Result<CreateTaskRes>

    @DELETE("/company/client/business/task/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: Long): Result<DeleteTaskRes>

    @GET("/company/client/business/task/{taskId}")
    suspend fun fetchTaskById(@Path("taskId") taskId: Long): Result<TaskRes>

    @GET("/company/{companyId}/client/business/tasks")
    suspend fun fetchTaskList(
        @Path("companyId") companyId: Long,
        @Query("date") date: String,
        @Query("type") type: String
    ): Result<TaskListRes>

    @GET("/company/client/{clientId}/business/task/statistic")
    suspend fun fetchTaskGraph(@Path("clientId") clientId: Long): Result<TaskGraphRes>

    @GET("/company/client/business/{businessId}/tasks")
    suspend fun fetchTaskListByDate(
        @Path("businessId") businessId: Long,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int?,
        @Query("categoryId") categoryId: Long?
    ): Result<TaskListRes>
}