package com.example.dogsapp.util

import android.content.Context
import android.content.SharedPreferences

import androidx.core.content.edit
import androidx.preference.PreferenceManager

class SharedPreferencesHelper {

    companion object {

        private const val PREF_TIME = "Pref time"
        private var prefs: SharedPreferences? = null

        @Volatile
        private var instance: SharedPreferencesHelper? = null
        private val LOCK = Any()

        // Here operator is used to override the function for two objects (refer $bottom)
        operator fun invoke(context: Context): SharedPreferencesHelper =
            instance ?: synchronized(LOCK) {
                instance ?: buildHelper(context).also {
                    instance = it
                }
            }

        private fun buildHelper(context: Context): SharedPreferencesHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesHelper()
        }
    }

    fun saveUpdateTime(time: Long) {
        prefs?.edit(commit = true) { putLong(PREF_TIME, time) }
    }

    fun getUpdateTime() = prefs?.getLong(PREF_TIME, 0)

    fun getCacheDuration() = prefs?.getString("pref_cache_duration", "")
}


/**     -- Operator Overriding --

Let's start with a simple data class:

data class Point(val x: Int, val y: Int)
We're going to enhance this data class with a few operators.

In order to turn a Kotlin function with a pre-defined name into an operator, we should mark the function with the operator modifier.  For example, we can overload the “+” operator:

e.g.1 :

operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)
This way we can add two Points with “+”:

>> val p1 = Point(0, 1)
>> val p2 = Point(1, 2)
>> println(p1 + p2)
>> Point(x=1, y=3)

e.g.2 :

You could use the get operator to access the positions using brackets. The implementation is very easy:

class Employee(val id: Long, val name: String)

class Company(private val employees: List) {
operator fun get(pos: Int) = employees[pos]
}

And that’s how you could use it:

val company = Company(listOf(Employee(1235, "John"), Employee(2584, "Mike")))
val mike = company[1]
But you could go beyond, and use the id to recover the value, implementing the function like this:

operator fun get(id: Long) = employees.first { it.id == id }

val mike = company[2584]

 * **/