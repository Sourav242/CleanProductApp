package com.souravroy.cleanproductapp.modules.product.utils

import com.souravroy.cleanproductapp.modules.product.utils.ProductConstants.MIN_TEXT_LENGTH_SEARCH

/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 22-09-2023
 */

fun String.validateSearchText() = this.length >= MIN_TEXT_LENGTH_SEARCH || this.isEmpty()