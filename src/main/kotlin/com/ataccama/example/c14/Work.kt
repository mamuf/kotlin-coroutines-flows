package com.ataccama.example.c14

import com.ataccama.example.say
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.DurationUnit

/**
 * Simulates work for given duration, and prints a message before and after the work.
 */
suspend fun work(name: String, duration: Duration) {
    say("Launch $name")
    withContext(Dispatchers.IO) {
        Thread.sleep(duration.toLong(DurationUnit.MILLISECONDS))
    }
    say("Done $name")
}
