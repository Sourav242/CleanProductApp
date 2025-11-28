package com.souravroy.cleanproductapp.modules.product.repository

import app.cash.turbine.test
import com.souravroy.cleanproductapp.base.model.ResponseModel
import com.souravroy.cleanproductapp.modules.product.model.Product
import com.souravroy.cleanproductapp.modules.product.repository.datasource.local.ProductLocalDataSource
import com.souravroy.cleanproductapp.modules.product.repository.datasource.remote.ProductRemoteDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProductRepositoryTest {
    @RelaxedMockK
    private lateinit var remote: ProductRemoteDataSource

    @RelaxedMockK
    private lateinit var local: ProductLocalDataSource

    private lateinit var repository: ProductRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = ProductRepository(
            remote = remote,
            local = local
        )
    }

    @Test
    fun getProductsShouldEmitSuccess() = runTest {
        // GIVEN
        val products = listOf(
            Product(
                id = 1,
                title = "iPhone 9",
                description = "An apple mobile which is nothing like apple",
                price = 549.0,
                discountPercentage = 12.96,
                rating = 4.69,
                stock = 94,
                brand = "Apple",
                category = "smartphones",
                thumbnail = "...",
                images = listOf()
            )
        )
        coEvery { remote.getProducts() } returns flow { emit(ResponseModel(products, 1, 0, 1)) }

        // WHEN
        val result = repository.remote.getProducts()

        // THEN
        result.test {
            val emission = awaitItem()
            assert(emission.products.isNotEmpty())
            awaitComplete()
        }
    }
}
