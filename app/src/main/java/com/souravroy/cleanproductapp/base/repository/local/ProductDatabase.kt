package com.souravroy.cleanproductapp.base.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.souravroy.cleanproductapp.modules.product.model.Product
import com.souravroy.cleanproductapp.modules.product.repository.datasource.local.ProductDao
import com.souravroy.cleanproductapp.modules.product.utils.ProductConstants

/**
 * @Author: Sourav PC
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */
@Database(entities = [Product::class], version = ProductConstants.PRODUCT_TABLE_VERSION)
@TypeConverters(Converters::class)
abstract class ProductDatabase : RoomDatabase() {
	abstract fun productDao(): ProductDao
}