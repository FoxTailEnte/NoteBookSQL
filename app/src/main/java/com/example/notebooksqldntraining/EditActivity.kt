package com.example.notebooksqldntraining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notebooksqldntraining.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    private lateinit var bining: ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bining = ActivityEditBinding.inflate(layoutInflater)
        setContentView(bining.root)
    }
}