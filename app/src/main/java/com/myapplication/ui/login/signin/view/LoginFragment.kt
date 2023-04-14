package com.myapplication.ui.login.signin.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.myapplication.R
import com.myapplication.core.Response
import com.myapplication.databinding.FragmentLoginBinding
import com.myapplication.ui.login.signin.viewmodel.SignViewModel
import com.myapplication.util.extension.snackbar
import com.myapplication.util.extension.validate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var _binding: FragmentLoginBinding
    val binding: FragmentLoginBinding get() = _binding
    private val signViewModel: SignViewModel by viewModels()
    private var loadingDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.toolbarLogin.setNavigationIcon(R.drawable.ic_close_x)

        setListeners()
        initObservers()
    }

    private fun setListeners() {
        _binding.tvCreateAccount.setOnClickListener {
            val action =
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
        _binding.toolbarLogin.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        _binding.btnLogin.setOnClickListener {
            if (validateFields()) {
                signViewModel.signIn(
                    _binding.etEmail.text.toString(),
                    _binding.etPass.text.toString(),
                )
            } else {
                snackbar(
                    message = getString(R.string.message_error_field),
                )
            }
        }
    }

    private fun initObservers() {
        signViewModel.signIn.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Response.Error -> {
                    hideLoading()
                    state.exception.apply {
                        message?.let {
                            snackbar(
                                message = it,
                            )
                        }
                        printStackTrace()
                    }
                }
                is Response.Loading -> {
                    showLoading()
                }
                is Response.Success -> {
                    hideLoading()
                    snackbar(
                        message = getString(state.message),
                    )
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun validateFields() = when {
        _binding.etEmail.text.toString().validate().not() -> false
        _binding.etPass.text.toString().validate().not() -> false
        else -> true
    }

    private fun showLoading() {
        loadingDialog = Dialog(requireContext())
        loadingDialog?.setContentView(R.layout.progress_dialog)
        loadingDialog?.show()
    }

    private fun hideLoading() {
        loadingDialog?.hide()
    }
}
