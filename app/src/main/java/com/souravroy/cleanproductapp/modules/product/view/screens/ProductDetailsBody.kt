package com.souravroy.cleanproductapp.modules.product.view.screens

import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.souravroy.cleanproductapp.R
import com.souravroy.cleanproductapp.base.model.ResponseState
import com.souravroy.cleanproductapp.modules.product.utils.connection.ConnectivityObserver
import com.souravroy.cleanproductapp.modules.product.view.Greeting
import com.souravroy.cleanproductapp.modules.product.viewmodel.ProductViewModel
import com.souravroy.cleanproductapp.ui.theme.CleanProductAppTheme

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
						}
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
