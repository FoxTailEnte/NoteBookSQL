package com.example.notebooksqldntraining

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notebooksqldntraining.adapter.RcViewAdapter
import com.example.notebooksqldntraining.databinding.ActivityMainBinding
import com.example.notebooksqldntraining.db.MyDbManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val myDbManager = MyDbManager(this)
    private val adapter = RcViewAdapter(ArrayList())
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRc()
        initSearchView()
        onClick()

    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter("")
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    private fun initRc()= with(binding){
        rcMain.layoutManager = LinearLayoutManager(this@MainActivity)
        val swapManager = getSwapManger()
        swapManager.attachToRecyclerView(rcMain)
        rcMain.adapter = adapter
    }

    private fun initSearchView(){
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fillAdapter(newText!!)
                return true
            }

        })

    }

    private fun fillAdapter(text: String){
        job?.cancel()
       job = CoroutineScope(Dispatchers.Main).launch{
            val list = myDbManager.readDbData(text)
            adapter.updateAdapter(list)
            if(list.size > 0){
                binding.tvNoElements.visibility = View.GONE
            }else{
                binding.tvNoElements.visibility = View.VISIBLE
            }
        }
    }

    private fun onClick() = with(binding) {
        bAdd.setOnClickListener {
            startActivity(Intent(this@MainActivity, EditActivity::class.java))
        }
    }

    private fun getSwapManger(): ItemTouchHelper{
        return ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                   return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    adapter.removeItem(viewHolder.adapterPosition, myDbManager)
                }
            })
    }
}