package com.ataccama.example.c31

import com.ataccama.example.say
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

/**
 * # Flows
 *
 * - Flows are non-blocking, cold streams of data.
 * - They are similar to Java Streams and Kotlin Sequences in that they evaluate lazily.
 * - The main difference is that Flows respect the structured concurrency
 * and adhere to the cooperative cancellation of coroutines.
 */
@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
fun main() {
    val requestContext = newSingleThreadContext("requestContext")
    val responseContext = newSingleThreadContext("responseContext")

    val requests = flow<Int> {
        // flow builder is executed only when the flow is collected
        say("Start emitting requests...") // (3)
        (1..4).forEach {
            say("Emitting $it") // (4)
            emit(it)
        }
        say("Finished remitting requests.") // (5)
    }.flowOn(requestContext) // by default flows on the scope from which `collect` is called

    val responses = flow<String> {
        say("Start processing requests...") // (2)
        requests
            .onEach { say("Got request $it") } // (6)
            .filter { it % 2 == 0 }
            .map { "processed-$it" }
            .onEach { say("Mapped to response $it") } // (7)
            .collect(this)
        say("Done processing requests into responses") // (9)
    }.flowOn(responseContext)

    runBlocking {
        say("Collecting responses...") // (1)
        responses.collect { say("Received response $it") } // (8)
        say("Done!")
    }
}
