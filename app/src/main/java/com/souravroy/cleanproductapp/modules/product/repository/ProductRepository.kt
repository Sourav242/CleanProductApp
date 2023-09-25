package com.souravroy.cleanproductapp.modules.product.repository

import com.souravroy.cleanproductapp.base.repository.BaseRepository
import com.souravroy.cleanproductapp.modules.product.repository.datasource.local.ProductLocalDataSource
import com.souravroy.cleanproductapp.modules.product.repository.datasource.remote.ProductRemoteDataSource
import javax.inject.Inject

/**
 * @Author: Sourav PC
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

data class ProductRepository @Inject constructor(
	val remote: ProductRemoteDataSource,
	val local: ProductLocalDataSource
) : BaseRepository()