package com.myapplication.util.extension

fun String.concatParam(value: String) = this + value

fun String.validate() = this.isNotBlank()
