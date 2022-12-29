package com.example.helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.example.helloworld.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottom_navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        bottom_navigation = findViewById(R.id.bottomNavigationView) //as BottomNavigationView

//        // bottomNavigationView 설정
//        bottom_navigation.setOnItemSelectedListener { item->
//            when(item.itemId){
//                R.id.tab_share -> {                    val shareFragment = NutritionalFragment()
//                    supportFragmentManager.beginTransaction().replace(R.id.main_content, shareFragment).commit()
//                    return@setOnItemSelectedListener true
//                }
//
//            }
//            return@setOnItemSelectedListener false
//        }

    }
}