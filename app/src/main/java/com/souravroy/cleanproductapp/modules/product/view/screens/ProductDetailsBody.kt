package com.souravroy.cleanproductapp.modules.product.view.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.souravroy.cleanproductapp.R
import com.souravroy.cleanproductapp.base.model.ResponseState
import com.souravroy.cleanproductapp.modules.product.view.Greeting
import com.souravroy.cleanproductapp.modules.product.viewmodel.ProductViewModel
import com.souravroy.cleanproductapp.ui.theme.CleanProductAppTheme

/**
 * @Author: Sourav PC
 * @Email: 1994sourav@gmail.com
 * @Date: 22-09-2023
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsBody(viewModel: ProductViewModel, navController: NavController? = null) {
	val snackbarState = remember { SnackbarHostState() }
	val id = navController?.currentBackStackEntry?.arguments?.getString("id")
	id?.let {
		viewModel.getProduct(it.toInt())
	}

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
						}
					)
				},
				snackbarHost = {
					SnackbarHost(hostState = snackbarState)
				}
			) { contentPadding ->

				val productState = viewModel.productResponseState.collectAsStateWithLifecycle()
				when (productState.value) {
					is ResponseState.Success -> {
						productState.value.data?.let {
							ProductDetails(it, viewModel, contentPadding, snackbarState)
						}
					}

					is ResponseState.Failure -> {
						productState.value.error?.let {
							ProductsErrorBody(it, snackbarState)
						}
					}

					is ResponseState.Loading, is ResponseState.LoadingWithData -> {

					}

					else -> {

					}
				}
			}
		}
	}
}
