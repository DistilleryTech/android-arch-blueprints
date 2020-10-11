package com.distillery.android.blueprints

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.distillery.android.blueprints.databinding.ActivityMainBinding
import com.distillery.android.blueprints.mvi.MviActivity
import com.distillery.android.blueprints.mvvm.MvvmActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.distillery.android.blueprints.mvp.MvpActivity

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mvvm.setOnClickListener {
            startActivity<MvvmActivity>()
        }

        binding.mvi.setOnClickListener {
            startActivity<MviActivity>()
        }

        binding.mvp.setOnClickListener{
            startActivity<MvpActivity>()
        }
    }
}
