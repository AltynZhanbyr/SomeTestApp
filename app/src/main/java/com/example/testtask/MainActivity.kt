package com.example.testtask

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testtask.databinding.ActivityMainBinding

const val PREFERENCE_NAME = "preference"
const val BASE_URL = "http://app.zaimforyou.ru/hello"
const val ID_TEXT = "idText"
const val UUID_TEXT = "uuidText"

class MainActivity : AppCompatActivity() {

    private var binding:ActivityMainBinding? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        sharedPreferences = getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)
    }

    override fun onStart() {
        super.onStart()

        val idText = sharedPreferences.getString(ID_TEXT,"")
        val uuidText = sharedPreferences.getString(UUID_TEXT,"")

        binding?.textView1?.text = "id: $idText\nuuid: $uuidText"

        binding?.startButton?.setOnClickListener {
            startActivity(Intent(this,WebViewActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}