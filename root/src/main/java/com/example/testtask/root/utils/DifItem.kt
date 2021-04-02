package com.example.testtask.root.utils

interface DifItem<T> {

	fun areItemsTheSame(second : T) : Boolean
	fun areContentsTheSame(second : T) : Boolean

}
