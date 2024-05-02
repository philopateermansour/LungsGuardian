package com.example.lungsguardian.ui.home.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.lungsguardian.R
import com.example.lungsguardian.data.model.Value
import com.example.lungsguardian.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HistoryFragment : Fragment() {


    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!


    private val historyViewModel :HistoryViewModel by viewModels()
    private val historyAdapter:AdapterHistoryRecyclerView by lazy { AdapterHistoryRecyclerView() }
    private var reportsList : ArrayList<Value> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyViewModel.showHistory()
        observers()
        onClicks()
    }

    private fun enableDelete() {
        if (reportsList.isEmpty()){
            binding.btnDeleteHistory.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.gray)
            binding.btnDeleteHistory.isClickable=false
        } else if(reportsList.isNotEmpty()){
            binding.btnDeleteHistory.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
            binding.btnDeleteHistory.isClickable=true
        }
    }
    private fun onClicks() {
        /*historyAdapter.onItemClick = object :AdapterHistoryRecyclerView.OnItemClick{
            override fun onClick(id: Int) {
            }
        }*/
        binding.btnDeleteHistory.setOnClickListener {
            historyViewModel.deleteReports()
        }
        binding.searchBar.doAfterTextChanged{
            val query = it.toString()
            if (query.isEmpty()){
                reportsList.let { it1 -> historyAdapter.setData(it1) }
                return@doAfterTextChanged
            }
            val newList = reportsList.filter {
                it.caption.lowercase().contains(query.lowercase())
            }
            historyAdapter.setData(newList as ArrayList<Value>)
        }
    }
    private fun observers() {
        historyViewModel.historyValidate.observe(viewLifecycleOwner){
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        historyViewModel.responseLiveData.observe(viewLifecycleOwner){
            reportsList = it.body()?.`$values` as ArrayList<Value>
            historyAdapter.setData(reportsList)
            binding.recyclerHistory.adapter=historyAdapter
            enableDelete()
        }
        historyViewModel.deleteLiveData.observe(viewLifecycleOwner){
            historyViewModel.showHistory()
            reportsList.clear()
            enableDelete()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}