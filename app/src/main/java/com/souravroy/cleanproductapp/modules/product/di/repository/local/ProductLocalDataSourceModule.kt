package com.souravroy.cleanproductapp.modules.product.di.repository.local

import com.souravroy.cleanproductapp.base.repository.local.ProductDatabase
import com.souravroy.cleanproductapp.modules.product.data.repository.datasource.local.ProductLocalDataSourceImpl
import com.souravroy.cleanproductapp.modules.product.repository.datasource.local.ProductDao
import com.souravroy.cleanproductapp.modules.product.repository.datasource.local.ProductLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

@Module
@InstallIn(SingletonComponent::class)
class ProductLocalDataSourceModule {
	@Singleton
	@Provides
	fun providesProductDao(productDatabase: ProductDatabase): ProductDao =
		productDatabase.productDao()

	@Singleton
	@Provides
	fun providesProductLocalDataSource(
		productDao: ProductDao
	): ProductLocalDataSource = ProductLocalDataSourceImpl(productDao)
}