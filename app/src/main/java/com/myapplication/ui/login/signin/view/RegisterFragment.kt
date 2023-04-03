package com.myapplication.ui.login.signin.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.myapplication.R
import com.myapplication.core.Response
import com.myapplication.databinding.FragmentRegisterBinding
import com.myapplication.ui.login.signin.viewmodel.SignViewModel
import com.myapplication.util.extension.validate

class RegisterFragment : Fragment() {

    private lateinit var _binding: FragmentRegisterBinding
    val binding: FragmentRegisterBinding get() = _binding
    private var loadingDialog: Dialog? = null
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
            when (state) {
                is Response.Error -> {
                    hideLoading()
                    state.exception.message?.let {
                        Snackbar.make(
                            requireView(),
                            it,
                            Snackbar.LENGTH_SHORT,
                        ).show()
                    }
                    state.exception.printStackTrace()
                }
                is Response.Loading -> {
                    showLoading()
                }
                is Response.Success -> {
                    hideLoading()
                    Snackbar.make(
                        requireView(),
                        state.message,
                        Snackbar.LENGTH_SHORT,
                    ).show()
                    clearFields()
                    val action =
                        RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                    findNavController().navigate(R.id.action_registerFragment_to_listMoviesFragment)
                }
            }
        }
    }

    private fun setListeners() {
        _binding.toolbarLogin.setNavigationOnClickListener {
            val action =
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        _binding.btnLogin.setOnClickListener {
            if (validateFields()) {
                signViewModel.register(
                    _binding.etName.text.toString(),
                    _binding.etEmail.text.toString(),
                    _binding.etPass.text.toString(),
                )
            } else {
                Snackbar.make(
                    requireView(),
                    "Fields can't be null",
                    Snackbar.LENGTH_SHORT,
                ).show()
            }
        }
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

    private fun showLoading() {
        loadingDialog = Dialog(requireContext())
        loadingDialog?.setContentView(R.layout.progress_dialog)
        loadingDialog?.show()
    }

    private fun hideLoading() {
        loadingDialog?.hide()
    }
}
