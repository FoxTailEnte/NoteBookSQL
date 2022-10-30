package com.example.notebooksqldntraining

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.notebooksqldntraining.databinding.ActivityEditBinding
import com.example.notebooksqldntraining.db.MyDbManager

class EditActivity : AppCompatActivity() {
    private val myDbManager = MyDbManager(this)
    private val imageRequestCode = 10
    private var tempImageUri = "empty"
    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == 10){
            binding.imMainImage.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
        }
    }
    private fun onClick() = with(binding){
        bAddImage.setOnClickListener {
            mainImageLayout.visibility = View.VISIBLE
            bAddImage.visibility = View.GONE
        }
        imButtonDelete.setOnClickListener {
            mainImageLayout.visibility = View.GONE
            bAddImage.visibility = View.VISIBLE
            tempImageUri = "empty"
        }
        imButtonEditImage.setOnClickListener {
            var intent =Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, imageRequestCode)
        }
        bSave.setOnClickListener {
            val myTitle = edTitle.text.toString()
            val myDesc = edDesc.text.toString()
            if(myTitle !== "" && myDesc !== ""){
                myDbManager.insertToDb(myTitle,myDesc,tempImageUri)
            }
        }

    }

}