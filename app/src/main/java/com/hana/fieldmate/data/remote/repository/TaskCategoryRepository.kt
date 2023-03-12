package com.hana.fieldmate.data.remote.repository

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.datasource.TaskCategoryDataSource
import com.hana.fieldmate.data.remote.model.request.CreateTaskCategoryReq
import com.hana.fieldmate.data.remote.model.request.UpdateTaskCategoryReq
import com.hana.fieldmate.data.remote.model.response.CreateTaskCategoryRes
import com.hana.fieldmate.data.remote.model.response.DeleteTaskCategoryListRes
import com.hana.fieldmate.data.remote.model.response.TaskCategoryListRes
import com.hana.fieldmate.data.remote.model.response.UpdateTaskCategoryRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskCategoryRepository @Inject constructor(
    private val taskCategoryDataSource: TaskCategoryDataSource
) {
    fun createTaskCategory(
        companyId: Long,
        name: String,
        color: String
    ): Flow<ResultWrapper<CreateTaskCategoryRes>> =
        taskCategoryDataSource.createTaskCategory(
            companyId, CreateTaskCategoryReq(name, color)
        )

    fun updateTaskCategory(
        categoryId: Long,
        name: String,
        color: String
    ): Flow<ResultWrapper<UpdateTaskCategoryRes>> =
        taskCategoryDataSource.updateTaskCategory(
            categoryId, UpdateTaskCategoryReq(name, color)
        )

    fun fetchTaskCategoryList(
        companyId: Long
    ): Flow<ResultWrapper<TaskCategoryListRes>> =
        taskCategoryDataSource.fetchTaskCategoryList(companyId)

    fun deleteTaskCategory(
        categoryIdList: List<Long>
    ): Flow<ResultWrapper<DeleteTaskCategoryListRes>> =
        taskCategoryDataSource.deleteTaskCategory(categoryIdList)
}