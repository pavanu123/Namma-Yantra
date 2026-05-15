package com.nammayantra.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammayantra.models.User
import com.nammayantra.repositories.FirebaseRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    private val _registerStatus = MutableLiveData<Result<User>?>(null)
    val registerStatus: LiveData<Result<User>?> = _registerStatus

    private val _loginStatus = MutableLiveData<Result<String>?>(null)
    val loginStatus: LiveData<Result<String>?> = _loginStatus

    private val _currentUser = MutableLiveData<User?>(null)
    val currentUser: LiveData<User?> = _currentUser

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(email: String, password: String, user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.registerUser(email, password, user)
            _registerStatus.value = result
            _isLoading.value = false
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.loginUser(email, password)
            if (result.isSuccess) {
                result.getOrNull()?.let { uid ->
                    val userResult = repository.getUserData(uid)
                    if (userResult.isSuccess) {
                        _currentUser.value = userResult.getOrNull()
                    }
                }
            }
            _loginStatus.value = result
            _isLoading.value = false
        }
    }

    fun logout() {
        repository.logout()
        _currentUser.value = null
    }
}