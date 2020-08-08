package com.app.userinputdemo.view.viewdetails

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.userinputdemo.R
import com.app.userinputdemo.data.User
import com.app.userinputdemo.data.UserDatabase
import com.app.userinputdemo.databinding.FragmentViewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File


class ViewFragment : Fragment() {
    private lateinit var mBinding: FragmentViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_view, container, false)

        return mBinding.root
    }

    companion object {
        fun newInstance() = ViewFragment()
        val TAG = this.javaClass.simpleName
    }

    fun gettDetails() {
        UserDatabase.getDataBase(activity!!).daoUser().getUserById(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setData(it)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()
    }

    private fun setObserver() {
        mBinding.btLoad.setOnClickListener {
            gettDetails()
        }
    }

    fun setData(user: User) {
        mBinding.editText.text = user.name
        mBinding.editText2.text = user.dob
        mBinding.imageView.setImageURI(Uri.fromFile(File(user.uri)))

    }

}