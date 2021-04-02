package com.example.testtask.mvvm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.example.testtask.root.ext.inflater

abstract class BaseMultiTypeListAdapter<T:Any, VB:ViewBinding>(callback:DiffUtil.ItemCallback<T>) : ListAdapter<T, BaseViewHolder<VB,T>>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB,T> {
        return inflaterByType(parent,viewType)
    }

    private fun inflaterByType(parent:ViewGroup, viewType: Int): BaseViewHolder<VB,T> {
        return object : BaseViewHolder<VB,T>(inflate(parent.context.inflater(),parent,false)[viewType]?: error("ViewBinding is not found for type $viewType")){
            override fun bind(data : T) {
                bindActions()[viewType]?.invoke(this,data)
            }
        }
    }

    abstract fun inflate(inflater: LayoutInflater, parent:ViewGroup?, attachToParent:Boolean): Map<Int,VB>

    abstract fun bindActions():Map<Int, BaseViewHolder<VB,T>.(data:T) -> Unit>

    override fun onBindViewHolder(holder: BaseViewHolder<VB, T>, position: Int) {
        currentList[position]?.let { holder.bind(it) }
    }

    abstract fun isForType(item:T):Int

    override fun getItemViewType(position: Int): Int {
        return isForType(currentList[position])
    }
}