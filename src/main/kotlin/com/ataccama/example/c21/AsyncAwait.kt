package com.ataccama.example.c21

import com.ataccama.example.measured
import com.ataccama.example.say
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

/**
 * # Sequential by default
 *
 * - Calls to suspending functions are sequential by default.
 * - If we want to execute two operations asynchronously, and get their results, we need to use `async`.
 * - `async` is similar to `launch` in that it starts a new coroutine.
 * - The difference is that returns a `Deferred` value instead of a `Job`.
 * - The combination of `async` and `Deferred.await` provides a way to transfer a value between coroutines.
 */
fun main() {
    measured { sequential() }
    println()
    measured { asynchronous() }
}

/**
 * `getBar` is only called after `getFoo` finishes.
 */
private fun sequential() {
    runBlocking(Dispatchers.Default) {
        val foo = getFoo()
        val bar = getBar()
        say("Got $foo and $bar")
    }
}

/**
 * Both `getFoo` and `getBar` are executed in concurrent coroutines.
 */
private fun asynchronous() {
    runBlocking(Dispatchers.Default) {
        val foo = async { getFoo() }
        val bar = async { getBar() }
        say("Got ${foo.await()} and ${bar.await()}")
    }
}

suspend fun getFoo(): String {
    say("Getting Foo")
    delay(2.seconds)
    say("Returning Foo")
    return "Foo"
}

suspend fun getBar(): String {
    say("Getting Bar")
    delay(1.seconds)
    say("Returning Bar")
    return "Bar"
}
