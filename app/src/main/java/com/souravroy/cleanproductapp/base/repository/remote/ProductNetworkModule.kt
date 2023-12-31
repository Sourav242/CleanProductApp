package com.souravroy.cleanproductapp.base.repository.remote

import com.souravroy.cleanproductapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

@Module
@InstallIn(SingletonComponent::class)
class ProductNetworkModule {
	@Singleton
	@Provides
	fun providesRetrofitBuilder(): Retrofit = Retrofit.Builder()
		.baseUrl(BuildConfig.BASE_URL)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
}