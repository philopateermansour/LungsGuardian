package com.example.lungsguardian.data.model

data class HistoryModel(
    val `$id`: String,
    val `$values`: List<Value>
)

data class Value(
    val `$id`: String,
    val caption: String,
    val image: String
)