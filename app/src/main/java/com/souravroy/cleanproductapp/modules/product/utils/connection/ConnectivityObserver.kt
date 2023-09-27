package com.souravroy.cleanproductapp.modules.product.utils.connection

import kotlinx.coroutines.flow.Flow

/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 22-09-2023
 */

interface ConnectivityObserver {
	fun observe(): Flow<NetworkStatus>

	sealed class NetworkStatus(
		val status: Boolean,
		val networkStatus: String? = null
	) {
		object Available : NetworkStatus(true, "Available")
		object Unavailable : NetworkStatus(false, "Unavailable")
		object Losing : NetworkStatus(false, "Losing")
		object Lost : NetworkStatus(false, "Lost")
		object Null : NetworkStatus(false)
	}
}