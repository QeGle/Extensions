package com.qegle.extensions

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

fun Context.getAppId(): String? {
	return getString("app_id")
}

fun Context.getVuforiaKey(): String? {
	return getString("vuforia_key")
}

private fun Context.getInt(key: String): Int? {
	return try {
		getAppInfo().metaData.getInt(key)
	} catch (ignored: PackageManager.NameNotFoundException) {
		null
	} catch (ignored: NullPointerException) {
		null
	}
}

private fun Context.getString(key: String): String? {
	return try {
		getAppInfo().metaData.getString(key)
	} catch (ignored: PackageManager.NameNotFoundException) {
		null
	} catch (ignored: NullPointerException) {
		null
	}
}


private fun Context.getAppInfo(): ApplicationInfo {
	return packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
}