package com.souravroy.cleanproductapp.modules.product.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.souravroy.cleanproductapp.R
import com.souravroy.cleanproductapp.modules.product.view.screens.NavigationRoutes.PRODUCT_DETAILS
import com.souravroy.cleanproductapp.modules.product.view.screens.NavigationRoutes.PRODUCT_HOME
import com.souravroy.cleanproductapp.modules.product.view.screens.NavigationRoutes.PRODUCT_SAVED
import com.souravroy.cleanproductapp.modules.product.view.screens.ProductDetailsBody
import com.souravroy.cleanproductapp.modules.product.view.screens.ProductHomeBody
import com.souravroy.cleanproductapp.modules.product.view.screens.ProductSavedBody
import com.souravroy.cleanproductapp.modules.product.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			// setup the navController this way
			val navController = rememberNavController()
			val viewModel: ProductViewModel = hiltViewModel()

			NavHost(
				navController, // the navController created above
				startDestination = PRODUCT_HOME // start destination variable needs to match one of the composable screen routes below
			) {
				composable(PRODUCT_HOME) { ProductHomeBody(viewModel, navController) }
				composable(PRODUCT_DETAILS) { ProductDetailsBody(viewModel, navController) }
				composable(PRODUCT_SAVED) { ProductSavedBody(viewModel, navController) }
			}
		}
	}
}

@Composable
fun Greeting(modifier: Modifier = Modifier, name: String? = null) {
	Text(
		modifier = modifier,
		text = name ?: stringResource(id = R.string.app_name)
	)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	//ProductHomeBody(null)
}