package com.myapplication.domain.usecases

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.myapplication.core.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SignInUseCase {
    suspend operator fun invoke(email: String, password: String): Response<AuthResult>
}

class SignInUseCaseImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : SignInUseCase {
    override suspend fun invoke(email: String, password: String): Response<AuthResult> =
        withContext(dispatcher) {
            try {
                Response.Success(
                    auth.signInWithEmailAndPassword(email, password).await(),
                )
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                e.printStackTrace()
                Response.Error(e)
            } catch (e: Exception) {
                e.printStackTrace()
                Response.Error(e)
            }
        }
}
