package com.example.learningcurvemvvmrecipeapp.domain.data

data class DataState<out T>(
    val data: T? = null,
    val error: String? = null,
    val loading: Boolean = false
){
    companion object{

        fun <T> success(data: T): DataState<T>{
            return DataState(
                data = data // we are passing the value for data only
            )
        }

        fun <T> error(message: String): DataState<T>{
            return DataState(
                error = message // we are passing the error message only
            )
        }

        fun<T> loading(): DataState<T> = DataState(loading = true)
    }
}