package com.example.lungsguardian.data.model

data class LoginResponse(
    val email: String,
    val fullName: String,
    val token: String
)