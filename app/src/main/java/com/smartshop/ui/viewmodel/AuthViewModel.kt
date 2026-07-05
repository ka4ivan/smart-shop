package com.smartshop.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.smartshop.data.repository.AuthRepository
import com.smartshop.data.repository.ListRepository
import com.smartshop.data.utils.UserUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val listRepository = ListRepository()

    private val _currentUser = MutableStateFlow(authRepository.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val authStateListener = FirebaseAuth.AuthStateListener {
        _currentUser.value = it.currentUser
    }

    init {
        authRepository.addAuthStateListener(authStateListener)
    }

    override fun onCleared() {
        authRepository.removeAuthStateListener(authStateListener)
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun signInWithEmail(context: Context, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val deviceId = UserUtils.getDeviceId(context)
            try {
                val user = authRepository.signInWithEmail(email, password)
                listRepository.reassignListsOwner(deviceId, user.uid)
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signUpWithEmail(context: Context, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val deviceId = UserUtils.getDeviceId(context)
            try {
                val user = authRepository.signUpWithEmail(email, password)
                listRepository.reassignListsOwner(deviceId, user.uid)
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signInWithGoogle(context: Context, idToken: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val deviceId = UserUtils.getDeviceId(context)
            try {
                val user = authRepository.signInWithGoogle(idToken)
                listRepository.reassignListsOwner(deviceId, user.uid)
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
