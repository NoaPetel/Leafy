package com.example.arrosageplante.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.arrosageplante.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : ViewModel() {
    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            setUserData(currentUser)
        }
    }

    fun setUserData(user: FirebaseUser?) {
        Log.d("UserModel", "$user")
        val appUser = user?.let {
            UserData(
                uid = it.uid,
                email = it.email ?: "",
                // Add other necessary fields
            )
        }
        _userData.value = appUser
    }
}