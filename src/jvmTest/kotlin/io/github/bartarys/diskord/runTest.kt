package io.github.bartarys.diskord

import kotlinx.coroutines.runBlocking

actual fun runTest(block: suspend () -> Unit) = runBlocking {
    block()
}