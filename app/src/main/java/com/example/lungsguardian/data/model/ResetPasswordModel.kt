package com.example.lungsguardian.data.model

data class ResetPasswordModel(
    val Code: String,
    val ConfirmNewPassword: String,
    val Email: String,
    val NewPassword: String
)