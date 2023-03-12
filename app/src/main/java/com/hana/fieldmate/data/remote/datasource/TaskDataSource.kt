package com.hana.fieldmate.data.remote.datasource

import android.util.Log
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.api.TaskService
import com.hana.fieldmate.data.remote.model.response.CreateTaskRes
import com.hana.fieldmate.data.remote.model.response.DeleteTaskRes
import com.hana.fieldmate.data.remote.model.response.TaskListRes
import com.hana.fieldmate.data.remote.model.response.TaskRes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class TaskDataSource @Inject constructor(
    private val taskService: TaskService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun createTask(
        data: HashMap<String, RequestBody>,
        images: List<MultipartBody.Part>
    ): Flow<ResultWrapper<CreateTaskRes>> = flow {
        taskService.createTask(data, images).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun deleteTaskBy(taskId: Long): Flow<ResultWrapper<DeleteTaskRes>> = flow {
        taskService.deleteTask(taskId).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun fetchTaskById(taskId: Long): Flow<ResultWrapper<TaskRes>> = flow {
        taskService.fetchTaskById(taskId).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun fetchTaskList(
        companyId: Long,
        date: String,
        type: String
    ): Flow<ResultWrapper<TaskListRes>> = flow {
        taskService.fetchTaskList(companyId, date, type).onSuccess {
            Log.d("업무 목록", it.toString())
            emit(ResultWrapper.Success(it))
        }.onFailure {
            Log.d("업무 목록", it.toString())
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)
}