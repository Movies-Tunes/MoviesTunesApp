package com.myapplication.ui.login.signin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myapplication.core.Response
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignViewModel : ViewModel() {

    private val _signIn: MutableLiveData<Response<FirebaseUser?>> = MutableLiveData()
    val signIn: LiveData<Response<FirebaseUser?>> = _signIn
    val auth = Firebase.auth

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signIn.value = Response.Loading()
            try {
                val result = auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        _signIn.value = Response.Success(result.user)
                    }.await()
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _signIn.value = Response.Error(e)
                e.printStackTrace()
            } catch (e: Exception) {
                _signIn.value = Response.Error(e)
                e.printStackTrace()
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signIn.value = Response.Loading()
            try {
                val result = auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        updateProfile(name, result)
                        _signIn.value = Response.Success(result.user)
                    }.await()
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _signIn.value = Response.Error(e)
                e.printStackTrace()
            } catch (e: FirebaseAuthUserCollisionException) {
                _signIn.value = Response.Error(e)
                e.printStackTrace()
            } catch (e: Exception) {
                _signIn.value = Response.Error(e)
                e.printStackTrace()
            }
        }
    }

    private fun updateProfile(name: String, result: AuthResult) {
        val requestUpdateProfile = UserProfileChangeRequest.Builder()
        requestUpdateProfile.displayName = name
        result.user?.updateProfile(requestUpdateProfile.build())
    }
}
