package com.mty.youtubeapiactivity.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB: ViewBinding, VM: ViewModel> : AppCompatActivity() {
    protected  lateinit var binding : VB
    protected abstract val  viewModel : VM

    protected abstract fun inflateViewBinding(inflater: LayoutInflater):VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateViewBinding(layoutInflater)
        setContentView(binding.root)
        checkInternet()
        initViewModel()
        initView()
        initListener()
    }
   open fun initListener() {}

    abstract fun initView()

    abstract fun initViewModel()

    abstract fun checkInternet()
}