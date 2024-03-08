package com.example.lungsguardian.ui.home.history

import androidx.recyclerview.widget.DiffUtil
import com.example.lungsguardian.data.model.Value

class DiffUtilReport(private val oldList:ArrayList<Value>,
                     private val newList :ArrayList<Value>) :DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].`$id`==newList[newItemPosition].`$id`
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].`$id` !=newList[newItemPosition].`$id` ->{
                false
            }
            oldList[oldItemPosition].image !=newList[newItemPosition].image ->{
                false
            }
            oldList[oldItemPosition].caption !=newList[newItemPosition].caption ->{
                false
            }
            else ->{
                true
            }
        }
    }
}