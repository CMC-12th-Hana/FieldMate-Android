package com.hana.fieldmate.data.remote.datasource

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.api.TaskCategoryService
import com.hana.fieldmate.data.remote.model.request.CreateTaskCategoryReq
import com.hana.fieldmate.data.remote.model.request.UpdateTaskCategoryReq
import com.hana.fieldmate.data.remote.model.response.CreateTaskCategoryRes
import com.hana.fieldmate.data.remote.model.response.DeleteTaskCategoryListRes
import com.hana.fieldmate.data.remote.model.response.TaskCategoryListRes
import com.hana.fieldmate.data.remote.model.response.UpdateTaskCategoryRes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TaskCategoryDataSource @Inject constructor(
    private val taskCategoryService: TaskCategoryService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun createTaskCategory(
        companyId: Long,
        createTaskCategoryReq: CreateTaskCategoryReq
    ): Flow<ResultWrapper<CreateTaskCategoryRes>> = flow {
        taskCategoryService.createTaskCategory(companyId, createTaskCategoryReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun updateTaskCategory(
        categoryId: Long,
        updateTaskCategoryReq: UpdateTaskCategoryReq
    ): Flow<ResultWrapper<UpdateTaskCategoryRes>> = flow {
        taskCategoryService.updateTaskCategory(categoryId, updateTaskCategoryReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun fetchTaskCategoryList(
        companyId: Long
    ): Flow<ResultWrapper<TaskCategoryListRes>> = flow {
        taskCategoryService.fetchTaskCategoryList(companyId).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun deleteTaskCategory(categoryIdList: List<Long>): Flow<ResultWrapper<DeleteTaskCategoryListRes>> =
        flow {
            taskCategoryService.deleteTaskCategory(categoryIdList).onSuccess {
                emit(ResultWrapper.Success(it))
            }.onFailure {
                emit(ResultWrapper.Error(it.message!!))
            }
        }.flowOn(ioDispatcher)
}