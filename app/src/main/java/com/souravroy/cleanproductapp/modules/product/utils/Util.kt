package com.souravroy.cleanproductapp.modules.product.utils

import java.math.RoundingMode
import java.text.DecimalFormat


/**
 * @Author: Sourav Roy
 * @Email: 1994sourav@gmail.com
 * @Date: 21-09-2023
 */

object Util {
	fun roundOffDecimal(number: Double): Double {
		val df = DecimalFormat("#.##")
		df.roundingMode = RoundingMode.CEILING
		return df.format(number).toDouble()
	}
}