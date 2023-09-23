package com.souravroy.cleanproductapp.modules.product.view.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.souravroy.cleanproductapp.R
import com.souravroy.cleanproductapp.modules.product.view.Greeting
import com.souravroy.cleanproductapp.modules.product.viewmodel.ProductViewModel
import com.souravroy.cleanproductapp.ui.theme.CleanProductAppTheme

/**
 * @Author: Sourav PC
 * @Date: 22-09-2023
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSavedBody(viewModel: ProductViewModel, navController: NavController? = null) {
	val snackbarState = remember { SnackbarHostState() }
	viewModel.getSavedProducts()

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
							Greeting(name = stringResource(id = R.string.favourite))
						}
					)
				}, snackbarHost = {
					SnackbarHost(hostState = snackbarState)
				}
			) { contentPadding ->
				ProductBody(viewModel, navController, contentPadding, false, snackbarState)
			}
		}
	}
}