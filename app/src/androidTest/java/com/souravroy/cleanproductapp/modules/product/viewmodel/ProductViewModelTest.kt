@file:OptIn(ExperimentalCoroutinesApi::class)
package com.souravroy.cleanproductapp.modules.product.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.souravroy.cleanproductapp.base.model.ResponseModel
import com.souravroy.cleanproductapp.base.model.ResponseState
import com.souravroy.cleanproductapp.base.test.MainCoroutineRule
import com.souravroy.cleanproductapp.modules.product.model.Product
import com.souravroy.cleanproductapp.modules.product.repository.ProductRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 24-09-2023
 */

@RunWith(JUnit4::class)
class ProductViewModelTest {

	@get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

	@get:Rule
	val mainCoroutineRule = MainCoroutineRule()

    @RelaxedMockK
    private lateinit var repository: ProductRepository

    private val context: Context = ApplicationProvider.getApplicationContext()

	private lateinit var viewModel: ProductViewModel

	@Before
	fun setup() {
		MockKAnnotations.init(this, relaxed = true)
        viewModel = ProductViewModel(repository, context)
    }

    @After
    fun tearDown() {
        clearAllMocks()
	}

	@Test
    fun getProductsShouldEmitSuccess() = runTest {
		val mockResponse = Product(
            0, "", "", 0.0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.remote.getProducts()
		} returns flowOf(ResponseModel(listOf(mockResponse), 0, 0, 0))

		viewModel.getProducts("lap")

		assertEquals(
            mockResponse.category,
			viewModel.productsResponseState.value.data?.get(0)?.category
		)
	}

	@Test
    fun getProductShouldEmitSuccess() = runTest {
		val mockResponse = Product(
            1, "", "", 0.0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.remote.getProduct(1)
		} returns flowOf(mockResponse)

		viewModel.getProduct(1)

		assertEquals(mockResponse.id, viewModel.productResponseState.value.data?.id)
	}

	@Test
    fun getSavedProductsShouldEmitSuccess() = runTest {
		val mockResponse = Product(
            0, "", "", 0.0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.local.getProducts()
		} returns flowOf(listOf(mockResponse))

		viewModel.getSavedProducts("lap")

		assertEquals(
            mockResponse.category,
			viewModel.productsSavedResponseState.value.data?.get(0)?.category
		)
	}

	@Test
    fun getSavedProductShouldEmitSuccess() = runTest {
		val mockResponse = Product(
            1, "", "", 0.0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.local.getProduct(1)
		} returns flowOf(mockResponse)

		viewModel.getSavedProduct(1)

		assertEquals(mockResponse.id, viewModel.productSavedResponseState.value.data?.id)
	}

	@Test
    fun saveShouldEmitSuccess() = runTest {
		val mockRequest = Product(
            1, "", "", 0.0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.local.save(mockRequest)
		} returns flowOf(1L)

		viewModel.save(mockRequest)

        assertEquals(ResponseState.Success(mockRequest), viewModel.productSavedState.value)
	}

	@Test
    fun removeShouldEmitSuccess() = runTest {
		val mockRequest = Product(
            1, "", "", 0.0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.local.remove(mockRequest)
		} returns flowOf(1)

		viewModel.remove(mockRequest)

        assertEquals(ResponseState.Success(mockRequest), viewModel.productSavedState.value)
	}
}