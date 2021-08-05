package com.example.room

/**
 * It's a boilerplate code for "Use an Event wrapper".
 *
 * refer
 * https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 *
 * 2021 Update: Working with Kotlin? I recommend you move to Channels!
 * https://proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}