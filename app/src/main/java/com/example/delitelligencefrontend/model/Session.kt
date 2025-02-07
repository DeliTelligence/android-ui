package com.example.delitelligencefrontend.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Session @Inject constructor(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun setUser(employee: Employee?) {
        val employeeJson = gson.toJson(employee)
        prefs.edit().putString(KEY_EMPLOYEE, employeeJson).apply()
    }

    fun getUser(): Employee? {
        val employeeJson = prefs.getString(KEY_EMPLOYEE, null)
        return employeeJson?.let { gson.fromJson(it, Employee::class.java) }
    }

    companion object {
        private const val PREFERENCE_NAME = "my_app_preferences"
        private const val KEY_EMPLOYEE = "employee"
    }
}