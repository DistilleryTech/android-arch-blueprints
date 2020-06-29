package com.distillery.android.blueprints

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.distillery.android.blueprints.mvvm.MvvmActivity
import kotlinx.android.synthetic.main.activity_main.*
import main.MvpActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mvvm.setOnClickListener {
            startActivity(Intent(this, MvvmActivity::class.java))
        }

        mvp.setOnClickListener{
            val intent = Intent(this, MvpActivity::class.java)
            startActivity(intent)
        }
    }
}
