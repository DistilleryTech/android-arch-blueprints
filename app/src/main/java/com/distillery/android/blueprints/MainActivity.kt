package com.distillery.android.blueprints

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.distillery.android.blueprints.databinding.ActivityMainBinding
import com.distillery.android.blueprints.mvi.MviActivity
import com.distillery.android.blueprints.mvvm.MvvmActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

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

        mvp.setOnClickListener{
            val intent = Intent(this, MvpActivity::class.java)
            startActivity(intent)
        }
    }
}
