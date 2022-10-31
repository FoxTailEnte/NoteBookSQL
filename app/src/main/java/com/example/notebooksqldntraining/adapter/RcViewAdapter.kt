package com.example.notebooksqldntraining.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notebooksqldntraining.EditActivity
import com.example.notebooksqldntraining.R
import com.example.notebooksqldntraining.databinding.RcItemBinding
import com.example.notebooksqldntraining.db.ListItem
import com.example.notebooksqldntraining.db.MyDbManager
import com.example.notebooksqldntraining.db.MyIntents

class RcViewAdapter(listMain: ArrayList<ListItem>): RecyclerView.Adapter<RcViewAdapter.RcViewHolder>() {
    private var listArray = listMain
    class RcViewHolder(view: View): RecyclerView.ViewHolder(view){
        private var binding = RcItemBinding.bind(view)
        fun setData(item: ListItem)= with(binding){
            tvTitle.text = item.title
            itemView.setOnClickListener {
                val intent = Intent(binding.root.context, EditActivity::class.java).apply {
                    putExtra(MyIntents.I_TITLE_KEY, item.title)
                    putExtra(MyIntents.I_DESCRIPTION_KEY, item.description)
                    putExtra(MyIntents.I_URI_KEY, item.uri)
                    putExtra(MyIntents.I_ID_KEY, item.id)
                }
                binding.root.context.startActivity(intent)

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RcViewHolder {
        val view = LayoutInflater.from(parent.context).
                inflate(R.layout.rc_item,parent,false)
        return RcViewHolder(view)
    }

    override fun onBindViewHolder(holder: RcViewHolder, position: Int) {
        holder.setData(listArray.get(position))
    }

    override fun getItemCount(): Int {
        return listArray.size
    }
    fun updateAdapter(listItem: List<ListItem>){
        listArray.clear()
        listArray.addAll(listItem)
        notifyDataSetChanged()
    }
    fun removeItem(position: Int, dbManager: MyDbManager) {
        dbManager.removeItemFromDb(listArray[position].id.toString())
        listArray.removeAt(position)
        notifyItemRangeChanged(0,listArray.size)
        notifyItemRemoved(position)
    }
}