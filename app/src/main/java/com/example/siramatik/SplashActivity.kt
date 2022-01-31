package com.example.siramatik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.google.gson.annotations.Until
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        val topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation)
        resim.startAnimation(topAnim)
        Handler().postDelayed({
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            },3000)

    }

}


