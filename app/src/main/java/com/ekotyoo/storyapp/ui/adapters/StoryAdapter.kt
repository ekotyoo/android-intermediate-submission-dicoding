package com.ekotyoo.storyapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.ekotyoo.storyapp.databinding.StoryListItemBinding
import com.ekotyoo.storyapp.model.StoryModel
import com.ekotyoo.storyapp.ui.home.HomeFragmentDirections
import com.ekotyoo.storyapp.utils.withDateFormat

class StoryAdapter : PagingDataAdapter<StoryModel, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding =
            StoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = getItem(position)
        story?.let { holder.bind(it) }
    }

    inner class ListViewHolder(var binding: StoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryModel) {
            Glide.with(binding.root)
                .load(story.imageUrl)
                .transition(
                    withCrossFade(
                        DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
                    )
                )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivImage)
            binding.apply {
                tvName.text = story.name
                tvDate.text = story.createdAt?.withDateFormat()
                root.setOnClickListener {
                    Navigation.findNavController(root).navigate(
                        HomeFragmentDirections.actionHomeFragmentToStoryDetailFragment(
                            story
                        )
                    )
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<StoryModel>() {
                override fun areItemsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean =
                    oldItem.id == newItem.id
            }
    }
}