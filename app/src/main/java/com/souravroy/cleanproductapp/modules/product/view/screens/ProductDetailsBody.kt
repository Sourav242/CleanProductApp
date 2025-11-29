package com.souravroy.cleanproductapp.modules.product.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
import com.souravroy.cleanproductapp.modules.product.view.Greeting
import com.souravroy.cleanproductapp.modules.product.viewmodel.ProductViewModel
import com.souravroy.cleanproductapp.ui.theme.CleanProductAppTheme
import com.souravroy.cleanproductapp.ui.theme.success
import com.souravroy.cleanproductapp.ui.theme.warning
import java.util.Locale

/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 22-09-2023
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsBody(viewModel: ProductViewModel, navController: NavController? = null) {
	val snackBarState = remember { SnackbarHostState() }

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
							Greeting(name = stringResource(id = R.string.product_details))
						},
						actions = {
							IconButton(onClick = {
								navController?.navigate(NavigationRoutes.PRODUCT_SAVED)
							}) {
								Icon(
									painter = painterResource(id = R.drawable.baseline_bookmark_24),
									contentDescription = stringResource(id = R.string.favourite)
								)
							}
						},
						navigationIcon = {
							IconButton(onClick = {
								navController?.popBackStack()
							}) {
								Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
									contentDescription = stringResource(id = R.string.favourite)
								)
							}
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            scrolledContainerColor = MaterialTheme.colorScheme.primary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
					)
				},
				snackbarHost = {
					SnackbarHost(hostState = snackBarState)
				}
			) { contentPadding ->

				val networkStatus by viewModel.connectivityObserver.observe()
					.collectAsStateWithLifecycle(
						initialValue = ConnectivityObserver.NetworkStatus.Null
					)

				val id = navController?.currentBackStackEntry?.arguments?.getString(
					NavigationRoutes.QueryParams.ID
				)

				val productState = when (networkStatus) {
					is ConnectivityObserver.NetworkStatus.Available -> {
						id?.let {
							viewModel.getProduct(it.toInt())
						}
						viewModel.productResponseState.collectAsStateWithLifecycle()
					}

					else -> {
						id?.let {
							viewModel.getSavedProduct(it.toInt())
						}
						viewModel.productSavedResponseState.collectAsStateWithLifecycle()
					}
				}

				when (productState.value) {
					is ResponseState.Success -> {
						productState.value.data?.let {
                            ProductDetails(it, viewModel, contentPadding, snackBarState)
						}
					}

					is ResponseState.Failure -> {
						productState.value.error?.let {
							ProductsErrorBody(it, snackBarState)
						}
					}

					else -> {}
				}
			}
		}
	}
}

@Composable
fun ProductDetails(
    product: Product,
    viewModel: ProductViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    snackBarState: SnackbarHostState
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
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background
                    )
                    .padding(all = 16.dp)
            ) {
                Text(
                    text = product.title,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.inverseSurface
                )

                RatingBar(
                    rating = product.rating,
                    color = if (product.rating > 4) {
                        MaterialTheme.colorScheme.success
                    } else if (product.rating > 2) {
                        MaterialTheme.colorScheme.warning
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .height(14.dp)
                )
                if (product.discountPercentage <= 0) {
                    Text(
                        text = "Price - $${
                            String.format(
                                Locale.getDefault(),
                                "%.2f",
                                product.price
                            )
                        }",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "-${product.discountPercentage}%",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 20.sp
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
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    }
                }
                if (product.stock == 1) {
                    Text(
                        text = "Hurry up stock\'s last",
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
                Text(
                    text = product.description,
                    modifier = Modifier.padding(top = 8.dp),
                )
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
            snackBarState = snackBarState
        )
    }
}