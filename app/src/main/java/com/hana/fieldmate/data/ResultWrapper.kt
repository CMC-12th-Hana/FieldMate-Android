package com.hana.fieldmate.data

sealed class ResultWrapper<out R> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val error: ErrorType) : ResultWrapper<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[error=$error]"
        }
    }
}

sealed class ErrorType {
    data class JwtExpired(val errorMessage: String) : ErrorType()
    data class General(val errorMessage: String) : ErrorType()
}
