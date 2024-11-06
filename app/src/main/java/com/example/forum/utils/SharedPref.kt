package com.example.forum.utils

import android.content.Context

object SharedPref {
    fun storeDetails(email: String, name: String, userName: String, imageUri: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("userName", userName)
        editor.putString("imageUri", imageUri)
        editor.apply()
    }

    fun getEmail(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return sharedPreferences.getString("email", "") ?: ""
    }

    fun getName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return sharedPreferences.getString("name", "") ?: ""
    }

    fun getUserName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userName", "") ?: ""
    }

    fun getImageUri(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return sharedPreferences.getString("imageUri", "") ?: ""
    }
}