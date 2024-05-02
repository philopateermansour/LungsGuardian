package com.example.lungsguardian.data.model

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date


data class HistoryModel(
    val `$id`: String,
    val `$values`: List<Value>
)

data class Value(
    val `$id`: String,
    val caption: String,
    val image: String
)
