package com.ataccama.example.c21

import com.ataccama.example.say
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

/**
 * # Channels
 *
 * - A Channel is conceptually similar to `BlockingQueue`.
 * - The key difference is that Channels are non-blocking.
 * - Channels provide a way to transfer stream of values.
 */
@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
fun main() {
    val senderScope = CoroutineScope(newSingleThreadContext("sender"))
    val receiverScope = CoroutineScope(newSingleThreadContext("receiver"))

    val channel = Channel<Int>(capacity = 4) // buffered channel
    val sendingJob = senderScope.launch {
        (1..10).forEach {
            say("Sending $it")
            channel.send(it)
        }
        channel.close() // unless closed the receiving side won't know we're done
    }
//    sendingJob.invokeOnCompletion { channel.close() }
//    sendingJob.cancel()

    val receivingJob = receiverScope.launch {
        for (value in channel) {
            say("Received $value")
        }
    }

    runBlocking {
        say("Waiting for jobs...")
        joinAll(sendingJob, receivingJob)
        say("Done!")
    }
}
