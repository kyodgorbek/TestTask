package com.example.testtask.ui.screens.main.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.example.testtask.Application
import com.example.testtask.R
import com.example.testtask.data.model.Step
import com.example.testtask.databinding.BottomFragmentStepsInfoBinding
import com.example.testtask.shared.helpers.BottomSheetCallback
import com.example.testtask.ui.screens.main.fragments.adapter.StepsListAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val LIST_KEY = "stepsList"
const val TAG = "StepInfo"

class StepInfoBottomFragment private constructor() : BottomSheetDialogFragment() {

	companion object {

		fun show(fragmentManager : FragmentManager, list : List<Step>) = StepInfoBottomFragment().apply {
			arguments = bundleOf(LIST_KEY to list)
		}.show(fragmentManager, TAG)
	}

	private lateinit var steps : List<Step>
	private lateinit var mBinding : BottomFragmentStepsInfoBinding

	override fun getTheme() : Int {
		return R.style.BottomSheetDialogTheme
	}
	override fun onCreateDialog(savedInstanceState : Bundle?) : Dialog {
		val dialog = super.onCreateDialog(savedInstanceState)
		dialog.setOnShowListener {
			it as BottomSheetDialog
			val bottomSheet = it.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
			bottomSheet!!.background = ResourcesCompat.getDrawable(Application.getInstance().applicationContext.resources, android.R.color.transparent, null)
			val behavior = BottomSheetBehavior.from(bottomSheet)
			behavior.state = BottomSheetBehavior.STATE_EXPANDED
			behavior.addBottomSheetCallback(object : BottomSheetCallback() {
				override fun onStateChanged(bottomSheet : View, newState : Int) {
					if (newState != BottomSheetBehavior.STATE_EXPANDED) {
						dismiss()
					}
				}
			})
		}
		return dialog
	}

	override fun onCreate(savedInstanceState : Bundle?) {
		super.onCreate(savedInstanceState)
		steps = arguments?.getParcelableArrayList(LIST_KEY) ?: emptyList()
	}

	override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
		if (steps.isEmpty()) {
			dismiss()
		}
		mBinding = BottomFragmentStepsInfoBinding.inflate(inflater, container, false)
		mBinding.initList(steps)
		return mBinding.root
	}

	private fun BottomFragmentStepsInfoBinding.initList(steps : List<Step>) {
		rvSteps.adapter = StepsListAdapter().apply { submitList(steps) }
	}
}