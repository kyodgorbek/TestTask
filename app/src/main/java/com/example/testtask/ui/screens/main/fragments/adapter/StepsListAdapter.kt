package com.example.testtask.ui.screens.main.fragments.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.testtask.R
import com.example.testtask.data.model.Step
import com.example.testtask.databinding.AdapterStepInfoItemBinding
import com.example.testtask.mvvm.ui.BaseListAdapter
import com.example.testtask.mvvm.ui.BaseViewHolder
import com.example.testtask.root.utils.getDiffCallback

class StepsListAdapter : BaseListAdapter<Step,AdapterStepInfoItemBinding>(getDiffCallback()) {

	override fun inflate(inflater : LayoutInflater, parent : ViewGroup?, attachToParent : Boolean) : AdapterStepInfoItemBinding {
		return AdapterStepInfoItemBinding.inflate(inflater,parent,attachToParent)
	}

	override fun BaseViewHolder<AdapterStepInfoItemBinding, Step>.bindActionTo(data : Step) {
		binding.apply {
			data.apply {
				tvDistance.text = distance.text
				tvDuration.text = duration.text
				tvActionTitle.text = Html.fromHtml(html_instructions)
				maneuver?.let {
					when(it){
						"turn-right" -> ivManeuverImage.setImageResource(R.drawable.ic_maneuver_turn_45)
						"turn-left" -> ivManeuverImage.setImageResource(R.drawable.ic_maneuver_turn_45_left)
					}
				} ?: run { ivManeuverImage.setImageResource(R.drawable.ic_maneuver_turn_0) }
			}
		}
	}
}