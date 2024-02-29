package com.ataccama.example.c14

import com.ataccama.example.say
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlinx.coroutines.ThreadContextElement as ThreadContextElement1
import kotlinx.coroutines.ThreadContextElement as ThreadContextElement

/**
 * # Structured concurrency
 *
 * - A *coroutine* finishes only after all its *child coroutines* finish.
 * - `launch` creates a new coroutine and returns a `Job` that can be used to interact with that coroutine.
 * - The new coroutine is executed concurrently, `launch` doesn't block the parent coroutine.
 */
fun main() {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val job = coroutineScope.launch {
        launch { work("1", 2.seconds) }
        launch {
            work("2", 1.seconds)
            launch { work("3", 1.seconds) }
        }
        say("I hope the others finish eventually, too...")
    }

    runBlocking {
        say("Waiting for all child coroutines...")
        job.join()
        say("Done!")
    }
}

suspend fun work(name: String, duration: Duration) {
    say("Launch $name")
    withContext(Dispatchers.IO) {
        Thread.sleep(duration.toLong(DurationUnit.MILLISECONDS))
    }
    say("Done $name")
}
