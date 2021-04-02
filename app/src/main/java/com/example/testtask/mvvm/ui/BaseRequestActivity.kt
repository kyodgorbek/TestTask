package com.example.testtask.mvvm.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.testtask.R
import com.example.testtask.mvvm.vm.BaseViewModel
import com.example.testtask.mvvm.vm.ViewCommand
import com.example.testtask.root.ext.launch
import com.example.testtask.ui.commands.Commands
import com.example.testtask.ui.screens.splash.SplashScreenFirstActivity

abstract class BaseRequestActivity<VB : ViewBinding, VM : BaseViewModel> : BaseActivity<VB>() {

	private val _viewModel by lazy { ViewModelProvider.NewInstanceFactory().create(viewModelType) }
	val mViewModel : VM
		get() = _viewModel

	protected abstract val viewModelType : Class<VM>

	protected abstract fun initView(binding : VB, viewModel : VM)

	@Deprecated("This function is deprecated for BaseRequestActivity child", ReplaceWith("initView(binding:VB,viewModel:VM)"))
	override fun initView(binding : VB) {
	}

	override fun onCreate(savedInstanceState : Bundle?) {
		savedInstanceState?.putBoolean(DISCARD_CONTENT, true)
		super.onCreate(savedInstanceState)
		initView(mBinding, _viewModel)
		_viewModel.viewCommands.observe(this) {
			proceedInternalCommands(it)
		}
		setContentView(mBinding.root)
	}

	fun getContent() {

	}

	private fun proceedInternalCommands(command : ViewCommand) {
		when (command) {
			is Commands.NetworkError -> {
				showNetworkErrorSnackBar(getString(R.string.default_network_error_message))
				hideLoadingDialog()
			}
			is Commands.ShowMessage -> {
				showToast(command.resId)
				hideLoadingDialog()
			}
			is Commands.ShowMessageText -> {
				showToast(command.errorMessage)
				hideLoadingDialog()
			}
			is Commands.ReLaunchApp -> {
				launch(flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) { SplashScreenFirstActivity::class.java }
				hideLoadingDialog()
			}
			else -> proceedViewCommand(command)
		}
	}

	protected abstract fun proceedViewCommand(command : ViewCommand)

}