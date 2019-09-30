package com.example.mvvmexample.base

import androidx.lifecycle.ViewModel
import com.example.mvvmexample.injection.component.ViewModelInjector
import com.example.mvvmexample.injection.component.DaggerViewModelInjector
import com.example.mvvmexample.injection.module.NetworkModule
import com.example.mvvmexample.ui.post.PostListViewModel

abstract class BaseViewModel: ViewModel() {
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is PostListViewModel -> injector.inject(this)
        }
    }
}