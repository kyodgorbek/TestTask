package com.example.testtask.mvvm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.testtask.root.ext.inflater

abstract class BasePagingAdapter<T : Any, VB : ViewBinding>(callback : DiffUtil.ItemCallback<T>) : PagingDataAdapter<T, BaseViewHolder<VB, T>>(callback) {

	override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : BaseViewHolder<VB, T> {
		return inflaterByType(parent)
	}

	private fun inflaterByType(parent : ViewGroup) : BaseViewHolder<VB, T> {
		return object : BaseViewHolder<VB, T>(inflate(parent.context.inflater(), parent, false)) {
			override fun bind(data : T) {
				bindActionTo(data)
			}
		}
	}

	abstract fun inflate(inflater : LayoutInflater, parent : ViewGroup?, attachToParent : Boolean) : VB

	abstract fun BaseViewHolder<VB, T>.bindActionTo(data : T)

	override fun onBindViewHolder(holder : BaseViewHolder<VB, T>, position : Int) {
		getItem(position)?.let { holder.bind(it) }
	}
}
