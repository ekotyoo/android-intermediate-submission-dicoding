package com.ekotyoo.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekotyoo.storyapp.R
import com.ekotyoo.storyapp.databinding.FragmentHomeBinding
import com.ekotyoo.storyapp.databinding.PopupMenuBinding
import com.ekotyoo.storyapp.model.StoryModel
import com.ekotyoo.storyapp.model.UserModel
import com.ekotyoo.storyapp.ui.StoryAdapter
import com.ekotyoo.storyapp.utils.ViewModelFactory
import com.ekotyoo.storyapp.utils.showToast

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: UserModel

    private lateinit var adapter: StoryAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeViewModel()
    }

    private fun setupView() {
        setupPopupMenu()
        adapter = StoryAdapter().apply {
            setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
                override fun onItemClicked(story: StoryModel) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToStoryDetailFragment(
                            story
                        )
                    )
                }
            })
        }

        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.rvStory.smoothScrollToPosition(0)
            }
        })

        binding.rvStory.layoutManager = layoutManager
        binding.rvStory.adapter = adapter

        binding.btnAddStory.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_postStoryFragment)
        }
    }

    private fun setupPopupMenu() {
        val popupBinding = PopupMenuBinding.inflate(layoutInflater)
        val popupWindow = PopupWindow(
            popupBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            isFocusable = true
            elevation = 10F
        }

        binding.btnMenu.setOnClickListener { btn ->
            popupWindow.showAsDropDown(btn, 0, -btn.height)
        }

        popupBinding.tvMenuLanguages.setOnClickListener {
            popupWindow.dismiss()
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        popupBinding.tvMenuLogout.setOnClickListener {
            popupWindow.dismiss()
            viewModel.logout()
        }
    }

    private fun observeViewModel() {
        viewModel.userModel.observe(viewLifecycleOwner) { userModel ->
            if (userModel?.isLoggedIn == false) {
                findNavController().navigateUp()
            }
            this.user = userModel
            viewModel.getStories(user.token ?: "")
        }

        viewModel.stories.observe(viewLifecycleOwner) { stories ->
            layoutManager.scrollToPositionWithOffset(0, 0)
            adapter.submitList(stories)
            binding.tvEmpty.isGone = stories.isNotEmpty()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            showToast(requireContext(), message)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            showLoading(state)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}