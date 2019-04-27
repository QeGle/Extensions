package com.qegle.extensions

import java.io.File


/**
 * Считает вес файла вместе со вложенными файлами
 */
fun File.getSize(): Long {
	var size: Long = 0
	if (this.isDirectory) {
		this.listFiles().forEach { size += it.getSize() }
	} else {
		size = this.length()
	}
	return size
}

/**
 * Возвращает массив файлов в папке используя строку как путь к ней
 */
fun String.files(): Array<File> {
	val folder = File(this)
	if (folder.exists() && folder.isDirectory) {
		return folder.listFiles() ?: emptyArray()
	}
	return emptyArray()
}

/**
 * Копирует файл по заданному[outputPath] пути
 */
fun File.copyTo(outputPath: String, overwrite: Boolean = false) {
	val outFile = File(outputPath)
	this.copyTo(outFile, overwrite)
}