package com.souravroy.cleanproductapp.modules.product.utils.connection

import kotlinx.coroutines.flow.Flow

/**
 * @Author: Sourav PC
 * @Date: 22-09-2023
 */

interface ConnectivityObserver {
	fun observe(): Flow<Status>

	sealed class Status(
		val status: Boolean,
		val networkStatus: String
	) {
		object Available : Status(true, "Available")
		object Unavailable : Status(false, "Unavailable")
		object Losing : Status(false, "Losing")
		object Lost : Status(false, "Lost")
		object Null : Status(false, "Null")
	}
}