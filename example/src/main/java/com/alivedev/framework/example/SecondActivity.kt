package com.alivedev.framework.example

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.akashic.framework.ext.singleClick
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/page/2")
class SecondActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<TextView>(R.id.tv).apply {
            text = "第二个界面"
            singleClick {
                setResult(RESULT_OK, Intent().apply {
                    putExtra("tip","123")
                })
                finish()
            }
        }
    }
}