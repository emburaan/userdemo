package com.app.userinputdemo.helper

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

object FragmentHelper {


    fun replaceFragmentNormal(
        fragment: Fragment,
        activity: AppCompatActivity,
        @IdRes containerId: Int,
        tag: String
    ) {

        activity.supportFragmentManager.beginTransaction()
            .replace(
                containerId,
                fragment,
                tag
            )
            .commit()

    }


}