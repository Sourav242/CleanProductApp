package com.souravroy.cleanproductapp.modules.product.data.repository.datasource.remote

import com.souravroy.cleanproductapp.modules.product.repository.datasource.remote.ProductApi
import com.souravroy.cleanproductapp.modules.product.repository.datasource.remote.ProductRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @Author: Sourav PC
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

class ProductRemoteDataSourceImpl @Inject constructor(
	private val productApi: ProductApi
) : ProductRemoteDataSource {
	override suspend fun getProducts(search: String?) = flow {
		emit(
			search?.let {
				productApi.getProducts(it)
			} ?: run {
				productApi.getProducts()
			}
		)
	}.flowOn(Dispatchers.IO)


	override suspend fun getProduct(id: Int) = flow {
		emit(
			productApi.getProduct(id)
		)
	}.flowOn(Dispatchers.IO)

	override suspend fun getProductCategories() = flow {
		emit(
			productApi.getProductCategories()
		)
	}.flowOn(Dispatchers.IO)

	override suspend fun getProductsByCategory(category: String) = flow {
		emit(
			productApi.getProductsByCategory(category)
		)
	}.flowOn(Dispatchers.IO)
}