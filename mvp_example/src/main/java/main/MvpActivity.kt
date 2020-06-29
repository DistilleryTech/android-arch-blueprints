package main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.distillery.android.mvp_example.R
import main.view.TodoFragment

class MvpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvp)
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                TodoFragment(),
                "TodoFragment"
            ).commit()
    }
}
