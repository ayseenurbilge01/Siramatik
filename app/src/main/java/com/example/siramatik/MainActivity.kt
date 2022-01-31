package com.example.siramatik

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.siramatik.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var sharedpreferences=null
    private lateinit var binding: ActivityMainBinding
    var data:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras

        data = bundle!!.getString("Id", "Default")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }
    fun getMyId():String{
        return data.toString();
    }

    fun logOut(){
            var sharedpreferences = getSharedPreferences("username", Context.MODE_PRIVATE);
            var editor =sharedpreferences.edit()
            editor.putString("loginusername"," ")
            editor.commit()

            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
    }

}
