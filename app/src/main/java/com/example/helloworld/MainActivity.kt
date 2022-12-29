package com.example.helloworld

import android.content.Intent
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
                R.id.tab_nutritional -> {                    val NutritionalFragment = NutritionalFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, NutritionalFragment).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.tab_calendar -> {
                    val intent = Intent(this, Calendar::class.java)
                    this.startActivity(intent)
                    overridePendingTransition(0, 0); //애니메이션 없애기
                }
                R.id.tab_community -> {
                    val intent = Intent(this, CommunityActivity::class.java)
                    this.startActivity(intent)
                    overridePendingTransition(0, 0); //애니메이션 없애기
                }
                R.id.tab_alarm -> {
                    val intent = Intent(this, AlarmActivity::class.java)
                    this.startActivity(intent)
                    overridePendingTransition(0, 0); //애니메이션 없애기
                }

            }
            return@setOnItemSelectedListener false
        }
        val NutritionalFragment = NutritionalFragment()
        supportFragmentManager.beginTransaction().add(R.id.main_content, NutritionalFragment).commit()

    }
}