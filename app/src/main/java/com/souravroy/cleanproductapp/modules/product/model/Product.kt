package com.souravroy.cleanproductapp.modules.product.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.souravroy.cleanproductapp.modules.product.utils.ProductConstants

@Entity(tableName = ProductConstants.PRODUCT_TABLE_NAME)
data class Product(
	@PrimaryKey
	@SerializedName("id")
	val id: Int,
	@SerializedName("title")
	val title: String,
	@SerializedName("description")
	val description: String,
	@SerializedName("price")
	val price: Float,
	@SerializedName("discountPercentage")
	val discountPercentage: Double,
	@SerializedName("rating")
	val rating: Double,
	@SerializedName("stock")
	val stock: Int,
	@SerializedName("brand")
	val brand: String,
	@SerializedName("category")
	val category: String,
	@SerializedName("thumbnail")
	val thumbnail: String,
	@SerializedName("images")
	val images: List<String>
)