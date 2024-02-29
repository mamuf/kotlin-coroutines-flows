package com.ataccama.example.c13

import com.ataccama.example.measured
import com.ataccama.example.say
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * # "Light-weight threads"
 */
fun main() = with(Coroutines03VersusThreadPools) {
    measured {
        mainWithThreadPool()
//        mainWithCoroutinesCustomThreadPool()
//        mainWithCoroutines()
    }
}

object Coroutines03VersusThreadPools {

    /**
     * Let's use a thread pool executor instead of creating new threads.
     */
    fun mainWithThreadPool() {
        val threadPool = Executors.newFixedThreadPool(64)

        repeat(1000) {
            threadPool.execute {
                say("launching $it")
                Thread.sleep(100L) // heavy computation, blocking IO, etc.
                say("done $it")
            }
        }

        threadPool.shutdown()
        threadPool.awaitTermination(1, TimeUnit.HOURS)
    }

    /**
     * Now compare it to Coroutines with the same thread-pool.
     */
    fun mainWithCoroutinesCustomThreadPool() {
        val threadPool = Executors.newFixedThreadPool(64)
        val dispatcher = threadPool.asCoroutineDispatcher()

        // we could use Dispatchers.IO, which uses a 64 thread pool as well
        runBlocking(dispatcher) {
            repeat(1000) {
                launch {
                    say("launching $it")
                    Thread.sleep(100L) // heavy computation
                    say("done $it")
                }
            }
        }

        threadPool.shutdown() // required
        threadPool.awaitTermination(1, TimeUnit.HOURS)
    }

    /**
     * You should pick an appropriate dispatcher, or create your own for heavy parallelism.
     *
     * - `runBlocking` without a specific dispatcher executes everything in the current thread, i.e., zero parallelism.
     * - `Dispatchers.Default` defaults to number of threads equal to CPU core count
     * - `Dispatchers.IO` defaults to 64 threads (or CPU core count, if higher)
     */
    fun mainWithCoroutines() {
//        runBlocking {
        runBlocking(Dispatchers.Default) {
            repeat(1000) {
                launch {
                    say("launching $it")
                    Thread.sleep(100L) // heavy computation
                    say("done $it")
                }
            }
        }
    }
}
