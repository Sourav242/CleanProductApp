package com.souravroy.cleanproductapp.modules.product.repository.datasource.remote

import com.souravroy.cleanproductapp.base.model.ResponseModel
import com.souravroy.cleanproductapp.modules.product.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

interface ProductRemoteDataSource {
	suspend fun getProducts(search: String? = null): Flow<ResponseModel<List<Product>>>
	suspend fun getProduct(id: Int): Flow<Product>
	suspend fun getProductCategories(): Flow<List<String>>
	suspend fun getProductsByCategory(category: String): Flow<List<Product>>
}