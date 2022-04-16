package com.ekotyoo.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekotyoo.storyapp.R
import com.ekotyoo.storyapp.databinding.FragmentHomeBinding
import com.ekotyoo.storyapp.databinding.PopupMenuBinding
import com.ekotyoo.storyapp.model.UserModel
import com.ekotyoo.storyapp.ui.adapters.LoadingStateAdapter
import com.ekotyoo.storyapp.ui.adapters.StoryAdapter
import com.ekotyoo.storyapp.ui.maps.MapsFragment
import com.ekotyoo.storyapp.utils.Utils
import com.ekotyoo.storyapp.utils.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

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
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        val adapterWithLoading =
            adapter.withLoadStateFooter(footer = LoadingStateAdapter { adapter.retry() })
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvStory.layoutManager = layoutManager
        binding.rvStory.adapter = adapterWithLoading

        binding.swipeLayout.setOnRefreshListener {
            adapter.refresh()
            lifecycleScope.launch {
                adapter.loadStateFlow.distinctUntilChanged { old, new ->
                    (old.mediator?.prepend?.endOfPaginationReached == true) ==
                            (new.mediator?.prepend?.endOfPaginationReached == true)
                }
                    .filter { it.refresh is LoadState.NotLoading && it.prepend.endOfPaginationReached && !it.append.endOfPaginationReached }
                    .collect {
                        binding.rvStory.smoothScrollToPosition(0)
                    }
            }
            binding.swipeLayout.isRefreshing = false
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collect {
                binding.loadingProgressBar.isVisible = (it.refresh is LoadState.Loading)
                binding.tvEmpty.isVisible = adapter.itemCount == 0
                if (it.refresh is LoadState.Error) {
                    Utils.showToast(
                        requireContext(),
                        (it.refresh as LoadState.Error).error.localizedMessage?.toString()
                            ?: getString(R.string.load_stories_failed)
                    )
                }
            }
        }

        binding.btnAddStory.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPostStoryFragment())
        }

        binding.btnMap.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMapsFragment(action = MapsFragment.ACTION_STORIES))
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
        }

        viewModel.stories.observe(viewLifecycleOwner) { stories ->
            adapter.submitData(lifecycle, stories)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Utils.showToast(requireContext(), message)
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