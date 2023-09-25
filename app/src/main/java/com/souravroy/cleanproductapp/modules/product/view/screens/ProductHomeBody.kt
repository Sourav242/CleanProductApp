package com.souravroy.cleanproductapp.modules.product.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.souravroy.cleanproductapp.R
import com.souravroy.cleanproductapp.base.model.ResponseState
import com.souravroy.cleanproductapp.modules.product.model.Product
import com.souravroy.cleanproductapp.modules.product.utils.connection.ConnectivityObserver
import com.souravroy.cleanproductapp.modules.product.utils.custom.RatingBar
import com.souravroy.cleanproductapp.modules.product.utils.validateSearchText
import com.souravroy.cleanproductapp.modules.product.view.Greeting
import com.souravroy.cleanproductapp.modules.product.view.screens.NavigationRoutes.PRODUCT_DETAILS
import com.souravroy.cleanproductapp.modules.product.view.screens.NavigationRoutes.PRODUCT_HOME
import com.souravroy.cleanproductapp.modules.product.view.screens.NavigationRoutes.PRODUCT_SAVED
import com.souravroy.cleanproductapp.modules.product.viewmodel.ProductViewModel
import com.souravroy.cleanproductapp.ui.theme.CleanProductAppTheme
import com.souravroy.cleanproductapp.ui.theme.success
import com.souravroy.cleanproductapp.ui.theme.warning

/**
 * @Author: Sourav PC
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

private var savedProducts: List<Product> = listOf()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductHomeBody(viewModel: ProductViewModel, navController: NavController? = null) {
	val snackbarState = remember { SnackbarHostState() }

	CleanProductAppTheme {
		// A surface container using the 'background' color from the theme
		Surface(
			modifier = Modifier.fillMaxSize(),
			color = MaterialTheme.colorScheme.background
		) {
			Scaffold(
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
						}
					)
				},
				snackbarHost = {
					SnackbarHost(hostState = snackbarState)
				}
			) { contentPadding ->
				ProductBody(viewModel, navController, contentPadding, true, snackbarState)
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
	snackbarState: SnackbarHostState
) {
	Column(
		modifier = Modifier.padding(contentPadding)
	) {

		val networkStatus by viewModel.connectivityObserver.observe()
			.collectAsStateWithLifecycle(
				initialValue = ConnectivityObserver.Status.Null
			)

		if (!networkStatus.status && remote) {
			networkStatus.networkStatus?.let {
				ProductsErrorBody(
					Exception("Internet connection $it"),
					snackbarState
				)
			}

			Column(
				modifier = Modifier.fillMaxSize(),
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
		} else {
			SearchBody(viewModel, remote)

			Box {
				val productsState = if (remote)
					viewModel.productsResponseState.collectAsStateWithLifecycle()
				else
					viewModel.productsSavedResponseState.collectAsStateWithLifecycle()

				when (productsState.value) {
					is ResponseState.Success -> {
						productsState.value.data?.let {
							if (it.isEmpty()) {
								Text(
									modifier = Modifier.fillMaxSize(),
									text = stringResource(R.string.no_products_available),
									textAlign = TextAlign.Center
								)
							} else {
								Products(viewModel, it, navController, snackbarState)
							}
						}
					}

					is ResponseState.Failure -> {
						productsState.value.error?.let {
							ProductsErrorBody(it, snackbarState)
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
fun SearchBody(viewModel: ProductViewModel, remote: Boolean) {
	val inputValue = remember { mutableStateOf(TextFieldValue()) }
	Card(
		modifier = Modifier.padding(
			start = 16.dp,
			end = 16.dp,
			bottom = 16.dp
		),
		border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant
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
					if (inputValue.value.text.validateSearchText()) {
						if (remote)
							viewModel.getProducts(inputValue.value.text)
						else
							viewModel.getSavedProducts(inputValue.value.text)
					}
				},
				trailingIcon = {
					if (inputValue.value.text.isNotEmpty()) {
						Icon(
							Icons.Default.Clear,
							contentDescription = stringResource(R.string.clear_text),
							modifier = Modifier
								.clickable {
									inputValue.value = TextFieldValue("")
									if (remote)
										viewModel.getProducts()
									else
										viewModel.getSavedProducts(inputValue.value.text)
								}
						)
					}
				},

				colors = TextFieldDefaults.textFieldColors(
					containerColor = MaterialTheme.colorScheme.surfaceVariant
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
	snackbarState: SnackbarHostState
) {
	val scrollState = rememberLazyListState()

	LazyColumn(state = scrollState) {
		items(products) {
			val modifier = Modifier.padding(
				start = 16.dp,
				end = 16.dp,
				top = 16.dp,
				bottom = if (products[products.size - 1] == it) 16.dp else 0.dp
			)
			ProductItem(it, viewModel, navController, snackbarState, modifier)
		}
	}
}

@Composable
fun ProductItem(
	product: Product,
	viewModel: ProductViewModel,
	navController: NavController? = null,
	snackbarState: SnackbarHostState,
	modifier: Modifier
) {
	Card(
		modifier = modifier
			.clickable {
				navController?.navigate(
					PRODUCT_DETAILS.replace(
						oldValue = "{id}",
						newValue = product.id.toString()
					)
				)
			},
		border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant
		),
	) {
		ProductDetails(product, viewModel = viewModel, snackbarState = snackbarState)
	}
}

@Composable
fun Favourite(
	modifier: Modifier,
	product: Product,
	viewModel: ProductViewModel,
	snackbarState: SnackbarHostState
) {
	var fav by remember { mutableStateOf(product in savedProducts) }

	val savedState = viewModel.productSavedState.collectAsStateWithLifecycle()
	when (savedState.value) {
		is ResponseState.Success -> {
			viewModel.getSavedProducts()
		}

		else -> {}
	}

	val savedProductsState =
		viewModel.productsSavedResponseState.collectAsStateWithLifecycle()

	when (savedProductsState.value) {
		is ResponseState.Success -> {
			savedProductsState.value.data?.let {
				savedProducts = it
				fav = product in savedProducts
				viewModel.reInitializeProductSavedState()
			}
		}

		is ResponseState.Failure -> {
			savedProductsState.value.error?.let {
				ProductsErrorBody(it, snackbarState)
				viewModel.productsSavedResponseState
			}
		}

		is ResponseState.Loading, is ResponseState.LoadingWithData -> {

		}

		else -> {
			Box(
				modifier = modifier,
				contentAlignment = Alignment.TopEnd
			) {
				CircularProgressIndicator()
			}
		}
	}

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
		colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.inverseOnSurface)
	)
}

@Composable
fun ProductDetails(
	product: Product,
	viewModel: ProductViewModel,
	contentPadding: PaddingValues = PaddingValues(0.dp),
	snackbarState: SnackbarHostState
) {
	Box(
		modifier = Modifier.padding(contentPadding)
	) {
		Column {
			AsyncImage(
				model = product.thumbnail,
				contentDescription = null,
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentHeight(
						align = Alignment.CenterVertically
					),
				contentScale = ContentScale.FillWidth
			)
			Column(modifier = Modifier.padding(all = 16.dp)) {
				Text(
					text = product.title,
					fontSize = 16.sp,
					color = MaterialTheme.colorScheme.inverseSurface
				)
				Text(text = product.description)

				RatingBar(
					rating = product.rating,
					color = if (product.rating > 4) {
						MaterialTheme.colorScheme.success
					} else if (product.rating > 2) {
						MaterialTheme.colorScheme.warning
					} else {
						MaterialTheme.colorScheme.error
					},
					modifier = Modifier.height(14.dp)
				)
				if (product.discountPercentage <= 0) {
					Text(
						text = "Price - $${product.price}",
						modifier = Modifier.padding(start = 8.dp)
					)
				} else {
					Row(
						verticalAlignment = Alignment.CenterVertically
					) {
						Text(
							text = "-${product.discountPercentage}%",
							color = MaterialTheme.colorScheme.error,
							fontSize = 16.sp
						)
						Text(
							text = "$${product.price + (product.price * product.discountPercentage / 100).toInt()}",
							modifier = Modifier.padding(start = 8.dp),
							style = TextStyle(textDecoration = TextDecoration.LineThrough),
							color = MaterialTheme.colorScheme.outline
						)
						Text(
							text = "$${product.price}",
							modifier = Modifier.padding(start = 8.dp),
							fontSize = 16.sp,
							color = MaterialTheme.colorScheme.inverseSurface
						)
					}
				}
				if (product.stock == 1) {
					Text(
						text = "Hurry up stock's last",
						color = MaterialTheme.colorScheme.warning,
						fontSize = 12.sp
					)
				} else if (product.stock < 10) {
					Text(
						text = "Hurry up only ${product.stock} left",
						color = MaterialTheme.colorScheme.warning,
						fontSize = 12.sp
					)
				}
			}
		}
		Box(
			modifier = Modifier.background(
				brush = Brush.horizontalGradient(
					colors = listOf(
						MaterialTheme.colorScheme.primary,
						MaterialTheme.colorScheme.primary,
						MaterialTheme.colorScheme.primary,
						colorResource(id = android.R.color.transparent)
					)
				)
			)
		) {
			Text(
				modifier = Modifier.padding(
					start = 8.dp,
					end = 16.dp
				),
				text = product.category,
				color = MaterialTheme.colorScheme.inversePrimary,
				fontSize = 12.sp
			)
		}
		Favourite(
			modifier = Modifier
				.align(Alignment.TopEnd)
				.padding(all = 16.dp),
			product = product,
			viewModel = viewModel,
			snackbarState = snackbarState
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
fun ProductsErrorBody(error: Throwable, snackbarHostState: SnackbarHostState) {
	LaunchedEffect("ProductError") {
		error.message?.let {
			snackbarHostState.showSnackbar(
				it
			)
		}
	}
}