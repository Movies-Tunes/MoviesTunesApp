package com.myapplication.domain.usecases

import com.google.firebase.auth.*
import com.myapplication.core.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RegisterUseCase {
    suspend operator fun invoke(name: String, email: String, password: String): Response<FirebaseUser?>
}

class RegisterUseCaseImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : RegisterUseCase {
    override suspend fun invoke(
        name: String,
        email: String,
        password: String,
    ): Response<FirebaseUser?> =
        withContext(dispatcher) {
            try {
                Response.Success(
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { result ->
                            updateProfile(name, result)
                        }.await().user,
                )
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                e.printStackTrace()
                Response.Error(e)
            } catch (e: FirebaseAuthUserCollisionException) {
                e.printStackTrace()
                Response.Error(e)
            } catch (e: Exception) {
                e.printStackTrace()
                Response.Error(e)
            }
        }

    private fun updateProfile(name: String, result: AuthResult?) {
        val requestUpdateProfile = UserProfileChangeRequest.Builder()
        requestUpdateProfile.displayName = name
        result?.user?.updateProfile(requestUpdateProfile.build())
    }
}
