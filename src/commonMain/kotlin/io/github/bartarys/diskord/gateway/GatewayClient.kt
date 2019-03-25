package io.github.bartarys.diskord.gateway

import io.github.bartarys.diskord.gateway.entity.Payload
import kotlinx.coroutines.channels.ReceiveChannel

class GatewayClientOptionsBuilder {
    var writer: PayloadWriter = PayloadWriter.default()
    var reader: PayloadReader = PayloadReader.default()
    var retry: Retry = Retry.LinearBackoff()

    fun build(): GatewayClientOptions = GatewayClientOptions(writer, reader, retry)
}

data class GatewayClientOptions(
    val writer: PayloadWriter,
    val reader: PayloadReader,
    val retry: Retry
)

expect class GatewayClient(token: String, options: GatewayClientOptionsBuilder.() -> Unit = {}) {

    inline fun <reified T : Payload<*>> on(): ReceiveChannel<T>

    suspend inline fun <reified T : Payload<*>> send(payload: T)

    suspend fun login()

    suspend fun close()
}

expect sealed class Retry {
    abstract val continueRetry: Boolean

    abstract suspend fun failure()
    abstract fun success()

    object None : Retry

    class LinearBackoff() : Retry

    class ExponentialBackoff() : Retry
}
