package com.souravroy.cleanproductapp.base.model


import com.google.gson.annotations.SerializedName

data class ResponseModel<T>(
	@SerializedName("products")
	val products: T,
	@SerializedName("total")
	val total: Int,
	@SerializedName("skip")
	val skip: Int,
	@SerializedName("limit")
	val limit: Int
)