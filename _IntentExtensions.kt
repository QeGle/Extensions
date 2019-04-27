package com.qegle.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.provider.CalendarContract
import android.provider.ContactsContract
import com.argin.common.models.MailSignature
import java.io.File


/**
 * Extension method to share for Context.
 */
fun Context.share(text: String, subject: String = ""): Boolean {
	val intent = Intent()
	intent.type = "text/plain"
	intent.putExtra(EXTRA_SUBJECT, subject)
	intent.putExtra(EXTRA_TEXT, text)
	try {
		startActivity(createChooser(intent, null))
		return true
	} catch (e: ActivityNotFoundException) {
		return false
	}
}

/**
 * Extension method to send email for Context.
 */
fun Context.email(email: String, signature: MailSignature? = null): Boolean {
	
	try {
		val emailIntent = Intent(ACTION_SENDTO, Uri.fromParts("mailto", email, null))
		
		if (signature != null) {
			val folder = File(this.externalCacheDir?.path + "/temp/mail")
			folder.mkdirs()
			
			val file = File(folder, "config.json")
			file.appendText(signature.getGson())
			emailIntent.putExtra(EXTRA_EMAIL, arrayOf(email))
			emailIntent.putExtra(EXTRA_STREAM, Uri.fromFile(file))
		}
		
		this.startActivity(createChooser(emailIntent, null))
		return true
	} catch (e: ActivityNotFoundException) {
		return false
	}
}

/**
 * Open the add-contact screen with pre-filled info
 *
 * @param person
 *            {@link Person} to add to contacts list
 */
fun Context.addAsContactConfirmed(name: String, company: String, jobTitle: String, phone: String, email: String) {
	val intent = Intent(ACTION_INSERT)
	intent.type = ContactsContract.Contacts.CONTENT_TYPE
	
	intent.putExtra(ContactsContract.Intents.Insert.NAME, name)
	intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone)
	intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email)
	intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company)
	intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle)
	
	this.startActivity(intent)
}

/**
 *  Open the add-event screen in calendar app with pre-filled info
 *
 */
fun Context.addCalendarEvent(title: String, location: String, description: String, start: Long, end: Long, timezone: String) {
	
	val calendarIntentVar = Intent(ACTION_INSERT)
			.setData(CalendarContract.Events.CONTENT_URI)
			.putExtra(CalendarContract.Events.DTSTART, start)
			.putExtra(CalendarContract.Events.EVENT_LOCATION, location)
			.putExtra(CalendarContract.Events.DTEND, end)
			.putExtra(CalendarContract.Events.EVENT_TIMEZONE, timezone)
			.putExtra(CalendarContract.Events.TITLE, title)
			.putExtra(CalendarContract.Events.DESCRIPTION, description)
	
	
	this.startActivity(calendarIntentVar)
}

/**
 * Extension method to make call for Context.
 */
fun Context.phoneTo(number: String): Boolean {
	return try {
		this.startActivity(Intent(ACTION_DIAL, Uri.parse("tel:$number")))
		true
	} catch (e: Exception) {
		false
	}
}


/**
 * Extension method to browse for Context.
 */
fun Context.browse(url: String): Boolean {
	return try {
		if (url.isUrl()) {
			this.startActivity(Intent(ACTION_VIEW, Uri.parse(url)));true
		} else false
	} catch (e: Exception) {
		false
	}
}


fun String.isUrl() = this.matches(Regex("^(https?|ftp)://[-a-zA-Z-а-яА-Я0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z-а-яА-Я0-9+&@#/%=~_|]"))


fun <T> startActivity(packageContext: Context, cls: Class<T>) {
	val intent = Intent(packageContext, cls).apply {
		addFlags(FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
	}
	packageContext.startActivity(intent)
}