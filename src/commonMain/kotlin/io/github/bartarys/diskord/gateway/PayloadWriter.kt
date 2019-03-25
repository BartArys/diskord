package io.github.bartarys.diskord.gateway

import kotlinx.io.core.ByteReadPacket

interface PayloadWriter {

    suspend fun<T: Any> write(payload: T) : ByteReadPacket

    companion object
}

expect fun PayloadWriter.Companion.default() : PayloadWriter