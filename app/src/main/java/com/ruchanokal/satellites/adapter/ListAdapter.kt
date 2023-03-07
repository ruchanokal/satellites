package com.ruchanokal.satellites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ruchanokal.satellites.ui.DetailFragmentArgs
import com.ruchanokal.satellites.ui.ListFragmentDirections
import com.ruchanokal.satellites.R
import com.ruchanokal.satellites.databinding.ListRowBinding
import com.ruchanokal.satellites.model.SatelliteModel

class ListAdapter(var mySatelliteList : List<SatelliteModel>) : RecyclerView.Adapter<ListAdapter.ListHolder>() {


    class ListHolder(var binding: ListRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val binding = ListRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListHolder(binding)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val activeness = mySatelliteList.get(position).active

        if (activeness){
            holder.binding.activenessImageView.setImageResource(R.drawable.greencircle)
            holder.binding.activenessTextView.setText("Active")

            holder.binding.satelliteTextView.alpha = 1F
            holder.binding.activenessTextView.alpha = 1F
        } else{
            holder.binding.activenessImageView.setImageResource(R.drawable.redcircle)
            holder.binding.activenessTextView.setText("Passive")

            holder.binding.satelliteTextView.alpha = 0.3F
            holder.binding.activenessTextView.alpha = 0.3F
        }

        holder.binding.satelliteTextView.setText(mySatelliteList.get(position).name)

        holder.itemView.setOnClickListener {

            val action = ListFragmentDirections.actionListFragmentToDetailFragment(mySatelliteList.get(position))
            Navigation.findNavController(it).navigate(action)

        }



    }

    override fun getItemCount(): Int {
        return mySatelliteList.size
    }

}