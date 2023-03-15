package com.hana.fieldmate.data.remote.datasource

import android.util.Log
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.api.TaskService
import com.hana.fieldmate.data.remote.model.response.*
import com.hana.fieldmate.network.TaskTypeQuery
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

    fun updateTask(
        taskId: Long,
        data: HashMap<String, RequestBody>,
        addImageList: List<MultipartBody.Part>
    ): Flow<ResultWrapper<UpdateTaskRes>> = flow {
        taskService.updateTask(taskId, data, addImageList).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            Log.d("이게 왜?", "난 실패작인거야???")
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
        type: TaskTypeQuery
    ): Flow<ResultWrapper<TaskListRes>> = flow {
        taskService.fetchTaskList(companyId, date, type).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun fetchTaskGraph(clientId: Long): Flow<ResultWrapper<TaskStatisticListRes>> = flow {
        taskService.fetchTaskGraph(clientId).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun fetchTaskListByDate(
        businessId: Long,
        year: Int,
        month: Int,
        day: Int?,
        categoryId: Long?
    ): Flow<ResultWrapper<TaskListRes>> = flow {
        taskService.fetchTaskListByDate(businessId, year, month, day, categoryId).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)
}