package com.example.siramatik

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.siramatik.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var sharedpreferences: Nothing? =null
    private lateinit var binding: ActivityMainBinding
    private var data:String? = null
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId =101
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

        createNotificationChannel()

    }
    fun getMyId():String{
        return data.toString()
    }

    fun logOut(){
            val sharedpreferences = getSharedPreferences("username", Context.MODE_PRIVATE)
        val editor =sharedpreferences.edit()
            editor.putString("loginusername"," ")
            editor.apply()

            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
    }
    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val name = "Sıranız yaklaşıyor"
            val descriptionText = ""
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description=descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    fun sendNotification()
    {
        val builder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher1_round)
            .setContentTitle("Sıranız yaklaşıyor")
            .setContentText("Önünüzde 3 kişi kaldı")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId,builder.build())
        }

    }

}
