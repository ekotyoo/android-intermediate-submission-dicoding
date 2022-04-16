package com.ekotyoo.storyapp.ui.storydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ekotyoo.storyapp.databinding.FragmentStoryDetailBinding
import com.ekotyoo.storyapp.utils.withDateFormat

class StoryDetailFragment: Fragment() {

    private var _binding: FragmentStoryDetailBinding? = null
    private val binding get() = _binding!!

    val args: StoryDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val story = args.story

        binding.apply {
            tvName.text = story.name
            tvDate.text = story.createdAt?.withDateFormat()
            tvDesc.text = story.caption
        }

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        Glide.with(requireContext())
            .load(story.imageUrl)
            .into(binding.ivImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}