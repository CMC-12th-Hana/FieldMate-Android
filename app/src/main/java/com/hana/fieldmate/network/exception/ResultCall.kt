package com.hana.fieldmate.network.exception

import android.util.Log
import com.google.gson.Gson
import com.hana.fieldmate.data.remote.model.response.ErrorRes
import com.hana.fieldmate.util.*
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ResultCall<T>(private val delegate: Call<T>) : Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(
            object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(
                                response.code(),
                                Result.success(response.body()!!)
                            )
                        )
                    } else {
                        val gson = Gson()
                        val errorResponse =
                            gson.fromJson(response.errorBody()?.string(), ErrorRes::class.java)

                        val errorMessage = if (errorResponse.errorCode == BAD_REQUEST) {
                            BAD_REQUEST_ERROR_MESSAGE
                        } else if (errorResponse.errorCode == JWT_TOKEN_NOT_FOUND) {
                            TOKEN_EXPIRED_MESSAGE
                        } else if (errorResponse.errorCode == JWT_ACCESS_TOKEN_EXPIRED) {
                            TOKEN_EXPIRED_MESSAGE
                        } else if (errorResponse.errorCode == JWT_REFRESH_TOKEN_EXPIRED) {
                            TOKEN_EXPIRED_MESSAGE
                        } else {
                            errorResponse.message
                        }

                        Log.d("에러 메시지", errorResponse.toString())
                        Log.d("예외 처리 메시지", errorMessage)

                        callback.onResponse(
                            this@ResultCall,
                            Response.success(
                                Result.failure(
                                    Exception(
                                        errorMessage
                                    )
                                )
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    val errorMessage = when (t) {
                        is IOException -> NETWORK_CONNECTION_ERROR_MESSAGE
                        else -> t.localizedMessage
                    }

                    callback.onResponse(
                        this@ResultCall,
                        Response.success(Result.failure(Exception(errorMessage)))
                    )
                }
            }
        )
    }

    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun execute(): Response<Result<T>> {
        return Response.success(Result.success(delegate.execute().body()!!))
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return delegate.isCanceled
    }

    override fun clone(): Call<Result<T>> {
        return ResultCall(delegate.clone())
    }

    override fun request(): Request {
        return delegate.request()
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }

}