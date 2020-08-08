package com.app.userinputdemo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.app.userinputdemo.databinding.ActivityMainBinding
import com.app.userinputdemo.databinding.FragmentRegisterBinding
import com.app.userinputdemo.helper.FragmentHelper
import com.app.userinputdemo.view.registerdetails.RegisterFragment
import com.app.userinputdemo.view.viewdetails.ViewFragment
import java.io.File
import java.lang.Exception

class MainActivity : AppCompatActivity() {
     lateinit var mBinding: ActivityMainBinding
    var myRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        replaceFragment()
    }
    fun replaceFragment() {

        FragmentHelper.replaceFragmentNormal(
            RegisterFragment.newInstance(),
            this,
            mBinding.container.id,
            RegisterFragment.TAG
        )
    }


}