package com.ataccama.example

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.time.measureTime

/**
 * Prints message with time stamp and thread name.
 */
fun say(message: String) {
    Utils.logger.info(message)
}

/**
 * Measures the execution time of given code and prints the total.
 */
fun measured(block: () -> Unit) {
    val duration = measureTime(block)
    say("Took $duration")
}

object Utils {
    val logger: Logger = LogManager.getLogger()
}
