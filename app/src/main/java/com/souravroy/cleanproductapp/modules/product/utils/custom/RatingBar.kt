package com.souravroy.cleanproductapp.modules.product.utils.custom

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun RatingBar(
	modifier: Modifier = Modifier,
	rating: Double = 0.0,
	stars: Int = 5,
	color: Color = Color.Yellow,
) {
	val filledStars = floor(rating).toInt()
	val unfilledStars = (stars - ceil(rating)).toInt()
	val halfStar = !(rating.rem(1).equals(0.0))
	Row(modifier = modifier) {
		repeat(filledStars) {
			Icon(
				imageVector = Icons.Default.Star,
				contentDescription = null,
				tint = color
			)
		}
		if (halfStar) {
			Icon(
				imageVector = Icons.Outlined.Star,
				contentDescription = null,
				tint = color
			)
		}
		repeat(unfilledStars) {
			Icon(
				imageVector = Icons.Outlined.Star,
				contentDescription = null,
				tint = color
			)
		}
	}
}

@Preview
@Composable
fun RatingPreview() {
	RatingBar(rating = 2.5)
}

@Preview
@Composable
fun TenStarsRatingPreview() {
	RatingBar(stars = 10, rating = 8.5)
}

@Preview
@Composable
fun RatingPreviewFull() {
	RatingBar(rating = 5.0)
}

@Preview
@Composable
fun RatingPreviewWorst() {
	RatingBar(rating = 1.0)
}

@Preview
@Composable
fun RatingPreviewDisabled() {
	RatingBar(rating = 0.0, color = Color.Gray)
}