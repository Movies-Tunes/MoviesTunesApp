package com.myapplication.ui.login.signin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
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

    private val _signIn: MutableLiveData<Response<FirebaseUser?>?> =
        MutableLiveData()
    val signIn: LiveData<Response<FirebaseUser?>?> = _signIn

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signIn.value = Response.Loading()
            _signIn.value = signInUseCase(email, password)
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signIn.value = Response.Loading()
            _signIn.value = registerUseCase(name, email, password)
        }
    }
}
