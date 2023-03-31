package com.myapplication.ui.login.signin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myapplication.core.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignViewModel : ViewModel() {

    private val _signIn: MutableLiveData<Response<FirebaseUser?>> = MutableLiveData()
    val signIn: LiveData<Response<FirebaseUser?>> = _signIn
    val auth = Firebase.auth

    fun signIn(email: String, password: String) {
        _signIn.value = Response.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val result = auth.signInWithEmailAndPassword(email, password).addOnSuccessListener { result ->
                _signIn.value = Response.Success(result.user)
            }.addOnFailureListener { exception ->
                exception.printStackTrace()
            }.await()
        }
    }

    fun register(name: String, email: String, password: String) {
        _signIn.value = Response.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val result = auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { result ->
                updateProfile(name, result)
                _signIn.value = Response.Success(result.user)
            }.addOnFailureListener { exception ->
                exception.printStackTrace()
            }.await()
        }
    }

    private fun updateProfile(name: String, result: AuthResult) {
        val requestUpdateProfile = UserProfileChangeRequest.Builder()
        requestUpdateProfile.displayName = name
        result.user?.updateProfile(requestUpdateProfile.build())
    }
}
