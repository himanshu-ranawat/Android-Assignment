package com.example.core2

/**
 * [Event] :
 *
 * Wrapper class used to receive single live event based on `LiveData` of particular state where
 * it needs to be notified just once per call, like calling `create, update or delete APIs` rather than
 * fetching list of data.
 *
 * [Credits](https://gist.github.com/JoseAlcerreca/5b661f1800e1e654f07cc54fe87441af)
 *
 */
open class Event<out T>(private val content: T) {

    /**
     * Object as [Boolean] flag used to dedicate status of whether [T] has been notified or not.
     */
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
     * Returns the content, even if it's already been handled as exceptional case.
     */
    fun peekContent(): T = content
}