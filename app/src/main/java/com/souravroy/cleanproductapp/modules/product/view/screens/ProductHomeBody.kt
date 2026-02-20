package com.souravroy.cleanproductapp.modules.product.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.souravroy.cleanproductapp.R
import com.souravroy.cleanproductapp.base.model.ResponseState
import com.souravroy.cleanproductapp.modules.product.model.Product
import com.souravroy.cleanproductapp.modules.product.utils.connection.ConnectivityObserver
import com.souravroy.cleanproductapp.modules.product.view.Greeting
import com.souravroy.cleanproductapp.modules.product.view.screens.NavigationRoutes.PRODUCT_DETAILS
import com.souravroy.cleanproductapp.modules.product.view.screens.NavigationRoutes.PRODUCT_HOME
import com.souravroy.cleanproductapp.modules.product.view.screens.NavigationRoutes.PRODUCT_SAVED
import com.souravroy.cleanproductapp.modules.product.viewmodel.ProductViewModel
import com.souravroy.cleanproductapp.ui.theme.CleanProductAppTheme

/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

private var savedProducts: List<Product> = listOf()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductHomeBody(viewModel: ProductViewModel, navController: NavController? = null) {
	val snackBarState = remember { SnackbarHostState() }
	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

	CleanProductAppTheme {
		// A surface container using the 'background' color from the theme
		Surface(
			modifier = Modifier.fillMaxSize(),
			color = MaterialTheme.colorScheme.background
		) {
			Scaffold(
				modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
				topBar = {
					TopAppBar(
						title = {
							Greeting()
						},
						actions = {
							IconButton(onClick = {
								navController?.navigate(PRODUCT_SAVED)
							}) {
								Icon(
									painter = painterResource(id = R.drawable.baseline_bookmark_24),
									contentDescription = stringResource(id = R.string.favourite)
								)
							}
						},
                        colors = TopAppBarDefaults.topAppBarColors(
							containerColor = MaterialTheme.colorScheme.primary,
							scrolledContainerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
						),
						scrollBehavior = scrollBehavior
					)
				},
				snackbarHost = {
					SnackbarHost(hostState = snackBarState)
				},
				floatingActionButton = {
					FloatingActionButton(
						onClick = { navController?.navigate(PRODUCT_SAVED) },
						containerColor = MaterialTheme.colorScheme.secondary,
						contentColor = MaterialTheme.colorScheme.onSecondary
					) {
						Icon(
							painter = painterResource(id = R.drawable.baseline_bookmark_24),
							contentDescription = stringResource(id = R.string.favourite)
						)
					}
				}
			) { contentPadding ->
				ProductBody(viewModel, navController, contentPadding, true, snackBarState)
			}
		}
	}
}

@Composable
fun ProductBody(
	viewModel: ProductViewModel,
	navController: NavController?,
	contentPadding: PaddingValues,
	remote: Boolean = true,
	snackBarState: SnackbarHostState
) {
	viewModel.productUiModel.remote = remote
	Column(
		modifier = Modifier
			.padding(contentPadding)
			.background(
				color = MaterialTheme.colorScheme.primary
			)
	) {

		val networkStatus by viewModel.connectivityObserver.observe()
			.collectAsStateWithLifecycle(
				initialValue = ConnectivityObserver.NetworkStatus.Null
			)

		when (networkStatus) {
			is ConnectivityObserver.NetworkStatus.Available -> {
				ShowProducts(
					viewModel,
					navController,
					snackBarState
				)
			}

			else -> {
				if (viewModel.productUiModel.remote) {
					ShowNetworkError(
						navController,
						snackBarState,
						networkStatus
					)
				} else {
					ShowProducts(
						viewModel,
						navController,
						snackBarState
					)
				}
			}
		}
	}
}

@Composable
fun ShowProducts(
	viewModel: ProductViewModel,
	navController: NavController?,
	snackBarState: SnackbarHostState
) {
	SearchBody(viewModel)

	Box(
		modifier = Modifier.background(
			color = MaterialTheme.colorScheme.background
		)
	) {
		val productsState = if (viewModel.productUiModel.remote)
			viewModel.productsResponseState.collectAsStateWithLifecycle()
		else
			viewModel.productsSavedResponseState.collectAsStateWithLifecycle()

		when (productsState.value) {
			is ResponseState.Success -> {
				productsState.value.data?.let {
					if (it.isEmpty()) {
						Text(
							modifier = Modifier
								.fillMaxSize()
								.padding(16.dp),
							text = stringResource(R.string.no_products_available),
							textAlign = TextAlign.Center
						)
					} else {
						Products(viewModel, it, navController, snackBarState)
					}
				}
			}

			is ResponseState.Failure -> {
				productsState.value.error?.let {
					ProductsErrorBody(it, snackBarState)
					Box(
						contentAlignment = Alignment.Center,
						modifier = Modifier.fillMaxSize()
					) {
						Button(
							onClick = {
								navController?.popBackStack()
								navController?.navigate(PRODUCT_HOME)
							},
							modifier = Modifier.padding(16.dp)
						) {
							Text(text = stringResource(id = R.string.retry))
						}
					}
				}
			}

			is ResponseState.Loading, is ResponseState.LoadingWithData -> {
				LoadingBody()
			}

			else -> {

			}
		}

		Decoration()
	}
}

@Composable
fun ShowNetworkError(
	navController: NavController?,
	snackBarState: SnackbarHostState,
	networkStatus: ConnectivityObserver.NetworkStatus
) {
	networkStatus.networkStatus?.let {
		ProductsErrorBody(
			Exception("Internet connection $it"),
			snackBarState
		)
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(
				color = MaterialTheme.colorScheme.background
			),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Button(
			onClick = { navController?.navigate(PRODUCT_SAVED) },
			modifier = Modifier.padding(16.dp)
		) {
			Text(
				text = stringResource(R.string.go_to_bookmarks),
				color = MaterialTheme.colorScheme.background
			)
		}
		Button(
			onClick = {
				navController?.popBackStack()
				navController?.navigate(PRODUCT_HOME)
			}
		) {
			Text(
				text = stringResource(R.string.retry),
				color = MaterialTheme.colorScheme.background
			)
		}
	}
}

@Composable
fun Decoration() {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height(16.dp)
			.background(
				brush = Brush.verticalGradient(
					colors = listOf(
						colorResource(id = R.color.shadow_color_1),
						colorResource(id = R.color.shadow_color_2),
						colorResource(id = android.R.color.transparent)
					)
				)
			)
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBody(viewModel: ProductViewModel) {
	val inputValue =
		remember { mutableStateOf(TextFieldValue(viewModel.productUiModel.searchText)) }
	Card(
		modifier = Modifier
			.padding(
				start = 16.dp,
				end = 16.dp,
				bottom = 16.dp
			)
			.background(
				color = MaterialTheme.colorScheme.primary
			),
		border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant
		),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 4.dp
		)
	) {
		Box {
			TextField(
				// below line is used to get
				// value of text field,
				value = inputValue.value,

				// below line is used to get value in text field
				// on value change in text field.
				onValueChange = {
					inputValue.value = it
					viewModel.searchProducts(it.text)
				},
				leadingIcon = {
					Icon(
                        Icons.Filled.Search,
						contentDescription = stringResource(R.string.search_products)
					)
				},
				trailingIcon = {
					if (inputValue.value.text.isNotEmpty()) {
						Icon(
                            Icons.Filled.Close,
							contentDescription = stringResource(R.string.clear_text),
							modifier = Modifier
								.clickable {
									val emptyText = ""
									inputValue.value = TextFieldValue(emptyText)
									viewModel.productUiModel.searchText = emptyText
									if (viewModel.productUiModel.remote)
										viewModel.getProducts()
									else
										viewModel.getSavedProducts(inputValue.value.text)
								}
						)
					}
				},

				colors = TextFieldDefaults.colors(
					focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
					unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
					disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
				),

				// below line is used to add placeholder
				// for our text field.
				placeholder = { Text(text = stringResource(R.string.search_products)) },

				// modifier is use to add padding
				// to our text field.
				modifier = Modifier.fillMaxWidth(),
				singleLine = true
			)
		}
	}
}

@Composable
fun Products(
	viewModel: ProductViewModel,
	products: List<Product>,
	navController: NavController?,
	snackBarState: SnackbarHostState
) {
	val scrollState: LazyGridState = rememberLazyGridState()
	LazyVerticalGrid(
		state = scrollState,
		modifier = Modifier.background(MaterialTheme.colorScheme.background),
		columns = GridCells.Fixed(2),
		contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		items(products) {
			ProductItem(it, viewModel, navController, snackBarState, Modifier)
		}
	}
}

@Composable
fun ProductItem(
	product: Product,
	viewModel: ProductViewModel,
	navController: NavController? = null,
	snackBarState: SnackbarHostState,
	modifier: Modifier
) {
	Card(
		modifier = modifier
			.clickable {
				navController?.navigate(
					PRODUCT_DETAILS.replace(
						oldValue = "{${NavigationRoutes.QueryParams.ID}}",
						newValue = product.id.toString()
					)
				)
			},
		border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surface
		),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 4.dp
		)
	) {
		ProductDetails(product, viewModel = viewModel, snackBarState = snackBarState)
	}
}

@Composable
fun Favourite(
	modifier: Modifier,
	product: Product,
	viewModel: ProductViewModel,
	snackBarState: SnackbarHostState
) {
	val savedState = viewModel.productSavedState.collectAsStateWithLifecycle()
	when (savedState.value) {
		is ResponseState.Success -> {
			viewModel.getSavedProducts()
			if (product != savedState.value.data) {
				FavIcon(modifier, product, viewModel)
			}
		}

		is ResponseState.LoadingWithData -> {
			if (savedState.value.data != null && product == savedState.value.data) {
				FavLoader(modifier)
			} else {
				FavIcon(modifier, product, viewModel)
			}
		}

		else -> {}
	}

	val savedProductsState =
		viewModel.productsSavedResponseState.collectAsStateWithLifecycle()

	when (savedProductsState.value) {
		is ResponseState.Success -> {
			savedProductsState.value.data?.let {
				savedProducts = it
				viewModel.reInitializeProductSavedState()
			}
		}

		is ResponseState.Failure -> {
			savedProductsState.value.error?.let {
				ProductsErrorBody(it, snackBarState)
			}
		}

		is ResponseState.LoadingWithData -> {
			if (product == savedState.value.data) {
				FavLoader(modifier)
			}
		}

		else -> {

		}
	}
}

@Composable
fun FavIcon(
	modifier: Modifier,
	product: Product,
	viewModel: ProductViewModel
) {
	val fav = product in savedProducts
	Image(
		painter = if (fav)
			painterResource(R.drawable.baseline_bookmark_24)
		else
			painterResource(R.drawable.baseline_bookmark_border_24),
		contentDescription = stringResource(R.string.favourite),
		modifier = modifier.clickable {
			if (!fav) {
				viewModel.save(product)
			} else {
				viewModel.remove(product)
			}
		},
		colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
	)
}

@Composable
fun FavLoader(modifier: Modifier) {
	Box(
		modifier = modifier,
		contentAlignment = Alignment.TopEnd
	) {
		CircularProgressIndicator(
			modifier = Modifier.size(24.dp),
			color = MaterialTheme.colorScheme.secondary
		)
	}
}

@Composable
fun LoadingBody() {
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier.fillMaxSize()
	) {
		CircularProgressIndicator()
	}
}

@Composable
fun ProductsErrorBody(error: Throwable, snackBarHostState: SnackbarHostState) {
	LaunchedEffect("ProductError") {
		error.message?.let {
			snackBarHostState.showSnackbar(
				it
			)
		}
	}
}