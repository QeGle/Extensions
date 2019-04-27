package com.qegle.extensions

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix

fun Bitmap.rotate(angle: Float): Bitmap {
	val matrix = Matrix()
	matrix.postRotate(angle)
	return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.isDark(): Boolean {
	var dark = false
	
	val darkThreshold = this.width.toFloat() * this.height.toFloat() * 0.75f
	var darkPixels = 0
	
	val pixels = IntArray(this.width * this.height)
	this.getPixels(pixels, 0, this.width, 0, 0, this.width, this.height)
	
	for (pixel in pixels) {
		val r = Color.red(pixel)
		val g = Color.green(pixel)
		val b = Color.blue(pixel)
		val luminance = 0.299 * r + 0.0 + 0.587 * g + 0.0 + 0.114 * b + 0.0
		if (luminance < 100) {
			darkPixels++
		}
	}
	
	if (darkPixels >= darkThreshold) {
		dark = true
	}
	return dark
}