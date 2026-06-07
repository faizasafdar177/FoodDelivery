package com.example.meituanfood.utils

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Double.toYuan(): String {
    return "¥" + String.format("%.2f", this)
}

fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(Date())
}

fun Int.toMonthlyOrders(): String {
    return if (this >= 10000) {
        String.format("%.1f0000", this / 10000.0)
    } else {
        this.toString()
    }
}
