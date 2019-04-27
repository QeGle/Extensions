package com.qegle.extensions

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.HttpURLConnection
import java.util.*
import java.util.concurrent.TimeUnit

private const val CHECKED_SUF = "/system/ok"

/**
 * Производит проверку доступности урла
 */
fun ping(url: String, checkedSuffix: String = CHECKED_SUF): PingResult {
	val timer = Timer()
	try {
		val client =
				OkHttpClient.Builder()
						.connectTimeout(5, TimeUnit.SECONDS).build()
		
		val request =
				Request.Builder()
						.url(url + checkedSuffix)
						.build()
		
		
		val resp = client.newCall(request).execute()
				?: return PingResult(url, false, timer.getSpend(), null, "Ping: response is null")
		
		return if (resp.code() == HttpURLConnection.HTTP_OK) PingResult(url, true, timer.getSpend(), HttpURLConnection.HTTP_OK)
		else PingResult(url, false, timer.getSpend(), resp.code(), resp.message())
	} catch (e: IOException) {
		return PingResult(url, false, timer.getSpend(), null, "Ping: ${e.message}")
	}
}

/**
 * Производит проверку доступности массива урлов. [onRequestTimeListener] принимает метрику по каждому пингу
 */
fun ping(urlsArray: ArrayList<String>, onRequestTimeListener: (metric: Metric) -> Unit): Pair<String, ArrayList<String>> {
	val errorUrls = arrayListOf<String>()
	val url = (urlsArray.asSequence().find {
		val result = ping(it)
		
		onRequestTimeListener.invoke(Metric(result.url, result.time, result.code, result.message))
		if (result.isSuccess.not()) errorUrls.add(result.url)
		return@find result.isSuccess
	} ?: urlsArray[0])
	return Pair(url, errorUrls)
}

class PingResult(val url: String, val isSuccess: Boolean, val time: Long, val code: Int?, val message: String? = null)
class Metric(val url: String, val time: Long, val code: Int?, var message: String? = null)

