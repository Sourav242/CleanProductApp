package com.souravroy.cleanproductapp.modules.product.view.screens

/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 22-09-2023
 */

object NavigationRoutes {
	const val PRODUCT_HOME = "/products"
	const val PRODUCT_DETAILS = "/products/{${QueryParams.ID}}"
	const val PRODUCT_SAVED = "/products/saved"
	const val PRODUCT_SEARCH = "/products/search"
	const val PRODUCT_CATEGORIES = "/products/categories"
	const val PRODUCT_CATEGORY = "/products/categories/{${QueryParams.CATEGORY}}"

	object QueryParams {
		const val ID = "id"
		const val CATEGORY = "category"
		const val SEARCH = "q"
	}
}