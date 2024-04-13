package com.example.lungsguardian.ui.home.history

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lungsguardian.data.model.Value
import com.example.lungsguardian.databinding.ItemReportBinding
import com.example.lungsguardian.utils.BASE_URL_BACK
import com.example.lungsguardian.utils.BASE_URL_IMAGE
import java.time.LocalDate

class AdapterHistoryRecyclerView : RecyclerView.Adapter<AdapterHistoryRecyclerView.Holder>() {

    var reportList: ArrayList<Value> = ArrayList()
   // var onItemClick: OnItemClick?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemReportBinding.inflate(LayoutInflater.from(parent.context)
            , parent, false))
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(reportList[position])
    }

    /*fun updateList(list:ArrayList<Value>){
        this.reportList=list
        notifyDataSetChanged()
    }*/
    fun setData(newReportList:ArrayList<Value>){
        val diffUtil = DiffUtilReport(reportList,newReportList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        reportList=newReportList
        diffResults.dispatchUpdatesTo(this)
    }

    inner class Holder(private val itemReportBinding: ItemReportBinding) :
        RecyclerView.ViewHolder(itemReportBinding.root) {
        fun bind(value: Value) {
            Glide.with(itemReportBinding.root.context)
                .load("$BASE_URL_IMAGE${value.image}")
                .into(itemReportBinding.imageReport)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                itemReportBinding.textDate.text = LocalDate.now().toString()
            } else {
                itemReportBinding.textDate.text = value.`$id`
            }
            itemReportBinding.textReport.text = value.caption
        }
       /* init {
            itemReportBinding.root.setOnClickListener{

                onItemClick?.onClick(reportList[layoutPosition].`$id`.toInt())

                notifyDataSetChanged()
            }
        }*/
    }
    /*interface OnItemClick{
        fun onClick(id: Int)
    }*/
}
