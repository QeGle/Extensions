package com.qegle.extensions

import android.content.res.Resources
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView

/**
 * Show the view  (visibility = View.VISIBLE)
 */
fun View.show(): View {
	if (visibility != View.VISIBLE) {
		visibility = View.VISIBLE
	}
	return this
}

fun View.isVisible(): Boolean {
	return visibility == View.VISIBLE
}

fun View.isNotVisible(): Boolean {
	return !isVisible()
}

/**
 * Show the view if [predicate] returns true
 * (visibility = View.VISIBLE)
 */
inline fun View.showIf(predicate: () -> Boolean): View {
	if (visibility != View.VISIBLE && predicate()) {
		visibility = View.VISIBLE
	}
	return this
}

/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
fun View.hide(): View {
	if (visibility != View.INVISIBLE) {
		visibility = View.INVISIBLE
	}
	return this
}

/**
 * Hide the view if [predicate] returns true
 * (visibility = View.INVISIBLE)
 */
inline fun View.hideIf(predicate: () -> Boolean): View {
	if (visibility != View.INVISIBLE && predicate()) {
		visibility = View.INVISIBLE
	}
	return this
}

fun TextView.changeText(text: String) {
	val anim = AlphaAnimation(1.0f, 0.0f)
	anim.duration = 200
	anim.repeatCount = 1
	anim.repeatMode = Animation.REVERSE
	
	anim.setAnimationListener(object : Animation.AnimationListener {
		override fun onAnimationStart(animation: Animation) {}
		
		override fun onAnimationEnd(animation: Animation) {}
		
		override fun onAnimationRepeat(animation: Animation) {
			this@changeText.text = text
		}
	})
	this.startAnimation(anim)
}

fun Int.dpToPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Float.pxToDp() = this / Resources.getSystem().displayMetrics.density