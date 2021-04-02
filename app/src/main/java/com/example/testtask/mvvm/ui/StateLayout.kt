package com.example.testtask.mvvm.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import com.example.testtask.R
import com.example.testtask.data.root.UIState
import com.example.testtask.databinding.LayoutErrorViewBinding
import com.example.testtask.root.ext.dpToPx
import com.example.testtask.root.ext.hide
import com.example.testtask.root.ext.inflater
import com.example.testtask.root.ext.show
import kotlin.collections.set
import com.example.testtask.root.R as RootRes

class StateLayout private constructor(
	context : Context, attrs : AttributeSet? = null, defStyleAttr : Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

	companion object {

		fun create(context : Context, action : StateLayout.() -> StateLayout) =
			StateLayout(context).run(action)
	}

	/*
	 * listener region
	 * */
	private val stateObserver = Observer<UIState> { switchState(it) }
	private lateinit var lifecycle : Lifecycle
	private var emptyModel : EmptyModel? = null
	private lateinit var uiStateListener : LiveData<UIState>
	/*
	 * listener region end
	 * */

	/*
	 * view region
	 * */
	private val previousVisibleView : MutableLiveData<View> by lazy { MutableLiveData() }
	private val viewMap = mutableMapOf<UIState, View>()
	lateinit var content : View
	/* view region end*/

	/*
	component initialization region start
	*/
	fun withComponentActivity(activity : AppCompatActivity) {
		checkLifecycle { this.lifecycle = activity.lifecycle }
	}

	fun withComponentFragment(fragment : Fragment) {
		checkLifecycle { this.lifecycle = fragment.lifecycle }
	}

	fun withStateListener(uiStateListener : LiveData<UIState>) {
		checkStateListener { this.uiStateListener = uiStateListener }
	}

	fun withEmptyModel(emptyModel : EmptyModel){
		checkEmptyModel { this.emptyModel = emptyModel }
	}

	fun withContent(content : View) {
		checkContent { this.content = content }
	}

	fun attach() = run {
		check(::uiStateListener.isInitialized) { "UIState listener is not initialized" }
		check(::lifecycle.isInitialized) { "Component lifecycle is not initialized" }
		check(::content.isInitialized) { "Content is not initialized" }
		lifecycle.addObserver(ComponentViewLifecycleObserver())
		this
	}
	/*
	component initialization region end
	*/

	private fun checkLifecycle(action : () -> Unit) {
		check(!::lifecycle.isInitialized) { "Component lifecycle is already initialized." }
		action()
	}

	private fun checkEmptyModel(action : () -> Unit) {
		check(emptyModel == null) { "EmptyModel is already initialized." }
		action()
	}

	private fun checkContent(action : () -> Unit) {
		check(!::content.isInitialized) { "Content is already initialized." }
		action()
	}

	private fun checkStateListener(action : () -> Unit) {
		check(!::uiStateListener.isInitialized) { "UIState listener is already initialized." }
		action()
	}

	@SuppressLint("SetTextI18n")
	private fun switchState(state : UIState) {
		previousVisibleView.value?.hide()
		if (viewMap[state] == null) {
			viewMap[state] = when (state) {
				UIState.INTERNAL_ERROR, UIState.SERVER_ERROR -> {
					LayoutErrorViewBinding.inflate(context.inflater()).apply {
						viewImage.setImageResource(R.drawable.ic_server_error)
						title.text = "Oops"
						subtitle.text = context.resources.getString(RootRes.string.default_internal_error_message)
					}.root.apply {
						layoutParams =
							LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
								gravity = Gravity.CENTER
							}
					}
				}
				UIState.NETWORK_ERROR -> {
					LayoutErrorViewBinding.inflate(context.inflater()).apply {
						viewImage.setImageResource(R.drawable.ic_network_error)
						title.text = "Oops"
						subtitle.text = context.resources.getString(RootRes.string.default_network_error_message)
					}.root.apply {
						layoutParams =
							LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
								gravity = Gravity.CENTER
							}
					}
				}
				UIState.LOADING -> {
					ProgressBar(context).apply {
						layoutParams = LayoutParams(25.dpToPx(), 25.dpToPx()).apply {
							gravity = Gravity.CENTER
						}
					}
				}
				UIState.SUCCESS -> {
					content.apply {
						layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
					}
				}
				UIState.EMPTY -> {
					LayoutErrorViewBinding.inflate(context.inflater()).apply {
						emptyModel?.let {
							viewImage.setImageResource(it.imageRes)
							title.text = context.getString(it.emptyTitle)
							subtitle.text = context.getString(it.emptyDescription)
						}
					}.root.apply {
						layoutParams =
							LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
								gravity = Gravity.CENTER
							}
					}
				}
			}
			addView(viewMap[state])
		}
		showView(state)
	}

	private fun showView(state : UIState) {
		previousVisibleView.value = viewMap[state]
		viewMap[state]?.show()
	}

	override fun addView(child : View?) {
		super.addView(child)
		child?.hide()
	}

	private inner class ComponentViewLifecycleObserver : LifecycleObserver {

		@OnLifecycleEvent(Lifecycle.Event.ON_START)
		fun registerObserver() {
			uiStateListener.observeForever(stateObserver)
		}

		@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
		fun unregisterObserver() {
			uiStateListener.removeObserver(stateObserver)
		}

	}

	data class EmptyModel(
		@DrawableRes val imageRes : Int,
		@StringRes val emptyTitle : Int,
		@StringRes val emptyDescription : Int
	)

}