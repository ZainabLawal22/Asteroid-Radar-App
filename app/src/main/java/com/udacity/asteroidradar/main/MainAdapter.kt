package com.udacity.asteroidradar.main


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidItemBinding

//class MainAdapter(private val clickListener: AsteroidListener) :
//    PagingDataAdapter<Asteroid, MainAdapter.AsteroidViewHolder>(AsteroidComparator) {
//
//    object AsteroidComparator : DiffUtil.ItemCallback<Asteroid>() {
//        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//    class AsteroidViewHolder(private var binding: AsteroidItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(asteroid: Asteroid) {
//            binding.asteroid = asteroid
//            binding.executePendingBindings()
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
//        val withDataBinding: AsteroidItemBinding = AsteroidItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return AsteroidViewHolder(withDataBinding)
//    }
//
//    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
//        val asteroid = getItem(position)
//        if (asteroid != null) {
//            holder.bind(asteroid)
//            holder.itemView.setOnClickListener {
//                clickListener.onClick(asteroid)
//            }
//        }
//    }
//
//    class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
//        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
//    }
//}


class MainAdapter(private val clickListener: AsteroidListener) :
    PagingDataAdapter<Asteroid, MainAdapter.AsteroidViewHolder>(AsteroidComparator) {

    // Comparator to check if items and their contents are the same
    object AsteroidComparator : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    // ViewHolder class to bind the asteroid data to the UI and handle click events
    class AsteroidViewHolder(
        private var binding: AsteroidItemBinding,
        private val clickListener: AsteroidListener
    ) : RecyclerView.ViewHolder(binding.root) {

        // Binds the asteroid data to the view and sets the click listener
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings() // Execute pending bindings immediately
            itemView.setOnClickListener { clickListener.onClick(asteroid) }
        }
    }

    // Inflates the view for the RecyclerView items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val withDataBinding: AsteroidItemBinding = AsteroidItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AsteroidViewHolder(withDataBinding, clickListener)
    }

    // Binds the data to the ViewHolder, with null-checks in case of placeholders
    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        getItem(position)?.let { asteroid ->
            holder.bind(asteroid) // Binds only non-null items
        }
    }

    // Listener class to handle click events
    class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}




