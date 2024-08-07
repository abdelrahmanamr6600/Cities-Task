package com.klivvr.cities.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.klivvr.cities.databinding.CityCellCardBinding
import com.klivvr.cities.model.CityItem
import com.klivvr.cities.model.Coord

class CitiesAdapter:RecyclerView.Adapter<CitiesAdapter.CityViewHolder>() {
    lateinit var onCityCardClick:((Coord)->Unit)
    private val diffUtil = object: DiffUtil.ItemCallback<CityItem>() {
        override fun areItemsTheSame(oldItem: CityItem, newItem: CityItem): Boolean {
            return (oldItem.name==newItem.name)
        }

        override fun areContentsTheSame(oldItem: CityItem, newItem: CityItem): Boolean {
            return oldItem == newItem
        }
    }
    val diff = AsyncListDiffer(this,diffUtil)

   inner class CityViewHolder(var cityCellCardBinding: CityCellCardBinding):ViewHolder(cityCellCardBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding =
            CityCellCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.cityCellCardBinding.titleTv.text = "${diff.currentList[position].name},${diff.currentList[position].country}"
        holder.cityCellCardBinding.subtitleTv.text =" ${diff.currentList[position].coord.lat} , ${diff.currentList[position].coord.lon} "

        holder.itemView.setOnClickListener {
            onCityCardClick.invoke(diff.currentList[position].coord)
        }


    }
}