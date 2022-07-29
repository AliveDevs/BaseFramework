package com.alivedev.framework.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.akashic.framework.ext.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv).apply {
            text = "第一个界面"
            singleClick {
                "/page/2".asRoute.go().forResult {
                    toast(it.data?.getStringExtra("tip")?:"1")
                }
            }
        }
    }
}