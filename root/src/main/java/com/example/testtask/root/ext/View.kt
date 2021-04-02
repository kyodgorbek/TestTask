package com.example.testtask.root.ext

import android.view.View

fun View.hide() {
    visibility = View.GONE
}

fun View.hideWithSpace() {
    visibility = View.INVISIBLE
    setOnClickListener(null)
}

fun View.show() {
    visibility = View.VISIBLE
}