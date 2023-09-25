package com.souravroy.cleanproductapp.modules.product.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.souravroy.cleanproductapp.base.model.ResponseModel
import com.souravroy.cleanproductapp.base.test.BaseTest
import com.souravroy.cleanproductapp.modules.product.model.Product
import com.souravroy.cleanproductapp.modules.product.repository.ProductRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

/**
 * @Author: Sourav PC
 * @Email: 1994sourav@gmail.com
 * @Date: 24-09-2023
 */

class ProductViewModelTest : BaseTest() {

	@get:Rule
	val instanceTaskExecutor = InstantTaskExecutorRule()

	@get:Rule
	val mainCoroutineRule = MainCoroutineRule()

	private var repository: ProductRepository = mockk(relaxed = true)

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

	private lateinit var viewModel: ProductViewModel

	@BeforeEach
	override fun beforeEach() {
		super.beforeEach()
	}

	@AfterEach
	override fun afterEach() {
		super.afterEach()
		clearAllMocks()
	}

	@Before
	fun setup() {
		MockKAnnotations.init(this, relaxed = true)
        viewModel = spyk(ProductViewModel(repository, context))
	}

	@Test
	fun testGetProducts() = runTest {
		val mockResponse = Product(
			0, "", "", 0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.remote.getProducts()
		} returns flowOf(ResponseModel(listOf(mockResponse), 0, 0, 0))

		viewModel.getProducts("lap")

		assertEquals(
			listOf(mockResponse)[0].category,
			viewModel.productsResponseState.value.data?.get(0)?.category
		)
	}

	@Test
	fun testGetProduct() = runTest {
		val mockResponse = Product(
			1, "", "", 0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.remote.getProduct(1)
		} returns flowOf(mockResponse)

		viewModel.getProduct(1)

		assertEquals(mockResponse.id, viewModel.productResponseState.value.data?.id)
	}

	@Test
	fun testGetSavedProducts() = runTest {
		val mockResponse = Product(
			0, "", "", 0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.local.getProducts()
		} returns flowOf(listOf(mockResponse))

		viewModel.getSavedProducts("lap")

		assertEquals(
			listOf(mockResponse)[0].category,
			viewModel.productsSavedResponseState.value.data?.get(0)?.category
		)
	}

	@Test
	fun testGetSavedProduct() = runTest {
		val mockResponse = Product(
			1, "", "", 0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.local.getProduct(1)
		} returns flowOf(mockResponse)

		viewModel.getSavedProduct(1)

		assertEquals(mockResponse.id, viewModel.productSavedResponseState.value.data?.id)
	}

	@Test
	fun testSave() = runTest {
		val mockRequest = Product(
			1, "", "", 0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.local.save(mockRequest)
		} returns flowOf(1L)

		viewModel.save(mockRequest)

		assertEquals(1L, viewModel.productSavedState.value.data)
	}

	@Test
	fun testRemove() = runTest {
		val mockRequest = Product(
			1, "", "", 0, 0.0, 0.0, 0, "", "laptops", "", listOf()
		)
		coEvery {
			repository.local.remove(mockRequest)
		} returns flowOf(1)

		viewModel.remove(mockRequest)

		assertEquals(1, viewModel.productSavedState.value.data)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}
}