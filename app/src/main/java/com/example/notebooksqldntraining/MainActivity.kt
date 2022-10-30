package com.example.notebooksqldntraining

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.notebooksqldntraining.databinding.ActivityEditBinding
import com.example.notebooksqldntraining.databinding.ActivityMainBinding
import com.example.notebooksqldntraining.db.MyDbManager
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val myDbManager = MyDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    private fun onClick() = with(binding) {
        bAdd.setOnClickListener {
            startActivity(Intent(this@MainActivity, EditActivity::class.java))
        }

    }


}