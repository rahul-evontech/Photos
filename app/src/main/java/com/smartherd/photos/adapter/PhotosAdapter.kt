package com.smartherd.photos.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.smartherd.photos.R
import com.smartherd.photos.models.Hit
import com.smartherd.photos.models.Photos
import kotlinx.android.synthetic.main.recycler_items.view.*

class PhotosAdapter: RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    val differCallbacks = object : DiffUtil.ItemCallback<Hit>(){
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.largeImageURL == newItem.largeImageURL
        }

        override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallbacks)

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.e("hello","on create view holder")
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_items,
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = differ.currentList[position]
        holder.itemView.apply {

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)

            Log.e("hello","on bind view holder")
            Glide.with(context)
                .load(differ.currentList[position].largeImageURL)
                .apply(requestOptions)
                .into(imageView)


            setOnClickListener {
                onItemClickListener?.let {
                    it(pos)
                }
            }
        }
    }

    private var onItemClickListener: ((Hit) -> Unit)? = null

    fun setOnItemClickListener(listener: ((Hit) -> Unit)){
        onItemClickListener = listener
    }
}