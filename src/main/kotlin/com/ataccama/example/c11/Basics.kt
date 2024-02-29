package com.ataccama.example.c11

import com.ataccama.example.say
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * # What are Coroutines
 *
 * - "light-weight threads"
 * - suspendable computation
 * - structured concurrency
 */
fun main() {
    runBlocking {// bridge blocking and non-blocking code
        launch { // launch a new coroutine and continue
            delay(1000L) // non-blocking delay for 1 second
            say("World!") // print after delay
        }

        // main coroutine continues while a previous one is delayed
        say("Hello")
    }
}
