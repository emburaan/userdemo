package com.app.userinputdemo.view.registerdetails

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.userinputdemo.MainActivity
import com.app.userinputdemo.R
import com.app.userinputdemo.data.User
import com.app.userinputdemo.data.UserDatabase
import com.app.userinputdemo.databinding.FragmentRegisterBinding
import com.app.userinputdemo.helper.FragmentHelper
import com.app.userinputdemo.view.viewdetails.ViewFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*


class RegisterFragment : Fragment() {
    private lateinit var mBinding: FragmentRegisterBinding
    var imageUri: Uri? = null
    var requestCode = 1
    var myPicturePath = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        return mBinding.root
    }

    companion object {
        fun newInstance() = RegisterFragment()
        val TAG = this.javaClass.simpleName
    }

    fun setUpListner() {

        mBinding.button.setOnClickListener {

            requestStoragePermission()
        }


        mBinding.editTextDob.setOnClickListener {
            onClickDate()
        }

        mBinding.submit.setOnClickListener {

            if (validation()) {
                addUser(
                    mBinding.editTextName.text.toString(),
                    mBinding.editTextDob.text.toString(),
                    1
                )

                replaceFragment()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListner()
    }

    fun replaceFragment() {

        FragmentHelper.replaceFragmentNormal(
            ViewFragment.newInstance(),
            activity!! as AppCompatActivity,
            (activity as MainActivity).mBinding.container.id,
            ViewFragment.TAG
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {


            if (requestCode == 1) {

                try {
                    if (data != null) {
                        val selectedImage = data.data
                        val filePath = arrayOf(MediaStore.Images.Media.DATA)
                        val c = activity!!.contentResolver
                            .query(selectedImage!!, filePath, null, null, null)
                        c!!.moveToFirst()
                        val columnIndex = c.getColumnIndex(filePath[0])
                        var picturePath = c.getString(columnIndex)
                        c.close()

                        if (!picturePath.isNullOrEmpty()) {
                            myPicturePath = picturePath
                            setImage()
                        }


                    }
                } catch (e: Exception) {


                }
            }
        }
    }

    fun validation(): Boolean {
        if (mBinding.editTextName.text.isNullOrEmpty()) {
            Toast.makeText(activity, getString(R.string.name_error), Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (mBinding.editTextDob.text.isNullOrEmpty()) {
            Toast.makeText(activity, getString(R.string.dob_error), Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (myPicturePath.isNullOrEmpty()) {
            Toast.makeText(activity, getString(R.string.picture_error), Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true


    }

    fun setImage() {

        mBinding.imageView.setImageURI(Uri.fromFile(File(myPicturePath)))
    }

    fun addUser(name: String, dob: String, id: Int) {
        val user = User(id, name, dob, myPicturePath)
        Single.fromCallable {
            UserDatabase.getDataBase(activity!!).daoUser().insertUser(user)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe()

    }

    fun onClickDate() {

        val myCalendar: Calendar = Calendar.getInstance()

        val datePickerDialog =
            DatePickerDialog(
                activity!!,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                    mBinding.editTextDob.text =
                        year.toString() + "/" + (month + 1).toString() + "/" + dayOfMonth.toString()


                },
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DATE)
            )
        datePickerDialog.show()


    }


    @SuppressLint("SimpleDateFormat")
    private fun selectImage() {
        val chooseGallery = getString(R.string.chooseGallery)
        val options = arrayOf<CharSequence>(chooseGallery)
        val builder = AlertDialog.Builder(activity)
        builder.setItems(options) { dialog, item ->
            if (options[item] == chooseGallery) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, requestCode)

            }
        }
        builder.show()
    }


    private fun requestStoragePermission() {
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        selectImage()
//                        CommonUtils.showToast(activity!!,"All permissions are granted!")
                    }
                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // show alert dialog navigating to Settings

                        Toast.makeText(
                            activity,
                            getString(R.string.please_allow_permissions),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).withErrorListener { error ->
                Toast.makeText(
                    activity,
                    getString(R.string.error_occured),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .onSameThread()
            .check()
    }


}