package com.example.notebooksqldntraining

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.notebooksqldntraining.databinding.ActivityEditBinding
import com.example.notebooksqldntraining.db.MyDbManager
import com.example.notebooksqldntraining.db.MyIntents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
     val myDbManager = MyDbManager(this)
     var id = 0
     var isEditState = false
     val imageRequestCode = 10
     var tempImageUri = "empty"
    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMyIntents()
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
        if(resultCode == Activity.RESULT_OK && requestCode == imageRequestCode){
            binding.imMainImage.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
            contentResolver.takePersistableUriPermission(data?.data!!,Intent.FLAG_GRANT_READ_URI_PERMISSION)
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
        }
        imButtonEditImage.setOnClickListener {
            val intent =Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            startActivityForResult(intent, imageRequestCode)
        }
        bSave.setOnClickListener {
            val myTitle = edTitle.text.toString()
            val myDesc = edDesc.text.toString()

            if(myTitle !== "" && myDesc !== ""){
                CoroutineScope(Dispatchers.Main).launch {
                    if(isEditState){
                        val iri = intent.getStringExtra(MyIntents.I_URI_KEY).toString()
                    myDbManager.updateItem(myTitle,myDesc,iri, id)
                }else{
                        myDbManager.insertToDb(myTitle,myDesc,tempImageUri)
                }
                    finish()
                }
            }
        }
    }

    private fun getMyIntents()= with(binding){
        val i = intent
        if(i !== null){
            if(i.getStringExtra(MyIntents.I_TITLE_KEY) != null){
                imButtonEditImage.visibility = View.GONE
                imButtonDelete.visibility = View.GONE
                edTitle.setText(i.getStringExtra(MyIntents.I_TITLE_KEY))
                isEditState = true
                edDesc.setText(i.getStringExtra(MyIntents.I_DESCRIPTION_KEY))
                id = i.getIntExtra(MyIntents.I_ID_KEY, 0)
                if(i.getStringExtra(MyIntents.I_URI_KEY) != "empty"){

                    mainImageLayout.visibility = View.VISIBLE
                    bAddImage.visibility = View.GONE
                    imMainImage.setImageURI(Uri.parse(i.getStringExtra(MyIntents.I_URI_KEY)))
                }
            }
        }
    }

}