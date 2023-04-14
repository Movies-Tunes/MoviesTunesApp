package com.myapplication.ui.login.signin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myapplication.core.Response
import com.myapplication.domain.usecases.RegisterUseCase
import com.myapplication.domain.usecases.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val _signIn: MutableLiveData<Response<FirebaseUser?>> = MutableLiveData()
    val signIn: LiveData<Response<FirebaseUser?>> = _signIn

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signIn.value = Response.Loading()
            when (val response = signInUseCase(email, password)) {
                is Response.Error -> {
                    _signIn.value = Response.Error(response.exception)
                }
                is Response.Loading -> {}
                is Response.Success -> {
                    _signIn.value = Response.Success(response.data.user)
                }
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signIn.value = Response.Loading()
            when (val response = registerUseCase(name, email, password)) {
                is Response.Error -> {
                    _signIn.value = Response.Error(response.exception)
                }
                is Response.Loading -> {}
                is Response.Success -> {
                    _signIn.value = Response.Success(response.data.user)
                }
            }
        }
    }
}
