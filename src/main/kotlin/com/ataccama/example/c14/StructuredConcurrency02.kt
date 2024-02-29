package com.ataccama.example.c14

import com.ataccama.example.say
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

/**
 * # Structured concurrency – cancellation
 *
 * - When a *coroutine* is cancelled, all its **children** are also cancelled.
 * - When an Exception is thrown from inside a coroutine, the coroutine gets cancelled,
 * and all of its children are cancelled, too.
 */
fun main() {
    val job = CoroutineScope(Dispatchers.Default).launch {
        launch { work("1", 2.seconds) }
        val job2 = launch {
            withTimeout(500.milliseconds) {
                work("2", 1.seconds)
            }
        }
        job2.invokeOnCompletion {
            say("Job 2 finished with throwable: $it")
            if (it != null) cancel()
        }
//         job2.cancel()
        say("I hope the others finish eventually, too...")
    }

    runBlocking {
        say("Waiting for all child coroutines...")
        job.join()
        say("Done!")
    }
}
