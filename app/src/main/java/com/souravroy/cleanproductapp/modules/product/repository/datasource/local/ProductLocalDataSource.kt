package com.souravroy.cleanproductapp.modules.product.repository.datasource.local

import com.souravroy.cleanproductapp.modules.product.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * @Author: Sourav PC
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

interface ProductLocalDataSource {
	suspend fun getProducts(search: String? = null): Flow<List<Product>>
	suspend fun getProduct(id: Int): Flow<Product>
	suspend fun save(product: Product): Flow<Long>
	suspend fun remove(product: Product): Flow<Int>
}