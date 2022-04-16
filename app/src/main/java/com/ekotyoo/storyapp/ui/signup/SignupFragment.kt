package com.ekotyoo.storyapp.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ekotyoo.storyapp.R
import com.ekotyoo.storyapp.databinding.FragmentSignupBinding
import com.ekotyoo.storyapp.utils.ViewModelFactory
import com.ekotyoo.storyapp.utils.showToast

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignupViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeViewModel()
        playAnimation()
    }

    private fun setupView() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSignup.setOnClickListener {
            val name = binding.etNameSignup.text.toString().trim()
            val email = binding.etEmailSignup.text.toString().trim()
            val password = binding.etPasswordSignup.text.toString().trim()

            val message = getString(R.string.must_not_be_empty)

            if (name.isEmpty()) {
                binding.etNameSignup.error = message
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.etNameSignup.error = message
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etNameSignup.error = message
                return@setOnClickListener
            }

            viewModel.signup(name, email, password)
        }
    }

    private fun playAnimation() {
        val greeting = ObjectAnimator.ofFloat(binding.tvGreeting, View.ALPHA, 1F).setDuration(500L)
        val lottie = ObjectAnimator.ofFloat(binding.lottieAuth, View.ALPHA, 1f).setDuration(500L)

        val signupAlpha = ObjectAnimator.ofFloat(binding.tvSignup, View.ALPHA, 1F).setDuration(500L)
        val signupTranslateX = ObjectAnimator.ofFloat(binding.tvSignup, View.TRANSLATION_X, -100F, 0F).setDuration(500L)
        val etNameAlpha = ObjectAnimator.ofFloat(binding.etNameSignup, View.ALPHA, 1F).setDuration(500L)
        val etNameTranslateX = ObjectAnimator.ofFloat(binding.etNameSignup, View.TRANSLATION_X, -100F, 0F).setDuration(500L)
        val etEmailAlpha = ObjectAnimator.ofFloat(binding.etEmailSignup, View.ALPHA, 1F).setDuration(500L)
        val etEmailTranslateX = ObjectAnimator.ofFloat(binding.etEmailSignup, View.TRANSLATION_X, -100F, 0F).setDuration(500L)
        val etPassAlpha = ObjectAnimator.ofFloat(binding.etPasswordSignup, View.ALPHA, 1F).setDuration(500L)
        val etPassTranslateX = ObjectAnimator.ofFloat(binding.etPasswordSignup, View.TRANSLATION_X, -100F, 0F).setDuration(500L)
        val btnSignupAlpha = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1F).setDuration(500L)
        val btnSignupTranslateX = ObjectAnimator.ofFloat(binding.btnSignup, View.TRANSLATION_X, -100F, 0F).setDuration(500L)

        val signup = AnimatorSet().apply { playTogether(signupAlpha, signupTranslateX) }
        val name = AnimatorSet().apply { playTogether(etNameAlpha, etNameTranslateX) }
        val email = AnimatorSet().apply { playTogether(etEmailAlpha, etEmailTranslateX) }
        val password = AnimatorSet().apply { playTogether(etPassAlpha, etPassTranslateX) }
        val btnSignup = AnimatorSet().apply { playTogether(btnSignupAlpha, btnSignupTranslateX) }

        AnimatorSet().apply {
            playSequentially(greeting, lottie, signup, name, email, password, btnSignup)
            start()
        }
    }

    private fun observeViewModel() {
        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                showToast(requireContext(), "Successfully signup, please continue to login.")
                findNavController().navigateUp()
            }
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