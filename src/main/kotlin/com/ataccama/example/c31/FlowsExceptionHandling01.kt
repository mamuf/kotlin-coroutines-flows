package com.ataccama.example.c31

import com.ataccama.example.say
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

/**
 * # Exception handling in Flows
 *
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
        responses.collect { say("Received response $it") }
        say("Done!")
    }
}
