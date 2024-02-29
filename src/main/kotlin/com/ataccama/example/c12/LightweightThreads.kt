package com.ataccama.example.c12

import com.ataccama.example.measured
import com.ataccama.example.say
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread
import kotlin.time.Duration.Companion.seconds

/**
 * # "Light-weight threads"
 */
fun main() = with(Coroutines02LightweightThreads) {
    measured {
        mainWithThreads()
//        mainWithCoroutines()
    }
}

object Coroutines02LightweightThreads {

    /**
     * Allocating a bajillion threads is not a good idea...
     */
    fun mainWithThreads() {
        val count = 100_000
        val latch = CountDownLatch(count)
        repeat(count) {
            thread {
                say("launching $it")
                Thread.sleep(1000L) // heavy computation, blocking IO, etc.
                latch.countDown()
                // commented out to see the error creating ton of threads at the same time
//                say("done $it")
            }
        }
        latch.await()
    }

    /**
     * This is a cool example of the "power" of Coroutines but there's a trick...
     */
    fun mainWithCoroutines() = runBlocking {
        repeat(10_000) {
            launch {
                say("launching $it")
                delay(1.seconds) // non-blocking operation, e.g., IO
//                Thread.sleep(1000L) // heavy computation
                say("done $it")
            }
        }
    }
}
