package com.souravroy.cleanproductapp.modules.product.di.repository.remote

import com.souravroy.cleanproductapp.modules.product.data.repository.datasource.remote.ProductRemoteDataSourceImpl
import com.souravroy.cleanproductapp.modules.product.repository.datasource.remote.ProductApi
import com.souravroy.cleanproductapp.modules.product.repository.datasource.remote.ProductRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

@Module
@InstallIn(SingletonComponent::class)
class ProductRemoteDataSourceModule {
	@Singleton
	@Provides
	fun providesProductApi(retrofit: Retrofit): ProductApi =
		retrofit.create(ProductApi::class.java)

	@Singleton
	@Provides
	fun providesProductRemoteDataSource(
		productApi: ProductApi
	): ProductRemoteDataSource = ProductRemoteDataSourceImpl(productApi)
}