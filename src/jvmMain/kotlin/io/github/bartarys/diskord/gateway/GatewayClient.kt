package io.github.bartarys.diskord.gateway

import io.github.bartarys.diskord.gateway.data.IdentifyData
import io.github.bartarys.diskord.gateway.data.IdentifyProperties
import io.github.bartarys.diskord.gateway.entity.HeartBeatPayload
import io.github.bartarys.diskord.gateway.entity.HelloPayload
import io.github.bartarys.diskord.gateway.entity.IdentifyPayload
import io.github.bartarys.diskord.gateway.entity.Payload
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.websocket.ClientWebSocketSession
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocketSession
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.io.core.ByteReadPacket
import java.net.URI
import java.time.Duration
import kotlin.coroutines.CoroutineContext

actual class GatewayClient constructor(
    private val token: String,
    val options: GatewayClientOptions
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()

    @ExperimentalCoroutinesApi
    val channel = BroadcastChannel<Payload<*>>(Channel.CONFLATED)
    private val client = HttpClient(CIO) { this.install(WebSockets) }
    lateinit var session: ClientWebSocketSession
    private var ticker: Ticker? = null
    private var sequenceNumber: String = ""

    actual constructor(token: String, options: GatewayClientOptionsBuilder.() -> Unit)
            : this(token, GatewayClientOptionsBuilder().apply(options).build())

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    actual inline fun <reified T : Payload<*>> on(): ReceiveChannel<T> {
        return channel.openSubscription().filter { it is T }.map { it as T }
    }

    actual suspend inline fun <reified T : Payload<*>> send(payload: T) {
        withContext(Dispatchers.IO) {
            val json = options.writer.write(payload)
            session.send(Frame.Binary(true, json))
        }
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    actual suspend fun login() = coroutineScope {
        val uri = URI("wss://gateway.discord.gg/")
        val session = client.webSocketSession(
            method = HttpMethod.Get,
            path = uri.path,
            host = uri.host
        )

        launch {
            on<HelloPayload>().consumeEach {
                val duration = Duration.ofMillis(it.data.heartbeatInterval.toLong())
                ticker?.cancel()
                ticker = Ticker(duration) { send(HeartBeatPayload(sequenceNumber)) }

                sendIdentify()
            }
        }

        session.incoming.filter { it is Frame.Text }.consumeEach {
            val packet = ByteReadPacket(it.buffer)
            val payload = options.reader.read(packet)
            channel.send(payload)
        }

        session.closeReason.await()

        Unit
    }

    actual suspend fun close() {
        ticker?.cancel()
        session.close(CloseReason(CloseReason.Codes.NORMAL, ""))
        client.close()
    }

    private suspend fun sendIdentify() {
        val data = IdentifyData(token, IdentifyProperties(System.getProperty("os.name"), "diskord", "diskord"))
        val payload = IdentifyPayload(data)
        send(payload)
    }
}

private class Ticker(interval: Duration, val action: suspend () -> Unit) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()

    private val channel: ReceiveChannel<Unit> = ticker(interval.toMillis())

    init {
        launch { channel.consumeEach { action() } }
    }

    fun cancel() = channel.cancel()

}