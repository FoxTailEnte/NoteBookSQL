package com.example.notebooksqldntraining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        val dataList = myDbManager.readDbData()
        for (item in dataList) {
            binding.tvTest.append(item)
            binding.tvTest.append("\n")
        }
    }

    fun onClickSave(view: View) = with(binding) {
        bSave.setOnClickListener {
            tvTest.text = ""
            myDbManager.insertToDb(edTitle.text.toString(), edContent.text.toString())
            val dataList = myDbManager.readDbData()
            for (item in dataList) {
                tvTest.append(item)
                tvTest.append("\n")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }
}