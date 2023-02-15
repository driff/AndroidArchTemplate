package com.driff.android.modulartemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commitNow
import com.driff.android.modulartemplate.databinding.ActivityMainBinding
import com.driff.android.module.ui.picture.PicturesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragment()
    }

    private fun addFragment() {
        val fragment = PicturesFragment.newInstance()
        supportFragmentManager.commitNow {
            add(R.id.fragment_container, fragment)
        }
    }

}