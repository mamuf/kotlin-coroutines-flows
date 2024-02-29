package com.ataccama.example.com.ataccama.example.c31

import com.ataccama.example.say
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

/**
 * # Flows
 *
 * - Flows are non-blocking, cold streams of data.
 * - They are similar to Java Streams and Kotlin Sequences in that they evaluate lazily.
 * - The main difference is that Flows respect the structured concurrency
 * and adhere to the cooperative cancellation of coroutines.
 */
fun main() {
    val requests = flow {
        say("Start emitting requests...")
        repeat(2) {
            say("Emitting $it")
            emit(it)
            error("Houston, we have a problem.")
        }
    }

    val responses = flow {
        say("Start processing requests...")
        requests
            .onEach { say("Got request $it") }
            .map { "processed-$it" }
            .onEach { say("Mapped to response $it") }
            .collect(this)
        say("Done processing requests into responses")
    }

    runBlocking {
        say("Collecting responses...")
        responses
            .onCompletion { say("Completed with $it") } // invoked only *after* the flow is completed
//            .catch { say("Caught $it") } // catches upstream exceptions, but not any thrown downstream
//            .onEach { error("Oops!") }
            .collect { say("Received response $it") }
        say("Done!")
    }
}
