package com.ekotyoo.storyapp.ui

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ekotyoo.storyapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        const val REQUEST_CODE_PERMISSIONS = 10
    }
}