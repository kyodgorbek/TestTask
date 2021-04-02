package com.example.testtask.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextValuePair(
    val text: String,
    val value: Int
):Parcelable