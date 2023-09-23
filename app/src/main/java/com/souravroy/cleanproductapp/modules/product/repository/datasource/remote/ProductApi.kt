package com.souravroy.cleanproductapp.modules.product.repository.datasource.remote

import com.souravroy.cleanproductapp.base.model.ResponseModel
import com.souravroy.cleanproductapp.modules.product.model.Product
import com.souravroy.cleanproductapp.modules.product.view.screens.NavigationRoutes
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @Author: Sourav PC
 * @Date: 21-09-2023
 */

interface ProductApi {
	@GET(NavigationRoutes.PRODUCT_HOME)
	suspend fun getProducts(): ResponseModel<List<Product>>

	@GET("/products/search")
	suspend fun getProducts(@Query("q") search: String): ResponseModel<List<Product>>

	@GET(NavigationRoutes.PRODUCT_DETAILS)
	suspend fun getProduct(@Path("id") id: Int): Product

	@GET("/products/categories")
	suspend fun getProductCategories(): List<String>

	@GET("/products/categories/{category}")
	suspend fun getProductsByCategory(@Path("category") category: String): List<Product>
}