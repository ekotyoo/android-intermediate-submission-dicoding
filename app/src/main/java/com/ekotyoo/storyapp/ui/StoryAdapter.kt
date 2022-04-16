package com.ekotyoo.storyapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ekotyoo.storyapp.databinding.StoryListItemBinding
import com.ekotyoo.storyapp.model.StoryModel

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    private val differ =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<StoryModel>() {
            override fun areItemsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean =
                oldItem == newItem

        })

    class ListViewHolder(var binding: StoryListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding =
            StoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = differ.currentList[position]
        Glide.with(holder.itemView.context)
            .load(story.imageUrl)
            .into(holder.binding.ivImage)
        holder.binding.apply {
            tvName.text = story.name
            tvDate.text = story.createdAt
            root.setOnClickListener {
                onItemClickCallback.onItemClicked(story)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<StoryModel>) {
        differ.submitList(list)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(story: StoryModel)
    }
}