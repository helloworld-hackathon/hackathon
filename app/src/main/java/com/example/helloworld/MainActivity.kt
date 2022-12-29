package com.example.helloworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottom_navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        bottom_navigation = findViewById(R.id.bottomNavigationView) //as BottomNavigationView

        // bottomNavigationView 설정
        bottom_navigation.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.tab_calendar -> {                    val NutritionalFragment = NutritionalFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, NutritionalFragment).commit()
                    return@setOnItemSelectedListener true
                }

            }
            return@setOnItemSelectedListener false
        }

    }
}