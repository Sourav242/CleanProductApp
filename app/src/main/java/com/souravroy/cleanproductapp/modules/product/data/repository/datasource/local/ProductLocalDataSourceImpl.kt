package com.souravroy.cleanproductapp.modules.product.data.repository.datasource.local

import com.souravroy.cleanproductapp.modules.product.model.Product
import com.souravroy.cleanproductapp.modules.product.repository.datasource.local.ProductDao
import com.souravroy.cleanproductapp.modules.product.repository.datasource.local.ProductLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @Author: Sourav PC
 * @Date: 21-09-2023
 */

class ProductLocalDataSourceImpl @Inject constructor(
	private val productDao: ProductDao
) : ProductLocalDataSource {

	override suspend fun getProducts(search: String?) = flow {
		emit(
			search?.let {
				productDao.getProducts(it)
			} ?: run {
				productDao.getProducts()
			}
		)
	}.flowOn(Dispatchers.IO)

	override suspend fun getProduct(id: Int) = flow {
		emit(
			productDao.getProduct(id)
		)
	}.flowOn(Dispatchers.IO)

	override suspend fun save(product: Product) = flow {
		emit(
			productDao.save(product)
		)
	}.flowOn(Dispatchers.IO)

	override suspend fun remove(product: Product) = flow {
		emit(
			productDao.remove(product)
		)
	}.flowOn(Dispatchers.IO)
}