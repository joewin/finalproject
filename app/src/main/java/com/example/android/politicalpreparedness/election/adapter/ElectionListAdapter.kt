package com.example.android.politicalpreparedness.election.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ItemsElectionLayoutBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }
    //Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener,item)
    }

    //Create ElectionListener
    class ElectionListener(val clickListener: (election: Election) -> Unit) {

        fun onClick(election: Election) = clickListener(election)

    }
    //Create ElectionDiffCallback
    class ElectionDiffCallBack: DiffUtil.ItemCallback<Election>(){

        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem == newItem
        }
    }
}

//Create ElectionViewHolder
class ElectionViewHolder private constructor(val binding:ItemsElectionLayoutBinding):
    RecyclerView.ViewHolder(binding.root){

    fun bind(clickListener: ElectionListAdapter.ElectionListener, item: Election){
        Log.i("Name", item.name)
        binding.election= item
        binding.executePendingBindings()

        itemView.setOnClickListener {
            clickListener.onClick(item)
        }
    }
    //Add companion object to inflate ViewHolder (from)
    companion object {
        fun from(parent: ViewGroup):ElectionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemsElectionLayoutBinding.inflate(layoutInflater, parent, false)
            return ElectionViewHolder(binding)
        }
    }

}



