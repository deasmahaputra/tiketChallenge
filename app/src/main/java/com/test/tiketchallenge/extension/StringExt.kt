package com.test.tiketchallenge.extension

fun String.takeIfNotEmpty(defValue: String): String {
    return if (this.isNotEmpty()) {
        this
    } else {
        defValue
    }
}