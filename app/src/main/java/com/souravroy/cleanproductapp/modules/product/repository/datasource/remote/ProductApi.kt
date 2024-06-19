package com.souravroy.cleanproductapp.modules.product.repository.datasource.remote

import com.souravroy.cleanproductapp.base.model.ResponseModel
import com.souravroy.cleanproductapp.modules.product.model.Product
import com.souravroy.cleanproductapp.modules.product.view.screens.NavigationRoutes
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

interface ProductApi {
    @GET(NavigationRoutes.PRODUCT_HOME)
    suspend fun getProducts(): ResponseModel<List<Product>>

    @GET(NavigationRoutes.PRODUCT_SEARCH)
    suspend fun getProducts(
        @Query(
            NavigationRoutes.QueryParams.SEARCH
        ) search: String
    ): ResponseModel<List<Product>>

    @GET(NavigationRoutes.PRODUCT_DETAILS)
    suspend fun getProduct(@Path(NavigationRoutes.QueryParams.ID) id: Int): Product

    @GET(NavigationRoutes.PRODUCT_CATEGORIES)
    suspend fun getProductCategories(): List<String>

    @GET(NavigationRoutes.PRODUCT_CATEGORY)
    suspend fun getProductsByCategory(
        @Path(
            NavigationRoutes.QueryParams.CATEGORY
        ) category: String
    ): List<Product>
}