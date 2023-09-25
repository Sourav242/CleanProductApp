package com.souravroy.cleanproductapp.modules.product.di.repository

import com.souravroy.cleanproductapp.modules.product.repository.ProductRepository
import com.souravroy.cleanproductapp.modules.product.repository.datasource.local.ProductLocalDataSource
import com.souravroy.cleanproductapp.modules.product.repository.datasource.remote.ProductRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Author: Sourav PC
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

@Module
@InstallIn(SingletonComponent::class)
class ProductRepositoryModule {
	@Singleton
	@Provides
	fun providesRepository(
		remote: ProductRemoteDataSource,
		local: ProductLocalDataSource
	) = ProductRepository(remote, local)
}