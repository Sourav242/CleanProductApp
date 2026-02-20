package com.souravroy.cleanproductapp.modules.product.viewmodel

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.souravroy.cleanproductapp.base.model.ResponseModel
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
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class ProductViewModelTest {

	@get:Rule
	val mainCoroutineRule = MainCoroutineRule()

    @RelaxedMockK
    private lateinit var repository: ProductRepository

    private val context: Context = ApplicationProvider.getApplicationContext()

	private lateinit var viewModel: ProductViewModel

	@BeforeEach
	fun setup() {
		MockKAnnotations.init(this, relaxed = true)
        viewModel = ProductViewModel(repository, context)
    }

	@AfterEach
    fun tearDown() {
        clearAllMocks()
	}

	@Test
	fun getProducts() = runTest {
		val mockProduct = Product(
            0, "", "", 0.0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		val mockResponse = ResponseModel(listOf(mockProduct), 0, 0, 0)
        val query = "lap"
		coEvery {
            repository.remote.getProducts(query)
		} returns flowOf(mockResponse)

        viewModel.getProducts(query)

		viewModel.productsResponseState.test {
			awaitItem() // Consume the initial Loading state
			val result = awaitItem() // Consume the success state
			assertEquals(
				mockProduct.category,
				result.data?.get(0)?.category
			)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun getProduct() = runTest {
		val mockResponse = Product(
            1, "", "", 0.0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.remote.getProduct(1)
		} returns flowOf(mockResponse)

		viewModel.getProduct(1)

		viewModel.productResponseState.test {
			awaitItem() // Consume the initial Loading state
			val result = awaitItem() // Consume the success state
			assertEquals(mockResponse.id, result.data?.id)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun getSavedProducts() = runTest {
		val mockProduct = Product(
            0, "", "", 0.0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
        val query = "lap"
		coEvery {
            repository.local.getProducts(query)
		} returns flowOf(listOf(mockProduct))

        viewModel.getSavedProducts(query)

		viewModel.productsSavedResponseState.test {
			awaitItem() // Consume the initial Loading state
			val result = awaitItem() // Consume the success state
			assertEquals(
				mockProduct.category,
				result.data?.get(0)?.category
			)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun getSavedProduct() = runTest {
		val mockResponse = Product(
            1, "", "", 0.0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.local.getProduct(1)
		} returns flowOf(mockResponse)

		viewModel.getSavedProduct(1)

		viewModel.productSavedResponseState.test {
			awaitItem() // Consume the initial Loading state
			val result = awaitItem() // Consume the success state
			assertEquals(mockResponse.id, result.data?.id)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun save() = runTest {
		val mockRequest = Product(
            1, "", "", 0.0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.local.save(mockRequest)
		} returns flowOf(1L)

		viewModel.save(mockRequest)

		viewModel.productSavedState.test {
			awaitItem() // Consume the initial Loading state
			val result = awaitItem() // Consume the success state
			assertEquals(
				mockRequest.id,
				result.data?.id
			)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun remove() = runTest {
		val mockRequest = Product(
            1, "", "", 0.0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.local.remove(mockRequest)
		} returns flowOf(1)

		viewModel.remove(mockRequest)

		viewModel.productSavedState.test {
			awaitItem() // Consume the initial Loading state
			val result = awaitItem() // Consume the success state
			assertEquals(
				mockRequest.id,
				result.data?.id
			)
			cancelAndIgnoreRemainingEvents()
		}
	}
}