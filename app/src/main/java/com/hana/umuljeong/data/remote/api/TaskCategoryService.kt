package com.hana.umuljeong.data.remote.api

import com.hana.umuljeong.data.remote.model.request.CreateTaskCategoryReq
import com.hana.umuljeong.data.remote.model.request.DeleteTaskCategoryListReq
import com.hana.umuljeong.data.remote.model.request.UpdateTaskCategoryReq
import com.hana.umuljeong.data.remote.model.response.CreateTaskCategoryRes
import com.hana.umuljeong.data.remote.model.response.DeleteTaskCategoryListRes
import com.hana.umuljeong.data.remote.model.response.TaskCategoryListRes
import com.hana.umuljeong.data.remote.model.response.UpdateTaskCategoryRes
import retrofit2.http.*

interface TaskCategoryService {
    @GET("/company/{companyId}/client/business/task/category")
    suspend fun createTaskCategory(
        @Path("companyId") companyId: Long,
        @Body createTaskCategoryReq: CreateTaskCategoryReq
    ): Result<CreateTaskCategoryRes>

    @PATCH("/company/client/business/task/category/{categoryId}")
    suspend fun updateTaskCategory(
        @Path("categoryId") categoryId: Long,
        @Body updateCategoryReq: UpdateTaskCategoryReq
    ): Result<UpdateTaskCategoryRes>

    @GET("/company/{companyId}/client/business/task/categories")
    suspend fun fetchTaskCategoryList(
        @Path("companyId") companyId: Long
    ): Result<TaskCategoryListRes>

    @DELETE("/company/client/business/task/categories")
    suspend fun deleteTaskCategory(
        @Body deleteCategoryListReq: DeleteTaskCategoryListReq
    ): Result<DeleteTaskCategoryListRes>
}