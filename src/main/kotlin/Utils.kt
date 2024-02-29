package com.ataccama.example

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.time.measureTime

/**
 * Prints message with time stamp and thread name.
 */
fun say(message: String) {
    val time = LocalTime.now().format(Utils.timePattern)
    val thread = Thread.currentThread().name
    println("$time [$thread] $message")
}

/**
 * Measures the execution time of given code and prints the total.
 */
fun measured(block: () -> Unit) {
    val duration = measureTime(block)
    say("Took $duration")
}

object Utils {
    val timePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
}
