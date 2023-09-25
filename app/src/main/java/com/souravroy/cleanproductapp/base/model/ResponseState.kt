package com.souravroy.cleanproductapp.base.model

/**
 * @Author: Sourav PC
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

sealed class ResponseState<T>(
	val data: T? = null,
	val error: Throwable? = null
) {
	class Success<T>(data: T) : ResponseState<T>(data = data)
	class Failure<T>(error: Throwable) : ResponseState<T>(error = error)
	class Loading<T> : ResponseState<T>()
	class LoadingWithData<T>(data: T) : ResponseState<T>(data = data)
	class Empty<T>(data: T) : ResponseState<T>(data)
	class Null<T> : ResponseState<T>()
}