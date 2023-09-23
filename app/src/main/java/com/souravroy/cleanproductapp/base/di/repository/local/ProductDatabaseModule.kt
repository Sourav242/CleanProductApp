package com.souravroy.cleanproductapp.base.di.repository.local

import android.content.Context
import androidx.room.Room
import com.souravroy.cleanproductapp.base.repository.local.ProductDatabase
import com.souravroy.cleanproductapp.modules.product.utils.ProductConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Author: Sourav PC
 * @Date: 21-09-2023
 */

@Module
@InstallIn(SingletonComponent::class)
class ProductDatabaseModule {
	@Singleton
	@Provides
	fun providesRoomDB(
		@ApplicationContext context: Context
	): ProductDatabase = Room.databaseBuilder(
		context,
		ProductDatabase::class.java, ProductConstants.PRODUCT_TABLE_NAME
	).build()
}