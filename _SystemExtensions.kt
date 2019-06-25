package com.qegle.extensions

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.BatteryManager


/**
 * Возвращает процент зарада батареи в пределах 0..100
 */
fun Context.getBatteryPercentage(): Int? {
	return this.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))?.getBatteryPercentage()
}


fun Context.notifyManager() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
fun Context.connectService() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

/**
 * Возвращает true если есть активное интернет соединение
 */
fun Context.hasConnect(): Boolean {
	return this.connectService().activeNetworkInfo?.isConnected == true
}

/**
 * Возвращает процент зарада батареи в пределах 0..100
 */
fun Intent.getBatteryPercentage(): Int? {
	
	val level = this.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: return null
	val scale = this.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: return null
	if (level == -1 || scale == -1) return null
	
	val batteryPct = level / scale.toFloat()
	
	return (batteryPct * 100).toInt()
}


/**
 * Возвращает true, если у устройства есть вспышка/фонарик
 */
fun Context.hasTorch() = this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

/**
* Возвращает true, если разрешение получен
*/
fun Context.checkPermission(permission: String): Boolean {
	return (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED)
}

/**
* Возвращает билдер в зависимости от оси девайса
*/
fun Context.notifyBuilder(channelId: String): Notification.Builder {
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		Notification.Builder(this, channelId)
	else
		Notification.Builder(this)
}
