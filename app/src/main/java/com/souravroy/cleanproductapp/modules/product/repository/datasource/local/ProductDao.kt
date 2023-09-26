package com.souravroy.cleanproductapp.modules.product.repository.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.souravroy.cleanproductapp.modules.product.model.Product

/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

@Dao
interface ProductDao {
	@Query("SELECT * FROM product")
	fun getProducts(): List<Product>

	@Query(
		"SELECT * FROM product WHERE " +
				"category LIKE '%' || :search || '%' " +
				"OR title LIKE '%' || :search || '%' " +
				"OR brand LIKE '%' || :search || '%' " +
				"OR description LIKE '%' || :search || '%' "
	)
	fun getProducts(search: String? = null): List<Product>

	@Query("SELECT * FROM product WHERE id = :id")
	fun getProduct(id: Int): Product

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun save(product: Product): Long

	@Delete
	fun remove(product: Product): Int
}