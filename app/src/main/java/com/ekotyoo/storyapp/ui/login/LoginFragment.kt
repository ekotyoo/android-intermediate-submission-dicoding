package com.ekotyoo.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ekotyoo.storyapp.R
import com.ekotyoo.storyapp.databinding.FragmentLoginBinding
import com.ekotyoo.storyapp.ui.signup.SignupViewModel
import com.ekotyoo.storyapp.utils.ViewModelFactory
import com.ekotyoo.storyapp.utils.showToast

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        playAnimation()
        observeViewModel()
    }

    private fun setupView() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            val message = getString(R.string.must_not_be_empty)

            if (email.isEmpty()) {
                binding.etEmail.error = message
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPassword.error = message
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }

        binding.tvOrSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.etEmail.text?.clear()
    }

    private fun playAnimation() {
        val greeting = ObjectAnimator.ofFloat(binding.tvGreeting, View.ALPHA, 1F).setDuration(500L)
        val lottie = ObjectAnimator.ofFloat(binding.lottieAuth, View.ALPHA, 1f).setDuration(500L)
        val loginAlpha = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1F).setDuration(500L)
        val loginTranslateX = ObjectAnimator.ofFloat(binding.tvLogin, View.TRANSLATION_X, -100F, 0F).setDuration(500L)
        val etEmailAlpha = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1F).setDuration(500L)
        val etEmailTranslateX = ObjectAnimator.ofFloat(binding.etEmail, View.TRANSLATION_X, -100F, 0F).setDuration(500L)
        val etPassAlpha = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1F).setDuration(500L)
        val etPassTranslateX = ObjectAnimator.ofFloat(binding.etPassword, View.TRANSLATION_X, -100F, 0F).setDuration(500L)
        val btnLoginAlpha = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1F).setDuration(500L)
        val btnLoginTranslateX = ObjectAnimator.ofFloat(binding.btnLogin, View.TRANSLATION_X, -100F, 0F).setDuration(500L)
        val orSignup = ObjectAnimator.ofFloat(binding.tvOrSignup, View.ALPHA, 1F).setDuration(500L)

        val login = AnimatorSet().apply { playTogether(loginAlpha, loginTranslateX) }
        val email = AnimatorSet().apply { playTogether(etEmailAlpha, etEmailTranslateX) }
        val password = AnimatorSet().apply { playTogether(etPassAlpha, etPassTranslateX) }
        val btnLogin = AnimatorSet().apply { playTogether(btnLoginAlpha, btnLoginTranslateX) }

        AnimatorSet().apply {
            playSequentially(greeting, lottie, login, email, password, btnLogin, orSignup)
            start()
        }
    }

    private fun observeViewModel() {
        viewModel.userModel.observe(viewLifecycleOwner) { userModel ->
            if (userModel?.isLoggedIn == true) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
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