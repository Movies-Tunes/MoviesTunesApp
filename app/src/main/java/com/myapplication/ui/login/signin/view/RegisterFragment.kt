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
import com.myapplication.databinding.FragmentRegisterBinding
import com.myapplication.ui.login.signin.viewmodel.SignViewModel
import com.myapplication.util.extension.createLoadingDialog
import com.myapplication.util.extension.snackbar
import com.myapplication.util.extension.validate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var _binding: FragmentRegisterBinding
    val binding: FragmentRegisterBinding get() = _binding
    private val loadingDialog: Dialog by lazy { createLoadingDialog() }
    private val signViewModel: SignViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.toolbarLogin.setNavigationIcon(R.drawable.ic_close_x)
        setListeners()
        initObservers()
    }

    private fun initObservers() {
        signViewModel.signIn.observe(viewLifecycleOwner) { state ->
            state?.let {
                when (state) {
                    is Response.Error -> {
                        state.exception.message?.let {
                            snackbar(
                                message = it,
                            )
                        }
                        state.exception.printStackTrace()
                    }

                    is Response.Success -> {
                        snackbar(message = getString(state.message))
                        clearFields()
                        val action =
                            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                        findNavController().navigate(R.id.action_registerFragment_to_listMoviesFragment)
                    }
                }
            }
        }
        signViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) loadingDialog.show() else loadingDialog.dismiss()
        }
    }

    private fun setListeners() {
        _binding.toolbarLogin.setNavigationOnClickListener {
            findNavController().navigate(
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(),
            )
        }
        _binding.btnLogin.setOnClickListener {
            if (validateFields()) {
                signViewModel.register(
                    _binding.etName.text.toString(),
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

    override fun onDestroyView() {
        super.onDestroyView()
        loadingDialog.dismiss()
    }

    private fun clearFields() {
        _binding.etName.text?.clear()
        _binding.etEmail.text?.clear()
        _binding.etPass.text?.clear()
    }

    private fun validateFields() = when {
        _binding.etName.text.toString().validate().not() -> {
            false
        }
        _binding.etEmail.text.toString().validate().not() -> {
            false
        }
        _binding.etPass.text.toString().validate().not() -> {
            false
        }
        else -> true
    }
}
