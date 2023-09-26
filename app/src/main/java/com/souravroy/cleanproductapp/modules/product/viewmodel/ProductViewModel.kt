package com.souravroy.cleanproductapp.modules.product.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.souravroy.cleanproductapp.base.model.ResponseModel
import com.souravroy.cleanproductapp.base.model.ResponseState
import com.souravroy.cleanproductapp.base.viewmodel.BaseViewModel
import com.souravroy.cleanproductapp.modules.product.model.Product
import com.souravroy.cleanproductapp.modules.product.model.ProductUiModel
import com.souravroy.cleanproductapp.modules.product.repository.ProductRepository
import com.souravroy.cleanproductapp.modules.product.utils.connection.ConnectivityObserver
import com.souravroy.cleanproductapp.modules.product.utils.connection.NetworkConnectivityObserver
import com.souravroy.cleanproductapp.modules.product.utils.validateSearchText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

@HiltViewModel
class ProductViewModel @Inject constructor(
	val repository: ProductRepository,
	@ApplicationContext context: Context
) : BaseViewModel() {

	val productUiModel = ProductUiModel()

	val connectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(context)

	private val _productsResponseState: MutableStateFlow<ResponseState<List<Product>>> =
		MutableStateFlow(ResponseState.LoadingWithData(listOf()))
	val productsResponseState: StateFlow<ResponseState<List<Product>>> = this._productsResponseState

	private val _productResponseState: MutableStateFlow<ResponseState<Product?>> =
		MutableStateFlow(ResponseState.LoadingWithData(data = null))
	val productResponseState: StateFlow<ResponseState<Product?>> = _productResponseState

	private val _productsSavedResponseState: MutableStateFlow<ResponseState<List<Product>>> =
		MutableStateFlow(ResponseState.LoadingWithData(listOf()))
	val productsSavedResponseState: StateFlow<ResponseState<List<Product>>> =
		this._productsSavedResponseState

	private val _productSavedResponseState: MutableStateFlow<ResponseState<Product?>> =
		MutableStateFlow(ResponseState.LoadingWithData(data = null))
	val productSavedResponseState: StateFlow<ResponseState<Product?>> = _productSavedResponseState

	private val _productSavedState: MutableStateFlow<ResponseState<Product?>> =
		MutableStateFlow(ResponseState.LoadingWithData(data = null))
	val productSavedState: StateFlow<ResponseState<Product?>> = _productSavedState

	init {
		getProducts()
		getSavedProducts()
	}

	fun searchProducts(search: String) {
		if (search.validateSearchText()) {
			productUiModel.searchText = search
			if (productUiModel.remote)
				getProducts(search)
			else
				getSavedProducts(search)
		}
	}

	fun getProducts(search: String? = null) = viewModelScope.launch {
		_productsResponseState.value = ResponseState.Loading()
		repository.remote.getProducts(search)
			.catch { error ->
				_productsResponseState.value = ResponseState.Failure(error)
				Log.e("#product - error", error.message.toString())
			}.stateIn(
				viewModelScope,
				SharingStarted.Eagerly,
				_productsResponseState.value.data?.let {
					ResponseModel(it, 0, 0, 0)
				}
			).collectLatest {
				it?.let {
					_productsResponseState.value = ResponseState.Success(it.products)
					Log.e("#product - response", "Products - $it")
				}
			}
	}

	fun getProduct(id: Int) = viewModelScope.launch {
		_productResponseState.value = ResponseState.Loading()
		repository.remote.getProduct(id)
			.catch { error ->
				_productResponseState.value = ResponseState.Failure(error)
				Log.e("#product - error", error.message.toString())
			}.stateIn(
				viewModelScope,
				SharingStarted.Eagerly,
				_productsResponseState.value.data?.find {
					it.id == id
				}
			).collectLatest {
				it?.let {
					_productResponseState.value = ResponseState.Success(it)
					Log.e("#product - response", "Product [$id] - $it")
				}
			}
	}

	fun getSavedProducts(search: String? = null) = viewModelScope.launch {
		_productsSavedResponseState.value = ResponseState.Loading()
		repository.local.getProducts(search)
			.catch { error ->
				_productsSavedResponseState.value = ResponseState.Failure(error)
				Log.e("#product - error", error.message.toString())
			}.stateIn(
				viewModelScope,
				SharingStarted.Eagerly,
				_productsSavedResponseState.value.data
			).collectLatest {
				it?.let {
					_productsSavedResponseState.value = ResponseState.Success(it)
					Log.e("#product - response", "Saved Products - $it")
				}
			}
	}

	fun getSavedProduct(id: Int) = viewModelScope.launch {
		_productSavedResponseState.value = ResponseState.Loading()
		repository.local.getProduct(id)
			.catch { error ->
				_productSavedResponseState.value = ResponseState.Failure(error)
				Log.e("#product - error", error.message.toString())
			}.stateIn(
				viewModelScope,
				SharingStarted.Eagerly,
				_productsSavedResponseState.value.data?.find {
					it.id == id
				}
			).collectLatest {
				it?.let {
					_productSavedResponseState.value = ResponseState.Success(it)
					Log.e("#product - response", "Saved Product [$id] - $it")
				}
			}
	}

	fun save(product: Product) = viewModelScope.launch {
		_productSavedState.value = ResponseState.LoadingWithData(product)
		repository.local.save(product)
			.catch { error ->
				_productSavedState.value = ResponseState.Failure(error)
				Log.e("#product - error", error.message.toString())
			}.stateIn(
				viewModelScope,
				SharingStarted.Eagerly,
				0L
			).collect {
				if (it > 0) {
					_productSavedState.value = ResponseState.Success(product)
					Log.e("#product - response", "Product Saved [$product.id] - $it")
				}
			}
	}

	fun remove(product: Product) = viewModelScope.launch {
		_productSavedState.value = ResponseState.LoadingWithData(product)
		repository.local.remove(product)
			.catch { error ->
				_productSavedState.value = ResponseState.Failure(error)
				Log.e("#product - error", error.message.toString())
			}.stateIn(
				viewModelScope,
				SharingStarted.Eagerly,
				0
			).collect {
				if (it > 0) {
					_productSavedState.value = ResponseState.Success(product)
					Log.e("#product - response", "Product Removed [$product.id] - $it")
				}
			}
	}

	fun reInitializeProductSavedState() {
		_productSavedState.value = ResponseState.LoadingWithData(data = null)
	}
}