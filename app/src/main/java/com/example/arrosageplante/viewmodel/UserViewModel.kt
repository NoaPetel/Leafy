package com.example.arrosageplante.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.arrosageplante.data.UserData
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : ViewModel() {
    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    fun setUserData(user: FirebaseUser?) {
        Log.d("UserModel", "$user")
        user?.let {
            _userData.value = UserData(
                email = it.email ?: "",
                uid = it.uid
            )
        }
    }
}