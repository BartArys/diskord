package io.github.bartarys.diskord.gateway.entity

import io.github.bartarys.diskord.gateway.data.*

expect open class Payload<T> constructor(
    @JsonName("op")
    opCode: OpCode,
    @JsonName("d")
    data: T
) {
    val opCode: OpCode
    val data: T
}

@Receive
class HelloPayload(data: HelloData) : Payload<HelloData>(OpCode.Hello, data)

@Receive
class ReadyPayload(data: ReadyData) : Payload<ReadyData>(OpCode.Dispatch, data)

@Send
expect open class DispatchPayload<T> constructor(
    opCode: OpCode,
    data: T,
    sequenceNumber: String?,
    eventName: String?
) : Payload<T> {
    val sequenceNumber: String?
    val eventName: String?
}

@Receive
class ResumedPayload(
    data: ResumedData? = null
) : DispatchPayload<ResumedData?>(OpCode.Dispatch, data, null, "RESUMED")

@Send
class IdentifyPayload(data: IdentifyData) : Payload<IdentifyData>(OpCode.Identify, data)

@Send
@Receive
class HeartBeatPayload(data: String) : Payload<String>(OpCode.Heartbeat, data)

@Receive
class HeartBeatAckPayload(data: Any?) : DispatchPayload<Any?>(OpCode.HeartbeatACK, data, null, null)

